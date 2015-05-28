package awesomefb.facebook;

import awesomefb.facebook.User;
import com.mongodb.BasicDBObject;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by earl on 5/25/2015.
 */
public class Comment extends Entity {
    private String mMessage;
    private User mCreator;
    private Date mCreatedTime;

    public String getMessage() {
        return mMessage;
    }

    public User getCreator() {
        return mCreator;
    }

    public Comment(JSONObject comment) {
        super(comment);
        mMessage = comment.getString("message");
        mCreator = new User(comment.getJSONObject("from"));
        mCreatedTime = parseTime(comment.getString("created_time"));
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
        BasicDBObject doc = new BasicDBObject("fb_id", getFacebookId())
                .append("message", mMessage)
                .append("creator", mCreator.toDBObject())
                .append("time_created", mCreatedTime);

        return doc;
    }

}
