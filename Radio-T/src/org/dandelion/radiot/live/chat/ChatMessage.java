package org.dandelion.radiot.live.chat;

public class ChatMessage {
    public final String body;
    public final String sender;

    public ChatMessage(String sender, String body) {
        this.body = body;
        this.sender = sender;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChatMessage) {
            ChatMessage other = (ChatMessage) o;
            return body.equals(other.body) &&
                    sender.equals(other.sender);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return body.hashCode() ^ sender.hashCode();
    }

    @Override
    public String toString() {
        return String.format("ChatMessage(sender: \"%s\", body: \"%s\")", sender, body);
    }
}
