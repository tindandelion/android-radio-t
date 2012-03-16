package org.dandelion.radiot.podcasts.download;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

public class DownloadServiceClient implements PodcastProcessor {
    @Override
    public void process(Context context, String url) {
        submitServiceCommand(context, url);
    }

    private void submitServiceCommand(Context context, String url) {
        new DownloadCommand(context)
                .setUrl(url)
                .submit();
    }
    
    private static class DownloadCommand {
        private Context context;
        private Intent intent;

        private DownloadCommand(Context context) {
            this.context = context;
            this.intent = new Intent(context, DownloadService.class);
        }

        public void submit() {
            context.startService(intent);
        }

        public DownloadCommand setUrl(String url) {
            intent.putExtra(DownloadService.URL_EXTRA, url);
            return this;
        }
    }
}
