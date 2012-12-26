package org.dandelion.radiot.live.chat;

public class PollingChatTranslation implements ChatTranslation {
    private final ChatTranslation translation;

    public PollingChatTranslation(ChatTranslation translation) {
        this.translation = translation;
    }

    @Override
    public void start(MessageConsumer consumer) {
        translation.start(consumer);
    }

    @Override
    public void refresh() {
        translation.refresh();
    }
}
