/**
 *
 */
package awesomefb;

import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author earl
 */
public class Main {

    private static DatabaseManager mDatabaseManager;

    /**
     * @param args
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        // Topics: samsung, iphone, coffee, galaxy
        String rootPage = "yannews";

        // Connect to mongodb.
        mDatabaseManager = DatabaseManager.getInstance();

        JSONArray feed = FacebookManager.getInstance().getPosts(rootPage);
        for (int i = 0; i < feed.length(); i++) {
            JSONObject post = feed.getJSONObject(i);
            String id = post.getString("id");
            System.out.println("[awesomeFb] Processing post " + id);

            Post extractedPost = new Post(post);
            mDatabaseManager.insertPost(extractedPost);
        }
    }
}
