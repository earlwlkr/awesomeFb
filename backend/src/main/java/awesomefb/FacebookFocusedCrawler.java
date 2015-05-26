package awesomefb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by earl on 5/25/2015.
 */
public class FacebookFocusedCrawler {
    private DatabaseManager mDatabaseManager;

    public FacebookFocusedCrawler() {
        mDatabaseManager = DatabaseManager.getInstance();
    }

    public void run() {
        String rootPage = "yannews";

        // List of pages to crawl
        Queue<String> queue = new LinkedList<>();
        queue.add(rootPage);

        // List of pages crawled
        List<String> crawledPages = new ArrayList<>();

        while (true) {
            // Get page id from queue
            String pageId = queue.remove();
            // Call request and get basic page info (id, name)
            Page page = new Page(pageId);

            // If that page exists and is not processed
            if (page.getId() != null && !crawledPages.contains(pageId)) {
                System.out.println("[awesomeFb] Processing page " + pageId);
                // Get feed as JSON array
                JSONArray feed = page.getPosts(pageId);

                for (int i = 0; i < feed.length(); i++) {
                    // Extract each post's data from feed
                    JSONObject pageObject = feed.getJSONObject(i);
                    // Skip if post does not contain message
                    if (!pageObject.has("message")) continue;
                    Post post = new Post(pageObject);

                    System.out.println("[awesomeFb] Processing post " + post.getId());

                    // Get list of comments as JSON array
                    JSONArray comments = post.getComments();
                    if (comments != null) {
                        for (int j = 0, l = comments.length(); j < l; j++) {
                            // Extract comment data
                            Comment comment = new Comment(comments.getJSONObject(j));
                            User commentCreator = comment.getCreator();
                            // If comment creator is page, add it to queue
                            if (commentCreator.isPage()) {
                                queue.add(commentCreator.getFacebookId());
                            }
                            post.addComment(comment);
                        }
                    }

                    // Save post data to database
                    mDatabaseManager.insertPost(post);
                }

                // Mark page id as processed
                crawledPages.add(pageId);
            }

            if (queue.isEmpty()) {
                break;
            }
        }
    }
}
