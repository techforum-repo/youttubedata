var express = require('express');
var app = express();

var data = {name:'Tech Forum', year: new Date().getFullYear(),links:['Test1', 'Test2', 'Test3']} 

//app.set('views', './customfolder')

// set the view engine to ejs
app.set('view engine', 'ejs');

// use res.render to load up an ejs view file

// index page 
app.get('/', function(req, res) {
    res.render('pages/index',{data:data});
});

app.listen(8080);
console.log('8080 is the magic port');