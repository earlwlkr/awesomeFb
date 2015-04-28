/**
 * 
 */
package awesomefb;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.social.UncategorizedApiException;
import org.springframework.social.facebook.api.*;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * @author earl
 *
 */
public class Main {
  
  private static Facebook facebook;
  
  /**
   * @param args
   * @throws UnknownHostException 
   */
  public static void main(String[] args) throws UnknownHostException {
    String rootPage = "yannews";
    String accessToken = "CAAJK2NXosZAABAKCejP1gLqcnf4EGZC4S61I5n2d1ckO4xrCZBivunTzudXXVcX1kc9UqRZAMV8gz7BmFhFZCzDJAUN31K6IxXhTwF2NjgZBg3OgAYbeP7KQ2qZCYpTuURIlZCNENs6m5GxH1CMaen39dZBzaAoBGyeLYTmFu0RbzCKQy7YpQi3CBbz2ZARZC8YlB6iWPTvNo9lrbVJNnf0JdZCC0LoFY80pmEAZD";
    
    facebook = new FacebookTemplate(accessToken);
    // Connect to mongodb.
    MongoClient mongoClient = new MongoClient();
    DB db = mongoClient.getDB("awesomefb");
    // Connect to collection (table in mysql).
    DBCollection collection = db.getCollection("posts");
    collection.drop();
    
    List<Post> feed = facebook.feedOperations().getPosts(rootPage);
    for (Post post: feed) {
      String id = post.getId();
      System.out.println("* Processing post " + id);
      
      BasicDBObject doc = new BasicDBObject("message", post.getMessage())
                                            .append("creator", getSenderDetailedInfo(post.getFrom().getId()))
                                            .append("time", post.getCreatedTime())
                                            .append("comments", getComments(facebook.commentOperations()
                                                                                    .getComments(id)))
                                            .append("link", "https://facebook.com/" + post.getId());
      collection.insert(doc);
    }
  }
    
  /**
   * @param id
   * @return
   */
  private static BasicDBObject getSenderDetailedInfo(String id) {
    BasicDBObject ret = new BasicDBObject("id", id);
    try {
      User user = facebook.userOperations().getUserProfile(id);
      String name = user.getName();
      ret.append("name", name);
      
      String email = user.getEmail();
      if (email != null) {
        ret.append("email", email);
      }
      
      String birthday = user.getBirthday();
      if (birthday != null) {
        ret.append("birthday", birthday);
      }
    } catch (UncategorizedApiException e) { // It's a page, extract page's info.
      Page page = facebook.pageOperations().getPage(id);
      String name = page.getName();
      ret.append("name", name);
      
      List<Reference> cat = page.getCategoryList();
      if (cat != null) {
        ret.append("cat", cat.get(0).getName());
      }
    }
    return ret;
  }
  
  private static BasicDBObject getComments(List<Comment> comments) {
    Integer count = comments.size();
    BasicDBObject ret = new BasicDBObject("count", count);
    for (Integer i = 0; i != count; i++) {
      ret.append(i.toString(), new BasicDBObject("creator", getSenderDetailedInfo(comments.get(i).getFrom().getId()))
                            .append("message", comments.get(i).getMessage()));
    }
    return ret;
  }
}
