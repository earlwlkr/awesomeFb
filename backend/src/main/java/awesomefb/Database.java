package awesomefb;

import awesomefb.facebook.Comment;
import awesomefb.facebook.Page;
import awesomefb.facebook.User;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by earl on 5/25/2015.
 */
public class Database {
    private static Database instance = null;
    private MongoDatabase database;
    private MongoCollection<Document> postsCollection;
    private MongoCollection<Document> usersCollection;
    private MongoCollection<Document> processedCollection;
    private MongoCollection<Document> queueCollection;

    public static Database getInstance() {
        if (instance == null) {
            MongoClient mongoClient = new MongoClient();
            MongoDatabase db = mongoClient.getDatabase("awesomefb");

            instance = new Database(db);
        }
        return instance;
    }

    protected Database(MongoDatabase database) {
        this.database = database;

        postsCollection = this.database.getCollection("posts");
        usersCollection = this.database.getCollection("users");
        processedCollection = this.database.getCollection("processed");
        queueCollection = this.database.getCollection("queue");
    }

    public void drop() {
        database.drop();
    }

    public void insertProcesed(String id) {
        processedCollection.insertOne(new Document("fb_id", id));
    }

    public void insertQueue(Page page) {
        queueCollection.insertOne(new Document("fb_id", page.getFacebookId())
                .append("name", page.getName()));
    }

    public List<String> getProcessed() {
        List<String> processedIds = new ArrayList<String>();
        MongoCursor<Document> cursor = processedCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                processedIds.add(cursor.next().get("fb_id").toString());
            }
        } finally {
            cursor.close();
        }
        return processedIds;
    }

    public Queue<Page> getQueue() {
        Queue<Page> queue = new LinkedList<>();
        MongoCursor<Document> cursor = queueCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document obj = cursor.next();
                queue.add(new Page(obj.get("fb_id").toString(), obj.get("name").toString()));
            }
        } finally {
            cursor.close();
        }
        return queue;
    }

    public void removeFromQueue(Page page) {
        Document pageObject = new Document("fb_id", page.getFacebookId())
                .append("name", page.getName());
        queueCollection.deleteOne(pageObject);
    }

    public void insertComment(Comment comment) {
        String facebookId = comment.getFacebookId();
        postsCollection.updateOne(new Document("fb_id", facebookId), comment.toDocument(), new UpdateOptions().upsert(true));
        User postCreator = comment.getCreator();
        insertUser(postCreator);
    }

    public void insertUser(User user) {
        String facebookId = user.getFacebookId();
        usersCollection.updateOne(new Document("fb_id", facebookId), user.toDocument(), new UpdateOptions().upsert(true));
    }
}
