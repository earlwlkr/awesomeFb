package awesomefb;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by earl on 5/25/2015.
 */
public class DatabaseManager {
    private static DatabaseManager instance = null;
    private DB database;
    private DBCollection postsCollection;
    private DBCollection usersCollection;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            try {
                MongoClient mongoClient = new MongoClient();
                DB db = mongoClient.getDB("awesomefb");

                instance = new DatabaseManager(db);
            } catch (UnknownHostException e) {
                System.out.println(e.toString());
            }
        }
        return instance;
    }

    protected DatabaseManager(DB database) {
        this.database = database;

        postsCollection = this.database.getCollection("posts");
        usersCollection = this.database.getCollection("users");

        postsCollection.drop();
        usersCollection.drop();
    }

    public void insertPost(Post post) {
        String facebookId = post.getId();
        postsCollection.update(new BasicDBObject("fb_id", facebookId), post.toDBObject(), true, false);
    }

    public void insertUser(User user) {
        String facebookId = user.getFacebookId();
        usersCollection.update(new BasicDBObject("fb_id", facebookId), user.toDBObject(), true, false);
    }
}
