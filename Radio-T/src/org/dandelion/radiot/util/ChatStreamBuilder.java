package org.dandelion.radiot.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatStreamBuilder {
    public static String chatStream(JSONArray messages) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("records", messages);
        return wrapIntoCallback(result);
    }

    private static String wrapIntoCallback(JSONObject object) {
        return String.format("callback_fn(%s);", object);
    }

    public static JSONArray withMessages(String... msgs) throws JSONException {
        JSONArray array = new JSONArray();
        for (String msg : msgs) {
            array.put(chatMessage("", msg));
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

    public static JSONObject chatMessage(String sender, String body) throws JSONException {
        JSONObject message = new JSONObject();
        message.put("from", sender);
        message.put("msg", body);
        return message;
    }
}
