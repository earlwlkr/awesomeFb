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
    private FacebookManager mFacebookManager;

    public FacebookFocusedCrawler() {
        mDatabaseManager = DatabaseManager.getInstance();
        mFacebookManager = FacebookManager.getInstance();
    }

    public void run() {
        String rootPage = "yannews";

        Queue<String> queue = new LinkedList<>();
        queue.add(rootPage);

        List<String> crawledPages = new ArrayList<>();

        while (true) {
            String pageId = queue.remove();
            if (!crawledPages.contains(pageId)) {
                System.out.println("[awesomeFb] Processing page " + pageId);
                JSONArray feed = mFacebookManager.getPosts(pageId);

                for (int i = 0; i < feed.length(); i++) {
                    JSONObject pageObject = feed.getJSONObject(i);
                    if (!pageObject.has("message")) continue;
                    Post post = new Post(pageObject);
                    String id = post.getId();
                    System.out.println("[awesomeFb] Processing post " + id);

                    JSONArray comments = post.getComments();
                    if (comments != null) {
                        for (int j = 0, l = comments.length(); j < l; j++) {
                            Comment comment = new Comment(comments.getJSONObject(j));
                            User commentCreator = comment.getCreator();
                            if (commentCreator.isPage()) {
                                queue.add(commentCreator.getFacebookId());
                            }
                            post.addComment(comment);
                        }
                    }

                    mDatabaseManager.insertPost(post);
                }

                crawledPages.add(pageId);
            }

            if (queue.isEmpty()) {
                break;
            }
        }
    }
}
