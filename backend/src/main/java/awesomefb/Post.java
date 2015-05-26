package awesomefb;

import com.mongodb.BasicDBObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by earl on 5/25/2015.
 */
public class Post {
    private String mId;
    private String mMessage;
    private User mCreator;
    private Date mCreatedTime;
    private JSONArray mComments;
    private List<Comment> mCommentsList;

    public String getId() {
        return mId;
    }

    public String getMessage() {
        return mMessage;
    }

    public User getCreator() {
        return mCreator;
    }

    public Date getCreatedTime() {
        return mCreatedTime;
    }

    public JSONArray getComments() {
        return mComments;
    }

    public void addComment(Comment comment) {
        mCommentsList.add(comment);
    }

    public Post(JSONObject post) {
        mId = post.getString("id");
        mMessage = post.getString("message");
        mCreator = new User(post.getJSONObject("from"));
        mCreatedTime = parseTime(post.getString("created_time"));
        mCommentsList = new ArrayList<>();
        if (post.has("comments")) {
            JSONArray comments = post.getJSONObject("comments").getJSONArray("data");
            mComments = comments;
        } else {
            mComments = null;
        }
    }

    private Date parseTime(String dateString) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Date time = format.parse(dateString);
            return time;
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public BasicDBObject toDBObject() {
        BasicDBObject doc = new BasicDBObject("fb_id", mId)
                .append("message", mMessage)
                .append("creator", mCreator.toDBObject())
                .append("time", mCreatedTime);

        if (mCommentsList != null) {
            Integer count = mCommentsList.size();
            BasicDBObject commentsDoc = new BasicDBObject("count", count);

            for (Integer i = 0; i != count; i++) {
                Comment comment = mCommentsList.get(i);
                commentsDoc.append(i.toString(), new BasicDBObject("creator", comment.getCreator().toDBObject())
                        .append("message", comment.getMessage()));
            }
            doc.append("comments", commentsDoc);
        }
        return doc;
    }
}
