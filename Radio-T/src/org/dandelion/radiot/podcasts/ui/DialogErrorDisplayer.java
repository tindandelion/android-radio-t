package org.dandelion.radiot.podcasts.ui;

import android.content.Context;
import org.dandelion.radiot.R;
import android.app.AlertDialog;
import org.dandelion.radiot.podcasts.core.ErrorListener;

class DialogErrorDisplayer implements ErrorListener {
    private Context context;

    public DialogErrorDisplayer(Context context) {
        this.context = context;
    }

    @Override
    public void onError(String errorMessage) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.error_title)
                .setMessage(errorMessage)
                .show();
    }
}
