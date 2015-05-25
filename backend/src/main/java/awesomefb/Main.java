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
    MongoClient mongoClient = new MongoClient();
    DB db = mongoClient.getDB("awesomefb");
    // Connect to collection (table in mysql).
    DBCollection posts_collection = db.getCollection("posts");
    DBCollection users_collection = db.getCollection("users");
    posts_collection.drop();
    users_collection.drop();
    
    List<Post> feed = facebook.feedOperations().getPosts(rootPage);
    for (Post post: feed) {
      String id = post.getId();
      System.out.println("[awesomeFb] Processing post " + id);
      
      BasicDBObject doc = new BasicDBObject("message", post.getMessage())
                                            .append("creator", getCreatorInfo(users_collection, post.getFrom()))
                                            .append("time", post.getCreatedTime())
                                            .append("comments", getComments(users_collection, facebook.commentOperations()
                                                    .getComments(id)))
                                            .append("link", post.getLink());
      posts_collection.insert(doc);
    }
  }
    
  /**
   * Saves user data into users collection,
   * returns DBObject with newly inserted id and some basic info (FB ID, name)
   * @param users_collection
   * @param reference
   * @return BasicDBObject
   */
  private static BasicDBObject getCreatorInfo(DBCollection users_collection, Reference reference) {
    String id = reference.getId();
    String name = reference.getName();

    BasicDBObject userObject = new BasicDBObject("id", id).append("name", name);
    System.out.println("[awesomeFb] Inserting basic user data " + id);
    users_collection.insert(userObject);

    // Returns DBObject with id of the object just inserted
    BasicDBObject ret = new BasicDBObject("user_id", userObject.get("_id").toString())
            .append("fb_id", id).append("name", name);
    return ret;
  }
  
  private static BasicDBObject getComments(DBCollection users_collection, List<Comment> comments) {
    Integer count = comments.size();
    BasicDBObject ret = new BasicDBObject("count", count);
    for (Integer i = 0; i != count; i++) {
      ret.append(i.toString(), new BasicDBObject("creator", getCreatorInfo(users_collection,
              comments.get(i).getFrom()))
              .append("message", comments.get(i).getMessage()));
    }
    return ret;
  }
}
