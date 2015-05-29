package awesomefb;

import awesomefb.facebook.*;
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
    private Database mDatabase;
    private Queue<Page> mQueue;
    private List<String> mProcessedPageIds;

    public FacebookFocusedCrawler() {
        mDatabase = Database.getInstance();
    }

    private Page removeFromQueue() {
        Page page = mQueue.remove();
        mDatabase.removeFromQueue(page);
        return page;
    }

    private void insertQueue(Page page) {
        mQueue.add(page);
        mDatabase.insertQueue(page);
    }

    private void insertProcessed(String id) {
        mProcessedPageIds.add(id);
        mDatabase.insertProcesed(id);
    }

    public void run() {
        final boolean RESET = false;

        //Facebook facebook = Facebook.getInstance();
        //facebook.login();

        if (RESET) {
            mDatabase.drop();

            // List of pages to crawl
            mQueue = new LinkedList<Page>();
            List<Page> resultPages = Facebook.getInstance().searchPages("iphone");
            for (Page resultPage: resultPages) {
                insertQueue(resultPage);
            }

            // List of pages crawled
            mProcessedPageIds = new ArrayList<String>();
        } else {
            // Continue from last checkpoint
            mQueue = mDatabase.getQueue();
            mProcessedPageIds = mDatabase.getProcessed();
        }

        while (true) {
            // Get page from mQueue
            Page page = mQueue.peek();
            String pageId = page.getFacebookId();

            // If that page is not processed
            if (!mProcessedPageIds.contains(pageId)) {
                System.out.println("[awesomeFb] Processing page " + pageId);
                List<Page> pageLikes = page.getLikes();
                for (Page pageLike: pageLikes) {
                    if (!mProcessedPageIds.contains(pageLike.getFacebookId())) {
                        insertQueue(pageLike);
                    }
                }

                // Get feed as JSON array
                JSONArray feed = page.getPosts();
                int feedLength = feed.length();
                System.out.println("[awesomeFb] " + feedLength + " posts returned.");

                if (feed != null) {
                    int count = 0;
                    int commentsCount = 0;
                    for (int i = 0; i < feedLength; i++) {
                        // Extract each post's data from feed
                        JSONObject pageObject = feed.getJSONObject(i);
                        // Skip if post does not contain message
                        if (!pageObject.has("message")) continue;

                        Post post = new Post(pageObject);
                        // Save post data to database
                        mDatabase.insertComment(post);

                        // Get list of comments as JSON array
                        List<Comment> comments = post.getComments();
                        if (comments != null) {
                            for (Comment comment : comments) {
                                commentsCount++;
                                mDatabase.insertComment(comment);
                            }
                        }

                        count++;
                    }
                    System.out.println("[awesomeFb] " + count + " posts processed.");
                    System.out.println("[awesomeFb] " + commentsCount + " comments processed.");
                }

                // Mark page id as processed
                insertProcessed(pageId);
            }

            removeFromQueue();
            if (mQueue.isEmpty()) {
                break;
            }
        }
    }
}
