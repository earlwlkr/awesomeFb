package awesomefb;

import awesomefb.facebook.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Created by earl on 5/25/2015.
 */
public class FacebookFocusedCrawler {
    private Database mDatabase;

    public FacebookFocusedCrawler() {
        mDatabase = Database.getInstance();
    }

    public void run() {
        final boolean RESET = false;

        //Facebook facebook = Facebook.getInstance();
        //facebook.login();
        Queue<Page> queue;
        List<String> crawledPages;
        if (RESET) {
            mDatabase.drop();

            // List of pages to crawl
            queue = new LinkedList<Page>();
            List<Page> resultPages = Facebook.getInstance().searchPages("iphone");
            for (Page resultPage: resultPages) {
                queue.add(resultPage);
                mDatabase.insertQueue(resultPage);
            }

            // List of pages crawled
            crawledPages = new ArrayList<String>();
        } else {
            // Continue from last checkpoint
            queue = mDatabase.getQueue();
            crawledPages = mDatabase.getProcessed();
        }

        while (true) {
            // Get page from queue
            Page page = queue.remove();
            mDatabase.removeFromQueue(page);
            String pageId = page.getFacebookId();

            // If that page is not processed
            if (!crawledPages.contains(pageId)) {
                System.out.println("[awesomeFb] Processing page " + pageId);
                List<Page> pageLikes = page.getLikes();
                for (Page pageLike: pageLikes) {
                    if (!crawledPages.contains(pageLike.getFacebookId())) {
                        queue.add(pageLike);
                        mDatabase.insertQueue(pageLike);
                    }
                }

                // Get feed as JSON array
                JSONArray feed = page.getPosts();

                if (feed != null) {
                    int count = 0;
                    for (int i = 0; i < feed.length(); i++) {
                        // Extract each post's data from feed
                        JSONObject pageObject = feed.getJSONObject(i);
                        // Skip if post does not contain message
                        if (!pageObject.has("message")) continue;

                        Post post = new Post(pageObject);
                        // Save post data to database
                        mDatabase.insertComment(post);

                        User postCreator = post.getCreator();
                        mDatabase.insertUser(postCreator);

                        // Get list of comments as JSON array
                        List<Comment> comments = post.getComments();
                        if (comments != null) {
                            for (Comment comment : comments) {
                                User commentCreator = comment.getCreator();
                                // If comment creator is page, add it to queue
                                if (!commentCreator.isPage()) {
                                    //commentCreator = facebook.updateUserDetails(commentCreator);
                                }
                                mDatabase.insertComment(comment);
                                mDatabase.insertUser(commentCreator);
                            }
                        }

                        count++;
                    }
                    System.out.println("[awesomeFb] " + count + " posts processed.");
                }

                // Mark page id as processed
                crawledPages.add(pageId);
                mDatabase.insertProcesed(pageId);
            }

            if (queue.isEmpty()) {
                break;
            }
        }
    }
}
