var app = require('express')(),
    mongoose = require('mongoose');

mongoose.connect('mongodb://localhost:27017/awesomefb');
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));

db.once('open', function (callback) {
  
  app.get('/', function(req, res) {
    var collection = db.collection('posts');
    
    var obj = {};
    
    var options = {
      'limit': 50
    };

    collection.find(obj, options).toArray(function(err, docs) {
      console.dir(obj);
      res.json(docs);
    });      
  });

  var server = app.listen(8080, function() {
    var port = server.address().port;
    console.log('App listening at port %s', port);
  });
});
