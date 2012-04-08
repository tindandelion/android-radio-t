package org.dandelion.radiot.live.core;

import java.io.Serializable;

public enum LiveShowState implements Serializable {
    Idle {
        @Override
        public void togglePlayback(LiveShowPlayer player) {
            player.beConnecting();
        }

        @Override
        public void acceptVisitor(LiveShowPlayer.StateVisitor visitor, long timestamp) {
            visitor.onIdle();
        }
    },

    Connecting {
        @Override
        public void togglePlayback(LiveShowPlayer player) {
            player.beStopping();
        }

        @Override
        public void acceptVisitor(LiveShowPlayer.StateVisitor visitor, long timestamp) {
            visitor.onConnecting(timestamp);
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

        @Override
        public void acceptVisitor(LiveShowPlayer.StateVisitor visitor, long timestamp) {
            visitor.onPlaying(timestamp);
        }

        public void handleError(LiveShowPlayer player) {
            player.beConnecting();
        }
    },

    Stopping {
        @Override
        public void acceptVisitor(LiveShowPlayer.StateVisitor visitor, long timestamp) {
            visitor.onStopping(timestamp);
        }
    },

    Waiting {
        @Override
        public void togglePlayback(LiveShowPlayer player) {
            player.beIdle();
        }

        @Override
        public void acceptVisitor(LiveShowPlayer.StateVisitor visitor, long timestamp) {
            visitor.onWaiting(timestamp);
        }
    };

    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor, long timestamp) {
    }

    public void togglePlayback(LiveShowPlayer player) {
    }

    public void handleError(LiveShowPlayer player) {
    }

    public static boolean isInactive(LiveShowState state) {
        return (state == Idle) || (state == Waiting);
    }
}
