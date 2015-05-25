package awesomefb;

import com.mongodb.BasicDBObject;
import org.springframework.social.facebook.api.Post;

import java.util.Date;

/**
 * Created by earl on 5/25/2015.
 */
public class ExtractedPost {
    private String mMessage;
    private ExtractedUser mCreator;
    private Date mCreatedTime;
    private ExtractedCommentsList mCommentsList;
    private String mLink;

    public ExtractedPost(Post post) {
        mMessage = post.getMessage();
        mCreator = new ExtractedUser(post.getFrom());
        mCreatedTime = post.getCreatedTime();
        mCommentsList = new ExtractedCommentsList(FacebookManager.getInstance().getComments(post.getId()));
        mLink = post.getLink();
    }

    public BasicDBObject toDBObject() {
        BasicDBObject doc = new BasicDBObject("message", mMessage)
                .append("creator", mCreator.toDBObject())
                .append("time", mCreatedTime)
                .append("comments", mCommentsList.toDBObject())
                .append("link", mLink);
        return doc;
    }
}
