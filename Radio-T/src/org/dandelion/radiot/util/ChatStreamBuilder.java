package org.dandelion.radiot.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatStreamBuilder {

    public static final String DEFAULT_TIMESTAMP = "Sat Dec 15 22:19:27 UTC 2012";

    public static String chatStream(JSONArray messages) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("msgs", messages);
        return result.toString();
    }

    public static String chatStream(JSONObject... messages) throws JSONException {
        JSONArray array = new JSONArray();
        for (JSONObject msg : messages) {
            array.put(msg);
        }
        return chatStream(array);
    }

    public static JSONObject message(String sender, String body, String timestamp, int seq) throws JSONException {
        JSONObject message = new JSONObject();
        message.put("from", sender);
        message.put("msg", body);
        message.put("time", timestamp);
        message.put("seq", seq);
        return message;
    }

    public static JSONObject message(int seq, String body) throws JSONException {
        return message("", body, DEFAULT_TIMESTAMP, seq);
    }
}
