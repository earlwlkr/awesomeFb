var express = require('express'),
    app = express(),
    path = require('path'),
    mongoose = require('mongoose'),
    jade = require('jade'),
    fs = require('fs'),
    template = fs.readFileSync('views/filters.jade', 'utf8');

var jadeFn = jade.compile(template, { filename: 'views/filters.jade', pretty: true });


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
  };

  commentSchema.methods.formatDate = function() {
    var date = this.time_created;
    var month = date.getMonth() + 1;
    var year = date.getYear() + 1900;
    if (month < 10) {
      month = '0' + month;
    }
    var formattedDate = date.getDate() + '/' + month + '/' + year;
    return formattedDate;
  };

  var Comment = mongoose.model('Post', commentSchema);
  
  app.get('/', function(req, res) {
    if (!req.xhr) {
      Comment.getAll(20, function(err, docs) {
        res.render('layout',
          { comments : docs }
        );
      });
    } else {

    }
  });

  app.get('/filters', function(req, res) {
    var filters = {};
    Comment.findOne().sort('time_created').exec(function(err, doc) {
      filters['min_date'] = doc.formatDate();
      Comment.findOne().sort('-time_created').exec(function(err, doc) {
        filters['max_date'] = doc.formatDate();
        Comment.find().distinct('topic').exec(function(err, docs) {
          filters['topics'] = ['Tất cả'].concat(docs);
          Comment.find().distinct('sentiment').exec(function(err, docs) {
            filters['sentiments'] = ['Tất cả'].concat(docs);
            var renderedTemplate = jadeFn({filters: filters});
            res.send(renderedTemplate);
          });
        });
      });
    });
  });

  var server = app.listen(8080, function() {
    var port = server.address().port;
    console.log('App listening at port %s', port);
  });
});

