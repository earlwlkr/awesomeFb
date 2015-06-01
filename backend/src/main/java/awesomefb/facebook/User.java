package awesomefb.facebook;

import org.bson.Document;
import org.json.JSONObject;

/**
 * Created by earl on 5/25/2015.
 */
public class User extends Entity {
    private String mName;
    private boolean mIsPage;

    public User(JSONObject reference) {
        super(reference);
        if (reference.has("name")) {
            mName = reference.getString("name");
        } else {
            mName = "undefined";
        }
        mIsPage = reference.has("category");
    }

    public User(String id, String name, boolean isPage) {
        super(id);
        mName = name;
        mIsPage = isPage;
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
