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

app.get('/embed-index', function(req, res) {
    res.sendFile(path.join(__dirname, '/embed-index.html')); 
});

app.get('/storageaccessapi', function(req, res) {
    res.sendFile(path.join(__dirname, '/storageAccessAPI.html')); 
});

app.get('/simplecookies', function(req, res) {
    res.sendFile(path.join(__dirname, '/simplecookies.html')); 
});

app.get('/embed.js', function(req, res) {
    res.setHeader('Content-Type', 'application/javascript');
    res.sendFile(path.join(__dirname, '/embed-site.js')); // Replace with the actual path to your JavaScript file
});

app.get('/setCookieScript.js', function(req, res) {
    res.setHeader('Content-Type', 'application/javascript');
    res.sendFile(path.join(__dirname, 'setCookieScript.js')); // Replace with the actual path to your JavaScript file
});

app.get('/readCookieScript.js', function(req, res) {
    res.setHeader('Content-Type', 'application/javascript');
    res.sendFile(path.join(__dirname, 'readCookieScript.js')); // Replace with the actual path to your JavaScript file
});

app.get('/readCookieScriptSimple.js', function(req, res) {
    res.setHeader('Content-Type', 'application/javascript');
    res.sendFile(path.join(__dirname, 'readCookieScriptSimple.js')); // Replace with the actual path to your JavaScript file
});



var httpsServer = https.createServer(credentials, app);
httpsServer.listen(443);
