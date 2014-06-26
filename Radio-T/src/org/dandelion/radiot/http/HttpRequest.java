package org.dandelion.radiot.http;

import android.os.AsyncTask;

public class HttpRequest<T> extends AsyncTask<Void, Void, Object> {
    protected final Provider<T> provider;
    protected final Consumer<T> dataConsumer;
    protected final Consumer<Exception> errorConsumer;

    public HttpRequest(Provider<T> provider, Consumer<T> dataConsumer, Consumer<Exception> errorConsumer) {
        this.provider = provider;
        this.dataConsumer = dataConsumer;
        this.errorConsumer = errorConsumer;
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
            errorConsumer.accept((Exception) result);
        } else {
            dataConsumer.accept((T) result);
        }
    }

    protected T makeRequest() throws Exception {
        return provider.get();
    }
}
