package org.dandelion.radiot.live.chat;

public class Message {
    public final String body;
    public final String sender;
    public final String time;

    public Message(String sender, String body, String time) {
        this.body = body;
        this.sender = sender;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Message) {
            Message other = (Message) o;
            return body.equals(other.body) &&
                    sender.equals(other.sender) &&
                    time.equals(other.time);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return body.hashCode() ^ sender.hashCode() ^ time.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Message(sender: \"%s\", body: \"%s\", time: \"%s\")",
                sender, body, time);
    }
}
