const emailconfig = require('./emailconfig.js');

let nodemailer = require("nodemailer");
const Email = require('email-templates');

var express = require('express');
var app = express();

let transporter = nodemailer.createTransport({
   //service:'gmail',
   host:emailconfig.host,
   port:emailconfig.port,
   //iSsecure:emailconfig.iSsecure
   
    //auth: {
      //  user: emailconfig.username,
        //pass: emailconfig.password
    //}
});

const email = new Email({
  views: { root:'./server/templates', options: { extension: 'ejs' } },
  message: {
    from: emailconfig.from,	
	attachments:
	[{
	  filename:'hello.txt',
	  path:'./server/files/hello.txt',
	  contentType:'text/plain'
	},
	{
	  filename:'sample.txt',
	  content:'sample attachement - content',
	  contentType:'text/plain'
	}]
  },
  preview:true,
  send: true,
  transport: transporter
  //transport: {
	//jsonTransport: true
  //}
  
});
 
 
app.get('/sendEmail', function (req, res) {	
	
email.send({
    template: 'test',
    message: {
      to: 'albinsharp@gmail.com'//,
	// attachments:
	 //[{
	  //filename:'individual.txt',
	  //content:'Individual attachement',
	  //contentType:'text/plain'
	//}]
    },
    locals: {
      name: 'Tech Forum'
    }
  })
  .then(console.log)
  .catch(console.error);
	
   return res.send('Email Send');
})

var server = app.listen(8081, function () {
   var host = server.address().address
   var port = server.address().port
   
   console.log("Example app listening at http://%s:%s", host, port)
})