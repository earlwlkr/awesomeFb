package awesomefb;

import awesomefb.facebook.Comment;
import awesomefb.facebook.Post;
import awesomefb.facebook.User;
import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by earl on 5/25/2015.
 */
public class Database {
    private static Database instance = null;
    private DB database;
    private DBCollection postsCollection;
    private DBCollection usersCollection;

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

        postsCollection.drop();
        usersCollection.drop();
    }

    public void insertComment(Comment comment) {
        String facebookId = comment.getFacebookId();
        postsCollection.update(new BasicDBObject("fb_id", facebookId), comment.toDBObject(), true, false);
    }

    public void insertUser(User user) {
        String facebookId = user.getFacebookId();
        usersCollection.update(new BasicDBObject("fb_id", facebookId), user.toDBObject(), true, false);
    }
}
