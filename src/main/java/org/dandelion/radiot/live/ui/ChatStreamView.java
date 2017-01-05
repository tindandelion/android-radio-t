package org.dandelion.radiot.live.ui;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChatStreamView extends ListView {
    private static boolean useSmoothScroll = true;

    static {
        useSmoothScroll = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public ChatStreamView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState myState = new SavedState(superState);
        myState.atBottom = atBottom();
        return myState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.atBottom) {
            setSelection(lastPosition());
        }
    }

    public boolean atBottom() {
        return getLastVisiblePosition() == (lastPosition());
    }

    public void scrollToBottom() {
        if (useSmoothScroll) {
            smoothScrollToPosition(lastPosition());
        } else {
            setSelection(lastPosition());
        }
    }

    private int lastPosition() {
        return getCount() - 1;
    }

    private static class SavedState extends BaseSavedState {
        public boolean atBottom;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeByte((byte) (atBottom ? 1 : 0));
        }

        @SuppressWarnings("UnusedDeclaration")
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public SavedState(Parcel in) {
            super(in);
            atBottom = in.readByte() > 0;
        }
    }
}
