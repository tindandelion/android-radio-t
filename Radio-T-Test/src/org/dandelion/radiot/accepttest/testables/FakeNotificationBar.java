package org.dandelion.radiot.accepttest.testables;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.dandelion.radiot.live.service.NotificationBar;

import static junit.framework.Assert.assertEquals;

public class FakeNotificationBar implements NotificationBar {
    private ShownIcon shown;
    private int removedIconId = 0;

    @Override
    public void showIcon(int id, int resourceId, String title, String statusText) {
        this.shown = new ShownIcon(id, resourceId, title, statusText);
    }

    @Override
    public void hideIcon(int id) {
        removedIconId = id;
    }

    public void showsIcon(int id, int resourceId, String title, String statusText) {
        ShownIcon expected = new ShownIcon(id, resourceId, title, statusText);
        assertEquals("Shown icon", expected, shown);
    }

    public void hasRemovedIcon(int id) {
        assertEquals("Removed icon", id, removedIconId);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class ShownIcon {
        private final int id;
        private final int resourceId;
        public final String title;
        public final String statusText;

        private ShownIcon(int id, int resourceId, String title, String statusText) {
            this.id = id;
            this.resourceId = resourceId;
            this.title = title;
            this.statusText = statusText;
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object o) {
            return EqualsBuilder.reflectionEquals(this, o);
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
        }
    }
}
