const express = require('express')
const path = require('path')
const https = require('https');
const fs = require('fs');
const cookie = require('cookie');


var app = express()

app.get('/cookies.html', function (req, res) { 


//res.setHeader('Set-Cookie', [cookie.serialize('sessionCookie',"Session Cookie Demo", {path:'/',httpOnly: true,'secure': true}),
							 //cookie.serialize('persistenceCookie',"Persistence Cookie Demo", {path:'/',maxAge: 900,httpOnly: true,'secure': true})]);
							 
res.setHeader('Set-Cookie', [cookie.serialize('sessionCookie',"Session Cookie Demo", {path:'/','secure': true}),
							 cookie.serialize('persistenceCookie',"Persistence Cookie Demo", {path:'/',maxAge: 900,'secure': true})]);


   res.sendFile('cookies.html', {
        root: path.join(__dirname, '.')
    })
})


app.get('/webstorageapi.html', function (req, res) { 

  res.sendFile('webstorageapi.html', {
        root: path.join(__dirname, '.')
    })
})

app.get('/indexdb.html', function (req, res) { 

  res.sendFile('indexdb.html', {
        root: path.join(__dirname, '.')
    })
})


app.get('/websqlapi.html', function (req, res) { 

  res.sendFile('websqlapi.html', {
        root: path.join(__dirname, '.')
    })
})


app.get('/cachestorageapi.html', function (req, res) { 

  res.sendFile('cachestorageapi.html', {
        root: path.join(__dirname, '.')
    })
})

app.get('/data.json', function (req, res) { 

res.setHeader("Content-Type","application/json");

  res.sendFile('data.json', {
        root: path.join(__dirname, '.')
    })
})

app.get('/data1.json', function (req, res) { 

res.setHeader("Content-Type","application/json");

  res.sendFile('data1.json', {
        root: path.join(__dirname, '.')
    })
})

app.get('/data2.json', function (req, res) { 

res.setHeader("Content-Type","application/json");

  res.sendFile('data2.json', {
        root: path.join(__dirname, '.')
    })
})

app.get('/data3.json', function (req, res) { 

res.setHeader("Content-Type","application/json");

  res.sendFile('data3.json', {
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







