package org.dandelion.radiot.live.chat;

public class ChatMessage {
    public final String body;
    public final String sender = "anonymous";

    public ChatMessage(String body) {
        this.body = body;
    }
}
