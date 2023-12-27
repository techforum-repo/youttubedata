var fs = require('fs');
var https = require('https');
var path = require('path'); // Add this line to import the 'path' module

var privateKey  = fs.readFileSync('sslcert/server.key', 'utf8');
var certificate = fs.readFileSync('sslcert/server.crt', 'utf8');

var credentials = {key: privateKey, cert: certificate};
var express = require('express');
var app = express();

app.get('/site-a', function(req, res) {
    res.sendFile(path.join(__dirname, '/sitea-index.html'));
});

app.get('/site-b', function(req, res) {
    res.sendFile(path.join(__dirname, '/siteb-index.html')); 
});

app.get('/embed-indexsitea', function(req, res) {
    res.sendFile(path.join(__dirname, '/embed-indexsitea.html')); 
});

app.get('/embed-indexsiteb', function(req, res) {
  res.sendFile(path.join(__dirname, '/embed-indexsiteb.html')); 
});

app.get('/embed-sitea.js', function(req, res) {
    res.setHeader('Content-Type', 'application/javascript');
    res.sendFile(path.join(__dirname, '/embed-sitea.js')); // Replace with the actual path to your JavaScript file
});

app.get('/embed-siteb.js', function(req, res) {
    res.setHeader('Content-Type', 'application/javascript');
    res.sendFile(path.join(__dirname, '/embed-siteb.js')); // Replace with the actual path to your JavaScript file
});

app.get('/sitea.js', function(req, res) {
    res.setHeader('Content-Type', 'application/javascript');
    res.sendFile(path.join(__dirname, '/sitea.js')); // Replace with the actual path to your JavaScript file
});

var httpsServer = https.createServer(credentials, app);
httpsServer.listen(443);
