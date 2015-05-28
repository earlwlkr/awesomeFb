package awesomefb.facebook;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by earl on 5/25/2015.
 */
public class Post extends Comment {
    private List<Comment> mComments;

    public List<Comment> getComments() {
        return mComments;
    }

    public Post(JSONObject post) {
        super(post);
        mComments = new ArrayList<Comment>();

        if (post.has("comments")) {
            JSONArray commentsArray = post.getJSONObject("comments").getJSONArray("data");
            for (int j = 0, l = commentsArray.length(); j < l; j++) {
                // Extract comment data
                Comment comment = new Comment(commentsArray.getJSONObject(j));
                mComments.add(comment);
            }
        }
    }
}
