package org.dandelion.radiot.live.chat;

public class Message {
    public final String body;
    public final String sender;
    public final String time;
    public final int seq;

    public Message(String sender, String body, String time, int seq) {
        this.body = body;
        this.sender = sender;
        this.time = time;
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "Message{" +
                "body='" + body + '\'' +
                ", sender='" + sender + '\'' +
                ", time='" + time + '\'' +
                ", seq=" + seq +
                '}';
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (seq != message.seq) return false;
        if (body != null ? !body.equals(message.body) : message.body != null) return false;
        if (sender != null ? !sender.equals(message.sender) : message.sender != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = body != null ? body.hashCode() : 0;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + seq;
        return result;
    }
}
