package awesomefb.facebook;

import awesomefb.facebook.User;
import org.json.JSONObject;

/**
 * Created by earl on 5/25/2015.
 */
public class Comment {
    private String mMessage;
    private User mCreator;

    public String getMessage() {
        return mMessage;
    }

    public User getCreator() {
        return mCreator;
    }

    public Comment(JSONObject comment) {
        mMessage = comment.getString("message");
        mCreator = new User(comment.getJSONObject("from"));
    }
}
