package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.common.Scheduler;
import org.dandelion.radiot.http.HttpDataEngine;

import java.util.List;

public class ChatTranslationEngine extends HttpDataEngine<List<Message>> {
    public ChatTranslationEngine(String baseUrl, Scheduler refreshScheduler) {
        super(ChatClient.create(baseUrl), refreshScheduler);
    }

    public ChatTranslationEngine(ChatClient dataProvider, Scheduler pollScheduler) {
        super(dataProvider, pollScheduler);
    }
}
