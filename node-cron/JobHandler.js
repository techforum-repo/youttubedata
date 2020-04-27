const cron = require('node-cron');
var task='';
module.exports = (app) => {
	app.get('/api/schedulejob', (req, res, next) => {   
        task = cron.schedule('*/1 * * * *', () =>  {
          console.log('task scheduled');
        }, {
          scheduled: false
        });         
        
        return res.send({
          message: 'Job Scheduled'
        });
    });  


    app.get('/api/startjob', (req, res, next) => {   
              
      task.start();
      return res.send({
        message: 'Job Started'
      });
  });
    
    app.get('/api/stopjob', (req, res, next) => {   
      task.stop();

    return res.send({
        message: 'Job Stopped'
      });
  });   

  app.get('/api/destroyjob', (req, res, next) => {   
    task.destroy();

  return res.send({
      message: 'Job Destroyed'
    });
}); 

app.get('/api/validatecron', (req, res, next) => {   
  var valid = cron.validate('*/1 * * * *');

return res.send({
    message: 'Valid: '+valid
  });
}); 
  
  };