package awesomefb;

import com.mongodb.BasicDBObject;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by earl on 5/25/2015.
 */
public class Post {
    private String mMessage;
    private User mCreator;
    private Date mCreatedTime;
    private CommentsList mCommentsList;

    public Post(JSONObject post) {
        mMessage = post.getString("message");
        mCreator = new User(post.getJSONObject("from"));
        mCreatedTime = parseTime(post.getString("created_time"));
        mCommentsList = new CommentsList(post.getJSONObject("comments").getJSONArray("data"));
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
        BasicDBObject doc = new BasicDBObject("message", mMessage)
                .append("creator", mCreator.toDBObject())
                .append("time", mCreatedTime)
                .append("comments", mCommentsList.toDBObject());
        return doc;
    }
}
