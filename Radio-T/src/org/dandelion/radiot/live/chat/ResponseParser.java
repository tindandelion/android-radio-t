package org.dandelion.radiot.live.chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ResponseParser {

    private final String source;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat TIMESTAMP_FORMAT =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

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
        return new ChatMessage(record.getString("from"), record.getString("msg"),
                formatTime(record.getString("time")));
    }

    private String formatTime(String timestamp) {
        try {
            Date ts = TIMESTAMP_FORMAT.parse(timestamp);
            return TIME_FORMAT.format(ts);
        } catch (ParseException e) {
            return "";
        }

    }

    private JSONArray getRecords() throws JSONException {
        JSONTokener tokener = new JSONTokener(source);
        tokener.skipTo('{');
        JSONObject json = new JSONObject(tokener);
        return json.getJSONArray("records");
    }

}