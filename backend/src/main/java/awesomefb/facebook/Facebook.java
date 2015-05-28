package awesomefb.facebook;

import awesomefb.utils.JsonReader;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by earl on 5/25/2015.
 */
public class Facebook {
    private static Facebook instance = null;
    private final String WEB_URL = "https://www.facebook.com/";
    private final String API_ENDPOINT = "https://graph.facebook.com/v2.3/";
    private final String
            ACCESS_TOKEN = "CAAJK2NXosZAABAPjvopDAeoFgmBFZB98ixFdop1sFGiBKsZB2ZAZCrPgVein9u6lxxG28fDHELyvE1Unyvmi2XSkaG5wuIflAYTc2vp7DT4xkD6j9lFN1l60la0sVsLbPRysxbOV6kmbAKpLPXbvza4Rb1bW5k9gu0ZBZARbI4ZBcUYwlb5Wat2OhZC4zaqEoXZCDSLAy6CTZBSvXtNujkGRipF";

    private List<String> cookies;
    private HttpsURLConnection conn;
    private final String USER_AGENT = "Mozilla/5.0";

    public static Facebook getInstance() {
        if (instance == null) {
            instance = new Facebook();
        }
        return instance;
    }

    protected Facebook() {}

    public JSONObject request(String node, Object params) {
        String url = API_ENDPOINT + node + "?";
        if (params != null) {
            url += params + "&";
        }
        url += "access_token=" + ACCESS_TOKEN;

        try {
            JSONObject obj = JsonReader.readJsonFromUrl(url);
            return obj;
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public JSONObject request(String url) {
        try {
            JSONObject obj = JsonReader.readJsonFromUrl(url);
            return obj;
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public List<Page> searchPages(String q) {
        String params = "type=page&limit=100&q=" + q;
        JSONObject obj = request("search", params);
        JSONArray results = obj.getJSONArray("data");
        List<Page> pages = new ArrayList<Page>();
        for (int i = 0, l = results.length(); i < l; i++) {
            Page page = new Page(results.getJSONObject(i));
            pages.add(page);
        }
        return pages;
    }

    public void login() {
        String loginUrl = "https://www.facebook.com/login.php?login_attempt=1";

        // make sure cookies is turn on
        CookieHandler.setDefault(new CookieManager());

        // 1. Send a "GET" request, so that you can extract the form's data.
        try {
            String page = getPageContent(WEB_URL);
            String postParams = getFormParams(page, "awesomefb2015@gmail.com", "awesome2015");

            // 2. Construct above post's content and then send a POST request for
            // authentication
            sendPost(loginUrl, postParams);

            page = getPageContent(WEB_URL);
            System.out.println(page);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public User updateUserDetails(User user) {
        try {
            String page = getPageContent("https://www.facebook.com/" + user.getFacebookId());
            String pattern = "class=\"_6-6\" href=\"(.*?)\" data-tab-key=\"about\"";

            // Create a Pattern object
            Pattern r = Pattern.compile(pattern);
            // Now create matcher object.
            Matcher m = r.matcher(page);
            System.out.println(page);

            if (m.find()) {
                String aboutUrl = m.group(1) + "?section=contact-info";
                System.out.println(aboutUrl);
                page = getPageContent(aboutUrl);
                String pattern2 = "<span class=\"_50f4\">(\\w+)<";
                Pattern r2 = Pattern.compile(pattern2);
                Matcher m2 = r2.matcher(page);
                System.out.println(aboutUrl);
                if (m2.find()) {
                    String gender = m2.group(1);
                    System.out.println(user.getFacebookId() + " " + gender);
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return user;
    }

    private void sendPost(String url, String postParams) throws IOException {

        URL obj = new URL(url);
        conn = (HttpsURLConnection) obj.openConnection();

        // Acts like a browser
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Host", "www.facebook.com");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        for (String cookie : this.cookies) {
            conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
        }
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

        conn.setDoOutput(true);
        conn.setDoInput(true);

        // Send post request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
    }

    private String getPageContent(String url) throws IOException {

        URL obj = new URL(url);
        conn = (HttpsURLConnection) obj.openConnection();

        // default is GET
        conn.setRequestMethod("GET");

        conn.setUseCaches(false);

        // act like a browser
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5,vi;q=0.6");
        if (cookies != null) {
            for (String cookie : this.cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
        System.out.println("\nSending 'GET' request to URL : " + url);

        String response = IOUtils.toString(conn.getInputStream());

        // Get the response cookies
        setCookies(conn.getHeaderFields().get("Set-Cookie"));

        return response.toString();
    }

    private String getFormParams(String html, String username, String password)
            throws UnsupportedEncodingException {

        System.out.println("Extracting form's data...");

        Document doc = Jsoup.parse(html);

        // Google form id
        Element loginform = doc.getElementById("login_form");
        Elements inputElements = loginform.getElementsByTag("input");
        List<String> paramList = new ArrayList<String>();
        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");

            if (key.equals("email"))
                value = username;
            else if (key.equals("pass"))
                value = password;
            if (key != null && value != null) {
                paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
            }
        }

        // build parameters list
        StringBuilder result = new StringBuilder();
        for (String param : paramList) {
            if (result.length() == 0) {
                result.append(param);
            } else {
                result.append("&" + param);
            }
        }
        return result.toString();
    }

    public List<String> getCookies() {
        return cookies;
    }

    public void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }
}
