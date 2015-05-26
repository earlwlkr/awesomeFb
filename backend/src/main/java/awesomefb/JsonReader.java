package awesomefb;

import java.io.IOException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        String jsonText = org.apache.commons.io.IOUtils.toString(new URL(url).openStream());
        JSONObject json = new JSONObject(jsonText);
        return json;
    }
}
