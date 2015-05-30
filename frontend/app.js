var express = require('express'),
    app = express(),
    path = require('path'),
    mongoose = require('mongoose');

app.set('views', __dirname + '/views');
app.set('view engine', 'jade');
app.use(express.static(path.join(__dirname, 'public')));

mongoose.connect('localhost', 'awesomefb');
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));

db.once('open', function (callback) {

  var commentSchema = new mongoose.Schema({ 
    fb_id: String, 
    message: String,
    creator: {
      fb_id: String,
      name: String,
      is_page: Boolean
    },
    time_created: Date,
    topic: String,
    is_spam: Boolean,
    sentiment: String
  });

  commentSchema.statics.getAll = function(limit, cb) {
    return this.find({}, cb).limit(limit);
  }

  var Comment = mongoose.model('Post', commentSchema);
  
  app.get('/', function(req, res) {

    Comment.getAll(20, function(err, docs) {
      res.render('layout',
        { comments : docs }
      );
    });
  });

  var server = app.listen(8080, function() {
    var port = server.address().port;
    console.log('App listening at port %s', port);
  });
});

