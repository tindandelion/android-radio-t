package org.dandelion.radiot.podcasts.download;

public class DownloadErrorMessages {
    private String[] messages;
    private int startingCode;
    private String defaultMessage;

    public DownloadErrorMessages(String[] messages, int startingCode, String defaultMessage) {
        this.messages = messages;
        this.startingCode = startingCode;
        this.defaultMessage = defaultMessage;
    }

    public String getMessageForCode(int code) {
        int index = code - startingCode;
        if (index < 0 || index >= messages.length) {
            return defaultMessageForCode(code);
        }
        return messages[index];
    }

    private String defaultMessageForCode(int code) {
        return String.format(defaultMessage, code);
    }
}
