package org.dandelion.radiot.live.chat;

public class ChatMessage {
    public final String body;
    public final String sender;

    public ChatMessage(String sender, String body) {
        this.body = body;
        this.sender = sender;
    }
}
