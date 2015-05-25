package awesomefb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

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
        postsCollection.insert(post.toDBObject());
    }

    public void insertUser(User user) {
        usersCollection.insert(user.toDBObject());
    }
}
