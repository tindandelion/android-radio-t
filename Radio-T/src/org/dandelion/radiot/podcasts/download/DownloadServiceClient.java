package org.dandelion.radiot.podcasts.download;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

public class DownloadServiceClient implements PodcastProcessor {
    @Override
    public void process(Context context, PodcastItem podcast) {
        new DownloadCommand(context)
                .setTitle(podcast.getTitle())
                .setUrl(podcast.getAudioUri())
                .submit();
    }

    private static class DownloadCommand {
        private Context context;
        private Intent intent;

        private DownloadCommand(Context context) {
            this.context = context;
            this.intent = new Intent(context, DownloadService.class);
            intent.setAction(DownloadService.START_DOWNLOAD_ACTION);
        }

        public void submit() {
            context.startService(intent);
        }

        public DownloadCommand setUrl(String url) {
            intent.putExtra(DownloadService.URL_EXTRA, url);
            return this;
        }

        public DownloadCommand setTitle(String title) {
            intent.putExtra(DownloadService.TITLE_EXTRA, title);
            return this;
        }
    }
}
