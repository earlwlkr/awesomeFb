/**
 * 
 */
package awesomefb;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.*;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.facebook.api.*;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

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
    // Topics: samsung, iphone, coffee, galaxy
    String rootPage = "KHTNCFS";
    String accessToken = "CAAJK2NXosZAABAPjvopDAeoFgmBFZB98ixFdop1sFGiBKsZB2ZAZCrPgVein9u6lxxG28fDHELyvE1Unyvmi2XSkaG5wuIflAYTc2vp7DT4xkD6j9lFN1l60la0sVsLbPRysxbOV6kmbAKpLPXbvza4Rb1bW5k9gu0ZBZARbI4ZBcUYwlb5Wat2OhZC4zaqEoXZCDSLAy6CTZBSvXtNujkGRipF";
    
    facebook = new FacebookTemplate(accessToken);
    // Connect to mongodb.
    DatabaseManager dbManager = DatabaseManager.getInstance();
    DBCollection postsCollection = dbManager.getPostsCollection();
    DBCollection usersCollection = dbManager.getUsersCollection();
    
    List<Post> feed = facebook.feedOperations().getPosts(rootPage);
    for (Post post: feed) {
      String id = post.getId();
      System.out.println("[awesomeFb] Processing post " + id);
      
      BasicDBObject doc = new BasicDBObject("message", post.getMessage())
                                            .append("creator", getCreatorInfo(usersCollection, post.getFrom()))
                                            .append("time", post.getCreatedTime())
                                            .append("comments", getComments(usersCollection, facebook.commentOperations()
                                                    .getComments(id)))
                                            .append("link", post.getLink());
      postsCollection.insert(doc);
    }
  }
    
  /**
   * Saves user data into users collection,
   * returns DBObject with newly inserted id and some basic info (FB ID, name)
   * @param usersCollection
   * @param reference
   * @return BasicDBObject
   */
  private static BasicDBObject getCreatorInfo(DBCollection usersCollection, Reference reference) {
    String id = reference.getId();
    String name = reference.getName();

    BasicDBObject userObject = new BasicDBObject("id", id).append("name", name);
    System.out.println("[awesomeFb] Inserting basic user data " + id);
    usersCollection.insert(userObject);

    // Returns DBObject with id of the object just inserted
    BasicDBObject ret = new BasicDBObject("user_id", userObject.get("_id").toString())
            .append("fb_id", id).append("name", name);
    return ret;
  }
  
  private static BasicDBObject getComments(DBCollection usersCollection, List<Comment> comments) {
    Integer count = comments.size();
    BasicDBObject ret = new BasicDBObject("count", count);
    for (Integer i = 0; i != count; i++) {
      ret.append(i.toString(), new BasicDBObject("creator", getCreatorInfo(usersCollection,
              comments.get(i).getFrom()))
              .append("message", comments.get(i).getMessage()));
    }
    return ret;
  }
}
