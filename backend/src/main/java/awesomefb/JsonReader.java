package awesomefb;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        System.out.println("Waiting for HTTP response from " + url);
        String jsonText = org.apache.commons.io.IOUtils.toString(new URL(url).openStream());
        System.out.println("Received HTTP response.");
        JSONObject json = new JSONObject(jsonText);
        return json;
    }
}
