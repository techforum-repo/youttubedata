const express = require('express')
const path = require('path')
const http = require('http');
const fs = require('fs');

const helmet=require('helmet');
//const helmetcsp=require('helmet-csp');

//const csp = require('content-security-policy');

//express-csp-header
//express-csp

var app = express()

//Helmet Module - CSP

//app.use(helmet()); // Apply All the default Security Headers

app.use(helmet.contentSecurityPolicy()); // Apply Default CSP header

/*app.use(helmet.contentSecurityPolicy({
    directives:{
      defaultSrc:["'self'"],
      scriptSrc:["'self'",'code.jquery.com','maxcdn.bootstrapcdn.com'],
      styleSrc:["'self'",'maxcdn.bootstrapcdn.com'],
      fontSrc:["'self'",'maxcdn.bootstrapcdn.com']}}));*/
  
  /*var cspHandlerhelmet = helmet.contentSecurityPolicy({
    directives: {
        frameSrc: ["'self'"],
        defaultSrc: ["'self'"]
    }
    });*/

//Helmet CSP Module

//app.use(helmetcsp())

  /*app.use(helmetcsp({
    directives:{
      defaultSrc:["'self'"],
      scriptSrc:["'self'",'code.jquery.com','maxcdn.bootstrapcdn.com'],
      styleSrc:["'self'",'maxcdn.bootstrapcdn.com'],
      fontSrc:["'self'",'maxcdn.bootstrapcdn.com']}}));*/

  /*var cspHandlerhelmetcsp = helmetcsp({
    directives: {
        frameSrc: ["'self'"],
        defaultSrc: ["'self'"]
    }
    });*/

  //Content Security Policy Module

  /*const cspPolicy = {
  'report-uri': '/reporting',
  'default-src': csp.SRC_NONE,
  'script-src': [ csp.SRC_SELF, csp.SRC_DATA ]
  };
 
const globalCSP = csp.getCSP(csp.STARTER_OPTIONS);
const localCSP = csp.getCSP(cspPolicy);

app.use(globalCSP);*/
    
app.get('/cspresponseheaders.html', /*cspHandlerhelmet*localCSP,*/function (req, res) { 

   //res.setHeader("Content-Security-Policy", "script-src 'self' https://code.jquery.com https://maxcdn.bootstrapcdn.com");

  res.sendFile('cspresponseheaders.html', {
        root: path.join(__dirname, '.')
    })
})

http.createServer(app).listen(80, function () {
  console.log('Example app listening on port 80! Go to http://localhost/')
})