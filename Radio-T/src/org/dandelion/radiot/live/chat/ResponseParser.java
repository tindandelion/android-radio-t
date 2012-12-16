package org.dandelion.radiot.live.chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

class ResponseParser {

    private final String source;

    public static List<String> parse(String strJson) {
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

    private List<String> getMessages() throws JSONException {
        List<String> messages = new ArrayList<String>();
        JSONArray records = getRecords();
        for (int i = 0; i < records.length(); i++) {
            messages.add(getMessageText(records, i));
        }
        return messages;
    }

    private JSONArray getRecords() throws JSONException {
        JSONTokener tokener = new JSONTokener(source);
        tokener.skipTo('{');
        JSONObject json = new JSONObject(tokener);
        return json.getJSONArray("records");
    }

    private String getMessageText(JSONArray records, int index) throws JSONException {
        JSONObject record = records.getJSONObject(index);
        return record.getString("msg");
    }

}
