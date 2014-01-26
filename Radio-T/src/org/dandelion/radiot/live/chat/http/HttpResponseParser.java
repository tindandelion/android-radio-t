package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.live.chat.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HttpResponseParser {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat TIMESTAMP_FORMAT =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

    public static List<Message> parse(String strJson) throws JSONException {
        JSONArray records = getMessageArray(strJson);

        ArrayList<Message> messages = new ArrayList<Message>();
        for (int i = 0; i < records.length(); i++) {
            messages.add(newMessage(records.getJSONObject(i)));
        }
        return messages;
    }

    private static JSONArray getMessageArray(String strJson) throws JSONException {
        JSONObject json = new JSONObject(strJson);
        return json.getJSONArray("msgs");
    }

    private static Message newMessage(JSONObject record) throws JSONException {
        return new Message(
                record.getString("from"),
                record.getString("msg"),
                formatTime(record.getString("time")),
                record.getInt("seq"));
    }

    private static String formatTime(String timestamp) {
        try {
            Date ts = TIMESTAMP_FORMAT.parse(timestamp);
            return TIME_FORMAT.format(ts);
        } catch (ParseException e) {
            return "";
        }

    }
}
