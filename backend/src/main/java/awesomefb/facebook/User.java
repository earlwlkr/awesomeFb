package awesomefb.facebook;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.json.JSONObject;

/**
 * Created by earl on 5/25/2015.
 */
public class User extends Entity {
    private String mName;
    private boolean mIsPage;

    /**
     * Saves user data into users collection,
     * returns DBObject with newly inserted id and some basic info (FB ID, name)
     * @param reference
     */
    public User(JSONObject reference) {
        super(reference);
        mName = reference.getString("name");
        mIsPage = reference.has("category");
    }

    public String getName() {
        return mName;
    }

    public boolean isPage() {
        return mIsPage;
    }

    public Document toDocument() {
        return new Document("fb_id", getFacebookId())
                .append("name", mName)
                .append("is_page", mIsPage)
                ;
    }
}
