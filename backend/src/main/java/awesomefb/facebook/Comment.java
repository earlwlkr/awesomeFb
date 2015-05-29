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
    private String mTopic;
    private boolean mIsSpam;
    private ExternalLink mLink;

    private class ExternalLink {
        private String mName;
        private String mUrl;
        private String mCaption;
        private String mDescription;

        public ExternalLink(JSONObject obj) {
            mName = obj.getString("name");
            mUrl = obj.getString("link");
            mCaption = obj.getString("caption");
            mDescription = obj.getString("description");
        }

        public BasicDBObject toDBObject() {
            BasicDBObject doc = new BasicDBObject("name", mName)
                    .append("link", mUrl)
                    .append("caption", mCaption)
                    .append("description", mDescription)
                    ;

            return doc;
        }
    }

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

        if (comment.has("link")) {
            mLink = new ExternalLink(comment);
        }

        mTopic = "";
        mIsSpam = false;
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
                .append("time_created", mCreatedTime)
                .append("topic", mTopic)
                .append("is_spam", mIsSpam)
                ;
        if (mLink != null) {
            doc.append("link", mLink.toDBObject());
        }
        return doc;
    }

}
