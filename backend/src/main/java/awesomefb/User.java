package awesomefb;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.json.JSONObject;

/**
 * Created by earl on 5/25/2015.
 */
public class User {
    private String mUserId;
    private String mFacebookId;
    private String mName;

    /**
     * Saves user data into users collection,
     * returns DBObject with newly inserted id and some basic info (FB ID, name)
     * @param reference
     */
    public User(JSONObject reference) {
        mFacebookId = reference.getString("id");
        mName = reference.getString("name");
        DatabaseManager databaseManager = DatabaseManager.getInstance();

        BasicDBObject userObject = new BasicDBObject("fb_id", mFacebookId).append("name", mName);
        mUserId = new ObjectId().toString();
        userObject.put("_id", mUserId);

        System.out.println("[awesomeFb] Inserting basic user data " + mFacebookId);
        databaseManager.insertUser(this);
    }

    public String getUserId() {
        return mUserId;
    }

    public String getFacebookId() {
        return mFacebookId;
    }

    public String getName() {
        return mName;
    }

    public BasicDBObject toDBObject() {
        return new BasicDBObject("user_id", mUserId)
                .append("fb_id", mFacebookId).append("name", mName);
    }
}
