package awesomefb.facebook;

import awesomefb.facebook.Facebook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by earl on 5/26/2015.
 */
public class Page extends Entity {
    private String mName;
    private Facebook mFacebook;

    public Page(JSONObject page) {
        super(page);

        mFacebook = Facebook.getInstance();
        mName = page.getString("name");
    }

    public String getName() {
        return mName;
    }

    public List<Page> getLikes() {
        JSONObject obj = mFacebook.request(getFacebookId() + "/likes", null);
        if (!obj.has("data")) {
            return null;
        }
        List<Page> pages = new ArrayList<Page>();
        JSONArray arr = obj.getJSONArray("data");
        for (int i = 0, l = arr.length(); i != l; i++) {
            Page page = new Page(arr.getJSONObject(i));
            pages.add(page);
        }
        return pages;
    }

    public JSONArray getPosts() {
        final int POSTS_LIMIT = 50;
        final int COMMENTS_LIMIT = 50;
        String params = "fields=id,from,message,created_time,comments.limit(" + COMMENTS_LIMIT + ")&limit=" + POSTS_LIMIT;
        JSONObject obj = mFacebook.request(getFacebookId() + "/feed", params);
        if (!obj.has("data")) {
            return null;
        }
        if (obj.has("paging")) {
            JSONObject nextPage = mFacebook.request(obj.getJSONObject("paging").getString("next"));
            if (!nextPage.has("data")) {
                return obj.getJSONArray("data");
            }
            JSONArray nextResults = nextPage.getJSONArray("data");
            for (int i = 0, l = nextResults.length(); i < l; i++) {
                obj.getJSONArray("data").put(nextResults.get(i));
            }
            return obj.getJSONArray("data");
        }
        return obj.getJSONArray("data");
    }
}
