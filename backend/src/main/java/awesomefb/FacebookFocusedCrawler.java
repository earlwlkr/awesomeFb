package awesomefb;

import org.json.JSONArray;

/**
 * Created by earl on 5/25/2015.
 */
public class FacebookFocusedCrawler {
    private DatabaseManager mDatabaseManager;
    private FacebookManager mFacebookManager;

    public FacebookFocusedCrawler() {
        mDatabaseManager = DatabaseManager.getInstance();
        mFacebookManager = FacebookManager.getInstance();
    }

    public void run() {
        String rootPage = "yannews";
        JSONArray feed = mFacebookManager.getPosts(rootPage);

        for (int i = 0; i < feed.length(); i++) {
            Post post = new Post(feed.getJSONObject(i));
            String id = post.getId();
            System.out.println("[awesomeFb] Processing post " + id);

            mDatabaseManager.insertPost(post);
        }
    }
}
