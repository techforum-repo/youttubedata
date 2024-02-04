const express = require('express')
const path = require('path')
const https = require('https');
const fs = require('fs');


var app = express()


app.get('/index.html', function (req, res) { 

  res.sendFile('index.html', {
        root: path.join(__dirname, '.')
    })
})

app.get('/embed.html', function (req, res) { 

  res.sendFile('embed.html', {
        root: path.join(__dirname, '.')
    })
})

app.get('/test.html', function (req, res) { 

  res.sendFile('test.html', {
        root: path.join(__dirname, '.')
    })
})





https.createServer({
  key: fs.readFileSync('ssl\\server.key'),
  cert: fs.readFileSync('ssl\\server.crt')
}, app)
.listen(443, function () {
  console.log('Example app listening on port 443! Go to https://localhost/')
})







