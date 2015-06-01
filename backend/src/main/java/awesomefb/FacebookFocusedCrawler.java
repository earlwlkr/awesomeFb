package awesomefb;

import awesomefb.facebook.*;
import awesomefb.spamfilter.SpamFilter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by earl on 5/25/2015.
 */
public class FacebookFocusedCrawler {
    private Database mDatabase;
    // List of pages to crawl
    private Queue<Page> mQueue;
    // List of pages crawled
    private List<String> mProcessedPageIds;

    private SentimentClassifier mClassifier;
    private SpamFilter mSpamFilter;
    
    public FacebookFocusedCrawler() {
        mDatabase = Database.getInstance();

        mClassifier = new SentimentClassifier();
        
        mSpamFilter = SpamFilter.getInstance();
        
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

    public void run(String topic) {
        //labelSpam();
        //updateUsers();

        mClassifier.train();
        crawl(topic);
    }

    public void labelSentiment() {
        int count = 0;
        List<Comment> comments = mDatabase.getComments();
        SentimentClassifier classifier = new SentimentClassifier();
        classifier.train();
        for (Comment comment: comments) {
            String sentiment = classifier.classify(comment.getMessage());
            comment.setSentiment(sentiment);
            mDatabase.insertComment(comment);
            System.out.println(count++);
        }
    }

    public void labelSpam() {
        int count = 0;
        List<Comment> comments = mDatabase.getComments();
        for (Comment comment: comments) {
            boolean isSpam = mSpamFilter.isSpam(comment.getMessage());
            comment.setIsSpam(isSpam);
            mDatabase.insertComment(comment);
            System.out.println(count++);
        }
    }

    public void updateUsers() {
        int count = 0;
        Facebook handler = Facebook.getInstance();
        handler.login();
        List<Comment> comments = mDatabase.getComments();
        for (Comment comment: comments) {
            User creator = comment.getCreator();
            if (!creator.isPage()) {
                creator = handler.updateUserDetails(creator);
                mDatabase.insertUser(creator);
                System.out.println(count++);
            }
        }
    }

    public void crawl(String topic) {
        System.out.println("Crawling for topic " + topic);
        final boolean RESET = false;

        //Facebook facebook = Facebook.getInstance();
        //facebook.login();

        if (RESET || mDatabase.getQueue().isEmpty()) {
            //mDatabase.drop();

            mQueue = new LinkedList<Page>();
            List<Page> resultPages = Facebook.getInstance().searchPages(topic);
            for (Page resultPage: resultPages) {
                insertQueue(resultPage);
            }

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

                if (feed != null) {
                    int feedLength = feed.length();
                    System.out.println("[awesomeFb] " + feedLength + " posts returned.");
                    System.out.println("Processing returned JSON.");

                    int count = 0;
                    int commentsCount = 0;
                    for (int i = 0; i < feedLength; i++) {
                        // Extract each post's data from feed
                        JSONObject pageObject = feed.getJSONObject(i);
                        // Skip if post does not contain message
                        if (!pageObject.has("message")) continue;

                        Post post = new Post(pageObject);
                        post.setTopic(topic);
                        String sentiment = mClassifier.classify(post.getMessage());
                        post.setSentiment(sentiment);
                        // Save post data to database
                        mDatabase.insertComment(post);

                        // Get list of comments as JSON array
                        List<Comment> comments = post.getComments();
                        if (comments != null) {
                            for (Comment comment : comments) {
                                comment.setTopic(topic);
                                sentiment = mClassifier.classify(comment.getMessage());
                                comment.setSentiment(sentiment);
                                boolean isSpam = mSpamFilter.isSpam(comment.getMessage());
                                comment.setIsSpam(isSpam);
                                mDatabase.insertComment(comment);
                                commentsCount++;
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
