package org.dandelion.radiot.live.chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

class ResponseParser {

    private final String source;

    public static List<ChatMessage> parse(String strJson) {
        try {
            ResponseParser parser = new ResponseParser(strJson);
            return parser.getMessages();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    ResponseParser(String strJson) {
        this.source = strJson;
    }

    private List<ChatMessage> getMessages() throws JSONException {
        ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
        JSONArray records = getRecords();
        for (int i = 0; i < records.length(); i++) {
            messages.add(newMessage(records.getJSONObject(i)));
        }
        return messages;
    }

    private ChatMessage newMessage(JSONObject record) throws JSONException {
        return new ChatMessage(record.getString("from"), record.getString("msg"));
    }

    private JSONArray getRecords() throws JSONException {
        JSONTokener tokener = new JSONTokener(source);
        tokener.skipTo('{');
        JSONObject json = new JSONObject(tokener);
        return json.getJSONArray("records");
    }

}
