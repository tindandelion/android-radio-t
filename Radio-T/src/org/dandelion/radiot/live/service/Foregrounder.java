package org.dandelion.radiot.live.service;

import android.app.Service;
import org.dandelion.radiot.util.IconNote;

class Foregrounder {
    private final Service service;
    private boolean isForeground = false;

    public Foregrounder(Service service) {
        this.service = service;
    }

    public void stopForeground() {
        if (isForeground) {
            service.stopForeground(true);
            isForeground = false;
        }
    }

    public void startForeground(IconNote note) {
        service.startForeground(note.id(), note.build());
        isForeground = true;
    }
}
