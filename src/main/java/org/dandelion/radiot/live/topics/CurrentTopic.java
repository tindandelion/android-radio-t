package org.dandelion.radiot.live.topics;

public class CurrentTopic {
    private static final CurrentTopic EMPTY = new CurrentTopic();

    public final String id;
    public final String text;

    public static CurrentTopic empty() {
        return EMPTY;
    }

    public static CurrentTopic create(String id, String text) {
        return new CurrentTopic(id, text);
    }

    private CurrentTopic(String id, String text) {
        this.id = id;
        this.text = text;
    }

    private CurrentTopic() {
        this("", "");
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
