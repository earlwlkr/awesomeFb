package awesomefb.facebook;

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

    public Page(String id, String name) {
        super(id);

        mFacebook = Facebook.getInstance();
        mName = name;
    }

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

        JSONArray postsJson = obj.getJSONArray("data");
        while (true) {
            if (!obj.has("paging")) {
                break;
            }
            obj = mFacebook.request(obj.getJSONObject("paging").getString("next"));
            if (!obj.has("data")) {
                break;
            }
            JSONArray nextResults = obj.getJSONArray("data");
            for (int i = 0, l = nextResults.length(); i < l; i++) {
                postsJson.put(nextResults.get(i));
            }
        }
        return postsJson;
    }
}
