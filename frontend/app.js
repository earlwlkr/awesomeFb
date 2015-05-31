var express = require('express'),
    app = express(),
    path = require('path'),
    mongoose = require('mongoose'),
    jade = require('jade'),
    fs = require('fs'),
    filtersTemplate = fs.readFileSync('views/filters.jade', 'utf8'),
    resultsTemplate = fs.readFileSync('views/results.jade', 'utf8');

var jadeFilters = jade.compile(filtersTemplate, { filename: 'views/filters.jade', pretty: true });
var jadeResults = jade.compile(resultsTemplate, { filename: 'views/results.jade', pretty: true });

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
    res.render('layout');
  });

  app.get('/comments', function(req, res) {

    var obj = {};

    if (req.query.topic != null && req.query.topic != 'Tất cả') {
      obj['topic'] = req.query.topic;
    }

    if (req.query.spam != null && req.query.spam != 'Tất cả') {
      obj['is_spam'] = req.query.spam;
    }

    if (req.query.sentiment != null && req.query.sentiment != 'Tất cả') {
      obj['sentiment'] = req.query.sentiment;
    }

    Comment.find(obj).limit(50).exec(function(err, result) {
      console.log(obj);
      var renderedTemplate = jadeResults({comments: result});
      res.send(renderedTemplate);
    });
  });

  app.get('/filters', function(req, res) {
    var filterOptions = {'spam': ['Tất cả', 'true', 'false']};
    Comment.findOne().sort('time_created').exec(function(err, doc) {
      filterOptions['min_date'] = doc.formatDate();
      Comment.findOne().sort('-time_created').exec(function(err, doc) {
        filterOptions['max_date'] = doc.formatDate();
        Comment.find().distinct('topic').exec(function(err, docs) {
          filterOptions['topics'] = ['Tất cả'].concat(docs);
          Comment.find().distinct('sentiment').exec(function(err, docs) {
            filterOptions['sentiments'] = ['Tất cả'].concat(docs);
            var renderedTemplate = jadeFilters({filters: filterOptions});
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

