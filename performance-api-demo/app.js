const express = require('express')
const path = require('path')
const http = require('http');
const fs = require('fs');

var app = express()

app.get('/performanceapidemo.html',function (req, res) { 

    res.sendFile('performanceapidemo.html', {
        root: path.join(__dirname, '.')
    })
})

//scripts
app.get('/highresolutionapi.js',function (req, res) { 

    res.sendFile('highresolutionapi.js', {
        root: path.join(__dirname, '.')
    })
})

app.get('/navigationtimingapi.js',function (req, res) { 

    res.sendFile('navigationtimingapi.js', {
        root: path.join(__dirname, '.')
    })
})

app.get('/painttimingapi.js',function (req, res) { 

    res.sendFile('painttimingapi.js', {
        root: path.join(__dirname, '.')
    })
})

app.get('/resourcetimingapi.js',function (req, res) { 

    res.sendFile('resourcetimingapi.js', {
        root: path.join(__dirname, '.')
    })
})

app.get('/usertimingapi.js',function (req, res) { 

    res.sendFile('usertimingapi.js', {
        root: path.join(__dirname, '.')
    })
})



http.createServer(app).listen(80, function () {
  console.log('Example app listening on port 80! Go to http://localhost/')
})