package org.dandelion.radiot.live.chat;

import org.json.JSONObject;
import org.junit.Test;

public class ResponseParserTest {
    @Test
    public void parseJson() throws Exception {
        String strJson = "{}";
        new JSONObject(strJson);
    }
}
