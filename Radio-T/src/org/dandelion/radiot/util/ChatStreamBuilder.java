package org.dandelion.radiot.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatStreamBuilder {
    public static String chatStream(JSONArray messages) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("msgs", messages);
        return result.toString();
    }

    public static JSONArray withMessages(String... msgs) throws JSONException {
        JSONArray array = new JSONArray();
        for (String msg : msgs) {
            array.put(chatMessage("", msg, "Sat Dec 15 22:19:27 UTC 2012"));
        }
        return array;
    }

    public static JSONArray withMessages(JSONObject... messages) {
        JSONArray array = new JSONArray();
        for (JSONObject msg : messages) {
            array.put(msg);
        }
        return array;
    }

    public static JSONObject chatMessage(String sender, String body, String timestamp) throws JSONException {
        JSONObject message = new JSONObject();
        message.put("from", sender);
        message.put("msg", body);
        message.put("time", timestamp);
        return message;
    }
}
