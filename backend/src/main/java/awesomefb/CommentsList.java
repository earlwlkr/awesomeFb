package awesomefb;

import com.mongodb.BasicDBObject;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by earl on 5/25/2015.
 */
public class CommentsList {
    private ArrayList<Comment> mCommentsList;

    public CommentsList(JSONArray comments) {
        mCommentsList = new ArrayList<>();
        for (int i = 0; i < comments.length(); i++) {
            mCommentsList.add(new Comment(comments.getJSONObject(i)));
        }
    }

    public BasicDBObject toDBObject() {
        Integer count = mCommentsList.size();
        BasicDBObject doc = new BasicDBObject("count", count);

        for (Integer i = 0; i != count; i++) {
            Comment comment = mCommentsList.get(i);
            doc.append(i.toString(), new BasicDBObject("creator", comment.getCreator().toDBObject())
                    .append("message", comment.getMessage()));
        }
        return doc;
    }
}
