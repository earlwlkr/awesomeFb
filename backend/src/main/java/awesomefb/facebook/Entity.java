package awesomefb.facebook;

import org.json.JSONObject;

/**
 * Created by earl on 5/28/2015.
 */
public class Entity {
    private String mFacebookId;

    public final String getFacebookId() {
        return mFacebookId;
    }

    public final void setFacebookId(String mFacebookId) {
        this.mFacebookId = mFacebookId;
    }

    public Entity(JSONObject obj) {
        mFacebookId = obj.getString("id");
    }
}
