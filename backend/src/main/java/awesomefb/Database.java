package awesomefb;

import awesomefb.facebook.Comment;
import awesomefb.facebook.Page;
import awesomefb.facebook.Post;
import awesomefb.facebook.User;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by earl on 5/25/2015.
 */
public class Database {
    private static Database instance = null;
    private DB database;
    private DBCollection postsCollection;
    private DBCollection usersCollection;
    private DBCollection processedCollection;
    private DBCollection queueCollection;

    public static Database getInstance() {
        if (instance == null) {
            try {
                MongoClient mongoClient = new MongoClient();
                DB db = mongoClient.getDB("awesomefb");

                instance = new Database(db);
            } catch (UnknownHostException e) {
                System.out.println(e.toString());
            }
        }
        return instance;
    }

    protected Database(DB database) {
        this.database = database;

        postsCollection = this.database.getCollection("posts");
        usersCollection = this.database.getCollection("users");
        processedCollection = this.database.getCollection("processed");
        queueCollection = this.database.getCollection("queue");
    }

    public void drop() {
        database.dropDatabase();
    }

    public void insertProcesed(String id) {
        processedCollection.insert(new BasicDBObject("fb_id", id));
    }

    public void insertQueue(Page page) {
        queueCollection.insert(new BasicDBObject("fb_id", page.getFacebookId())
                .append("name", page.getName()));
    }

    public List<String> getProcessed() {
        List<String> processedIds = new ArrayList<String>();
        DBCursor cursor = processedCollection.find();
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
        DBCursor cursor = queueCollection.find();
        try {
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                queue.add(new Page(obj.get("fb_id").toString(), obj.get("name").toString()));
            }
        } finally {
            cursor.close();
        }
        return queue;
    }

    public void removeFromQueue(Page page) {
        BasicDBObject pageObject = new BasicDBObject("fb_id", page.getFacebookId())
                .append("name", page.getName());
        queueCollection.remove(pageObject);
    }

    public void insertComment(Comment comment) {
        String facebookId = comment.getFacebookId();
        postsCollection.update(new BasicDBObject("fb_id", facebookId), comment.toDBObject(), true, false);
        User postCreator = comment.getCreator();
        insertUser(postCreator);
    }

    public void insertUser(User user) {
        String facebookId = user.getFacebookId();
        usersCollection.update(new BasicDBObject("fb_id", facebookId), user.toDBObject(), true, false);
    }
}
