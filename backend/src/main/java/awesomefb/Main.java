/**
 *
 */
package awesomefb;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.social.facebook.api.Post;

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
        String rootPage = "KHTNCFS";

        // Connect to mongodb.
        mDatabaseManager = DatabaseManager.getInstance();

        List<Post> feed = FacebookManager.getInstance().getPosts(rootPage);
        for (Post post : feed) {
            String id = post.getId();
            System.out.println("[awesomeFb] Processing post " + id);

            ExtractedPost extractedPost = new ExtractedPost(post);
            mDatabaseManager.insertPost(extractedPost);
        }
    }
}
