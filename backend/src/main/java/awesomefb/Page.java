package awesomefb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by earl on 5/26/2015.
 */
public class Page {
    private String mId;
    private String mName;
    private FacebookManager mFacebook;

    public Page(String idOrName) {
        mFacebook = FacebookManager.getInstance();
        JSONObject obj = mFacebook.request(idOrName, (Object)null);
        if (obj != null) {
            mId = obj.getString("id");
            mName = obj.getString("name");
        }
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public JSONArray getPosts(String pageName) {
        if (mId == null) {
            return null;
        }
        String params = "limit=100&fields=id,from,message,created_time,comments.limit(50)";
        JSONObject obj = mFacebook.request(mId + "/feed", params);
        if (!obj.has("data")) {
            return null;
        }
        return obj.getJSONArray("data");
    }
}
