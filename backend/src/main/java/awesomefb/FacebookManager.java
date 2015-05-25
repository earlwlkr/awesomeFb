package awesomefb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by earl on 5/25/2015.
 */
public class FacebookManager {
    private static FacebookManager instance = null;
    private final String
            mAccessToken = "CAAJK2NXosZAABAPjvopDAeoFgmBFZB98ixFdop1sFGiBKsZB2ZAZCrPgVein9u6lxxG28fDHELyvE1Unyvmi2XSkaG5wuIflAYTc2vp7DT4xkD6j9lFN1l60la0sVsLbPRysxbOV6kmbAKpLPXbvza4Rb1bW5k9gu0ZBZARbI4ZBcUYwlb5Wat2OhZC4zaqEoXZCDSLAy6CTZBSvXtNujkGRipF";

    public static FacebookManager getInstance() {
        if (instance == null) {
            instance = new FacebookManager();
        }
        return instance;
    }

    protected FacebookManager() {}

    public JSONArray getPosts(String pageName) {
        JSONObject obj = new JSONObject();
        try {
            obj = JsonReader.readJsonFromUrl("https://graph.facebook.com/" + pageName
                    + "/feed?fields=id,from,message,created_time,comments&access_token=" + mAccessToken);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return obj.getJSONArray("data");
    }
}
