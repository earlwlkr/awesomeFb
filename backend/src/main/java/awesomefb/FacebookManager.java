package awesomefb;

import org.springframework.social.facebook.api.Comment;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import java.util.List;

/**
 * Created by earl on 5/25/2015.
 */
public class FacebookManager {
    private static FacebookManager instance = null;
    private FacebookTemplate mFacebook;

    public static FacebookManager getInstance() {
        if (instance == null) {
            instance = new FacebookManager();
        }
        return instance;
    }

    protected FacebookManager() {
        String accessToken = "CAAJK2NXosZAABAPjvopDAeoFgmBFZB98ixFdop1sFGiBKsZB2ZAZCrPgVein9u6lxxG28fDHELyvE1Unyvmi2XSkaG5wuIflAYTc2vp7DT4xkD6j9lFN1l60la0sVsLbPRysxbOV6kmbAKpLPXbvza4Rb1bW5k9gu0ZBZARbI4ZBcUYwlb5Wat2OhZC4zaqEoXZCDSLAy6CTZBSvXtNujkGRipF";
        mFacebook = new FacebookTemplate(accessToken);
    }

    public List<Comment> getComments(String postId) {
        return mFacebook.commentOperations().getComments(postId);
    }
    public List<Post> getPosts(String pageName) {
        return mFacebook.feedOperations().getPosts(pageName);
    }
}
