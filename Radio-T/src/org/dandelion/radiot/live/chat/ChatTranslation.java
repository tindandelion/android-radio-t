package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.common.Scheduler;
import org.dandelion.radiot.http.HttpDataEngine;

import java.util.List;

public class ChatTranslation extends HttpDataEngine<List<Message>> {
    public ChatTranslation(String baseUrl, Scheduler refreshScheduler) {
        super(ChatClient.create(baseUrl), refreshScheduler);
    }

    public ChatTranslation(ChatClient dataProvider, Scheduler pollScheduler) {
        super(dataProvider, pollScheduler);
    }
}
