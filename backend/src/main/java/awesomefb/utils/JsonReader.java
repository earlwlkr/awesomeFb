package awesomefb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

    public static JSONObject readJsonFromUrl(String urlPath) throws IOException, JSONException {
        System.out.println("Waiting for HTTP response from " + urlPath);
        URL url = new URL(urlPath);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(30000);
        con.setReadTimeout(10000);
        InputStream in = con.getInputStream();
        String jsonText = org.apache.commons.io.IOUtils.toString(in);
        System.out.println("Received HTTP response.");
        JSONObject json = new JSONObject(jsonText);
        return json;
    }
}
