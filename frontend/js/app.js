var MongoClient = require('mongodb').MongoClient, 
    assert = require('assert');

// Connection URL
var url = 'mongodb://localhost:27017/awesomefb';

// Use connect method to connect to the Server
MongoClient.connect(url, function(err, db) {
  assert.equal(null, err);
  console.log("Successfully connected to server!");
  findDocuments(db, null);
});

var findDocuments = function(db, callback) {
  // Get the documents collection
  var collection = db.collection('posts');
  // Find some documents
  collection.find({}).toArray(function(err, docs) {
    console.dir(docs)
    //callback(docs);
  });      
}