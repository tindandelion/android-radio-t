package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HttpResponseParser {
    private final String source;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat TIMESTAMP_FORMAT =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

    public static List<Message> parse(String strJson) throws JSONException {
        HttpResponseParser parser = new HttpResponseParser(strJson);
        return parser.getMessages();
    }

    HttpResponseParser(String strJson) {
        this.source = strJson;
    }

    private List<Message> getMessages() throws JSONException {
        ArrayList<Message> messages = new ArrayList<Message>();
        JSONArray records = getRecords();
        for (int i = 0; i < records.length(); i++) {
            messages.add(newMessage(records.getJSONObject(i)));
        }
        return messages;
    }

    private Message newMessage(JSONObject record) throws JSONException {
        return new Message(
                record.getString("from"),
                record.getString("msg"),
                formatTime(record.getString("time")),
                record.getInt("seq"));
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
        return json.getJSONArray("msgs");
    }

}
