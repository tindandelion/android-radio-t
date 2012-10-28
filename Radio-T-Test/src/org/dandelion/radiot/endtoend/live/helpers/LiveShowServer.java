package org.dandelion.radiot.endtoend.live.helpers;

import android.content.Context;
import org.dandelion.radiot.helpers.LiveStreamServer;

import java.io.IOException;

public class LiveShowServer extends LiveStreamServer {
    private boolean translationActivated = true;

    public LiveShowServer(Context context) throws IOException {
        super(context);
    }

    @Override
    protected Response serveUri(String uri) {
        if (translationActivated) {
            return super.serveUri(uri);
        }
        return notFound();
    }

    private Response notFound() {
        return new Response(HTTP_NOTFOUND, MIME_PLAINTEXT, "");
    }

    public void activateTranslation() {
        translationActivated = true;
    }

    public void suppressTranslation() {
        translationActivated = false;
    }
}
