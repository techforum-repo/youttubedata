const express = require('express')
const path = require('path')
const fs = require('fs');
const https = require('https');


var app = express()


app.get('/index.html', function (req, res) {

    res.setHeader("Speculation-Rules","speculationrules.json");

    //res.setHeader("Content-Security-Policy","script-src 'sha256-wLB7fawn1OEDXFoR8x81iN30/CAWyrJKPFS+YiloUxE=' 'sha256-8BXDaXebWVQggTlJl7D/AtBiwaJhjkDWjXZr94Wx9DE=';default-src 'self'");
    //res.setHeader("Content-Security-Policy","script-src 'inline-speculation-rules';default-src 'self'");
   // res.setHeader("Content-Security-Policy","script-src 'sha256-tokUIVR/+5j287MKcZVNAm+ZnOJpeTCDhz96SDs6m7I=' 'sha256-5qxWxJybXNoIW74RTv8nlDpyBIFGEk1IjqOzqrnDrE4=';default-src 'self'")
    res.sendFile('index.html', { root: path.join(__dirname, '.') });
});

app.get('/test1.html', function (req, res) { 

  console.log('test1.html Sec-Purpose: '+req.headers['sec-purpose']);

  res.sendFile('test1.html', {
    root: path.join(__dirname, '.')
})

})

app.get('/test2.html', function (req, res) { 

  console.log('test2.html Sec-Purpose: '+req.headers['sec-purpose']);

  res.sendFile('test2.html', {
        root: path.join(__dirname, '.')
    })
})

app.get('/test3.html', function (req, res) { 


  console.log('test3.html Sec-Purpose: '+req.headers['sec-purpose']);

  res.sendFile('test3.html', {
        root: path.join(__dirname, '.')
    })
})

app.get('/test4.html', function (req, res) { 


  console.log('test4.html Sec-Purpose: '+req.headers['sec-purpose']);

  res.sendFile('test4.html', {
        root: path.join(__dirname, '.')
    })
})
app.get('/test5.html', function (req, res) { 

  console.log('test5.html Sec-Purpose: '+req.headers['sec-purpose']);

  res.sendFile('test5.html', {
        root: path.join(__dirname, '.')
    })
})

app.get('/test6.html', function (req, res) { 


  console.log('test6.html Sec-Purpose: '+req.headers['sec-purpose']);

  res.sendFile('test6.html', {
        root: path.join(__dirname, '.')
    })
})



app.get('/selectormatch.html', function (req, res) { 

  console.log('selectormatch.html Sec-Purpose: '+req.headers['sec-purpose']);
  // Prefetch - Sec-Purpose: prefetch, Prerender - Sec-Purpose: prefetch;prerender

  res.sendFile('selectormatch.html', {
        root: path.join(__dirname, '.')
    })
})


app.get('/speculationrules.json', function (req, res) { 

  console.log("Requested...");

  res.setHeader("Content-Type","application/speculationrules+json");

  res.sendFile('speculationrules.json', {
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







