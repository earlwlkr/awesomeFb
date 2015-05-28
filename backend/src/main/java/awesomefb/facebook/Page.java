package awesomefb.facebook;

import awesomefb.facebook.Facebook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by earl on 5/26/2015.
 */
public class Page {
    private String mId;
    private String mName;
    private Facebook mFacebook;

    public Page(String idOrName) {
        mFacebook = Facebook.getInstance();
        JSONObject obj = mFacebook.request(idOrName, null);
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

    public List<String> getLikes() {
        if (mId == null) {
            return null;
        }
        JSONObject obj = mFacebook.request(mId + "/likes", null);
        if (!obj.has("data")) {
            return null;
        }
        List<String> pages = new ArrayList<String>();
        JSONArray arr = obj.getJSONArray("data");
        for (int i = 0, l = arr.length(); i != l; i++) {
            JSONObject page = arr.getJSONObject(i);
            pages.add(page.getString("id"));
        }
        return pages;
    }

    public JSONArray getPosts() {
        if (mId == null) {
            return null;
        }
        String params = "limit=1&fields=id,from,message,created_time,comments.limit(1)";
        JSONObject obj = mFacebook.request(mId + "/feed", params);
        if (!obj.has("data")) {
            return null;
        }
        return obj.getJSONArray("data");
    }
}
