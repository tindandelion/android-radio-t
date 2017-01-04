package org.dandelion.radiot.live.core;

import java.io.Serializable;

public enum LiveShowState implements Serializable {
    Idle {
        @Override
        public void togglePlayback(LiveShowPlayer player) {
            player.beConnecting();
        }

    },

    Connecting {
        @Override
        public void togglePlayback(LiveShowPlayer player) {
            player.beStopping();
        }

        @Override
        public void handleError(LiveShowPlayer player) {
            player.beWaiting();
        }
    },

    Playing {
        @Override
        public void togglePlayback(LiveShowPlayer player) {
            player.beStopping();
        }

        public void handleError(LiveShowPlayer player) {
            player.beConnecting();
        }
    },

    Stopping {
    },

    Waiting {
        @Override
        public void togglePlayback(LiveShowPlayer player) {
            player.beIdle();
        }

    };

    public void togglePlayback(LiveShowPlayer player) {
    }

    public void handleError(LiveShowPlayer player) {
    }

    public static boolean isIdle(LiveShowState state) {
        return state == Idle;
    }

    public static boolean isActive(LiveShowState state) {
        return state != Idle && state != Waiting;
    }
}
