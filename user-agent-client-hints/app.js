const express = require('express')
const path = require('path')
const http = require('http');
const https = require('https');
const fs = require('fs');
const cookie = require('cookie');


var app = express()



app.get('/index.html', function (req, res) { 

res.setHeader('Accept-CH', 'sec-ch-ua-platform,sec-ch-ua-arch,sec-ch-ua-model,sec-ch-ua-platform-version,sec-ch-ua-full-version,sec-ch-ua-bitness');

console.log('Index.html: '+req.headers['sec-ch-ua-platform']);

  res.sendFile('index.html', {
        root: path.join(__dirname, '.')
    })
})


app.get('/index1.html', function (req, res) { 

res.setHeader('Accept-CH', 'sec-ch-ua-platform,sec-ch-ua-arch,sec-ch-ua-model,sec-ch-ua-platform-version,sec-ch-ua-full-version');

console.log('Index1.html: '+req.headers['sec-ch-ua-platform']);

//res.setHeader('permissions-policy','ch-ua-arch=("https://sub.myexample.com"), ch-ua-model=("https://sub.myexample.com"), ch-ua-platform=("https://sub.myexample.com"), ch-ua-platform-version=("https://sub.myexample.com"), ch-ua-full-version=("https://sub.myexample.com")')

res.setHeader('Feature-Policy','ch-ua-arch https://sub.myexample.com, ch-ua-model https://sub.myexample.com,ch-ua-platform https://sub.myexample.com, ch-ua-platform-version https://sub.myexample.com, ch-ua-full-version https://sub.myexample.com');

res.setHeader('Vary','Accept,sec-ch-ua-paltform,sec-chua-arch');

  res.sendFile('index1.html', {
        root: path.join(__dirname, '.')
    })
})


app.get('/child.html', function (req, res) { 

console.log('Child.html: '+req.headers['sec-ch-ua-platform']);

  res.sendFile('child.html', {
        root: path.join(__dirname, '.')
    })
})

http.createServer(app)
.listen(80, function () {
  console.log('Example app listening on port 80! Go to http ://localhost/')
})


https.createServer({
  key: fs.readFileSync('ssl\\server.key'),
  cert: fs.readFileSync('ssl\\server.crt')
}, app)
.listen(443, function () {
  console.log('Example app listening on port 443! Go to https://localhost/')
})







