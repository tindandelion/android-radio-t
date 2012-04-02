package org.dandelion.radiot.live.service;

import android.app.Service;
import org.dandelion.radiot.util.IconNote;

class Foregrounder {
    private final Service service;

    public Foregrounder(Service service) {
        this.service = service;
    }

    public void stopForeground() {
        service.stopForeground(true);
    }

    public void startForeground(IconNote note) {
        service.startForeground(note.id(), note.build());
    }
}
