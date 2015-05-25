package awesomefb;

import com.mongodb.BasicDBObject;
import org.springframework.social.facebook.api.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by earl on 5/25/2015.
 */
public class ExtractedCommentsList {
    private ArrayList<ExtractedComment> mCommentsList;

    public ExtractedCommentsList(List<Comment> comments) {
        mCommentsList = new ArrayList<ExtractedComment>();
        for (Comment comment: comments) {
            mCommentsList.add(new ExtractedComment(comment));
        }
    }

    public BasicDBObject toDBObject() {
        Integer count = mCommentsList.size();
        BasicDBObject doc = new BasicDBObject("count", count);

        for (Integer i = 0; i != count; i++) {
            ExtractedComment comment = mCommentsList.get(i);
            doc.append(i.toString(), new BasicDBObject("creator", comment.getCreator().toDBObject())
                    .append("message", comment.getMessage()));
        }
        return doc;
    }
}
