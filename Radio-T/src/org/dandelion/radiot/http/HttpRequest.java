package org.dandelion.radiot.http;

import android.os.AsyncTask;

public class HttpRequest<T> extends AsyncTask<Void, Void, Object> {
    public interface ErrorListener {
        void onError();
    }

    protected final Provider<T> provider;
    protected final Consumer<T> consumer;
    protected final ErrorListener errorListener;

    public HttpRequest(Provider<T> provider, Consumer<T> consumer, ErrorListener errorListener) {
        this.provider = provider;
        this.consumer = consumer;
        this.errorListener = errorListener;
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            return makeRequest();
        } catch (Exception e) {
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof Exception) {
            errorListener.onError();
        } else {
            consumer.accept((T) result);
        }
    }

    protected T makeRequest() throws Exception {
        return provider.get();
    }
}
