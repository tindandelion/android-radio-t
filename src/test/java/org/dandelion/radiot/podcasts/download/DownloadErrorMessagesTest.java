package org.dandelion.radiot.podcasts.download;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DownloadErrorMessagesTest {
    public static final String MESSAGE = "Error message";
    public static final String[] ALL_MESSAGES = new String[]{MESSAGE};
    public static final int STARTING_CODE = 1000;
    public static final String UNKNOWN_ERROR_MESSAGE = "Unknown error: %d";
    private DownloadErrorMessages messages;

    @Before
    public void setUp() throws Exception {
        messages = new DownloadErrorMessages(ALL_MESSAGES, STARTING_CODE, UNKNOWN_ERROR_MESSAGE);
    }

    @Test
    public void getMessageForCode() {
        assertEquals(MESSAGE, messages.getMessageForCode(STARTING_CODE));
    }

    @Test
    public void errorCodeBiggerThanMessageArray() throws Exception {
        int code = 1001;
        assertEquals(String.format(UNKNOWN_ERROR_MESSAGE, code),
                messages.getMessageForCode(code));
    }

    @Test
    public void errorCodeLessThanStartingCode() throws Exception {
        int code = 900;
        assertEquals(String.format(UNKNOWN_ERROR_MESSAGE, code),
                messages.getMessageForCode(code));
    }
}
