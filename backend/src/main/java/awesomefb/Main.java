/**
 * 
 */
package awesomefb;

import java.util.List;

import org.springframework.social.facebook.api.*;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

/**
 * @author earl
 *
 */
public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    String target = "yannews";
    
    String accessToken = "645245115609488|WdyDln6T0OFih3PXwF3dC3Qay9U"; // access token received from Facebook after OAuth authorization
    Facebook facebook = new FacebookTemplate(accessToken);
    
    List<Post> feed = facebook.feedOperations().getFeed(target);
    for (Post post: feed) {
      System.out.println(post.getMessage());
    }
  }
    
}
