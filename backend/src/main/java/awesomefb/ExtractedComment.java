package awesomefb;

import org.springframework.social.facebook.api.Comment;

/**
 * Created by earl on 5/25/2015.
 */
public class ExtractedComment {
    private String mMessage;
    private ExtractedUser mCreator;

    public String getMessage() {
        return mMessage;
    }

    public ExtractedUser getCreator() {
        return mCreator;
    }

    public ExtractedComment(Comment comment) {
        mMessage = comment.getMessage();
        mCreator = new ExtractedUser(comment.getFrom());
    }
}
