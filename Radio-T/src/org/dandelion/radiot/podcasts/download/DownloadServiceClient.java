package org.dandelion.radiot.podcasts.download;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

public class DownloadServiceClient implements PodcastProcessor {
    @Override
    public void process(Context context, PodcastItem podcast) {
        new StartCommand(context)
                .setTitle(podcast.getTitle())
                .setUrl(podcast.getAudioUri())
                .submit();
    }

    public void downloadCompleted(Context context, long id) {
        new CompletionCommand(context)
                .setId(id)
                .submit();
    }

    // TODO: Duplication between commands
    private static class CompletionCommand {
        private Context context;
        private Intent intent;

        public CompletionCommand(Context context) {
            this.context = context;
            this.intent = new Intent(context, DownloadService.class);
            intent.setAction(DownloadService.DOWNLOAD_COMPLETE_ACTION);
        }

        public CompletionCommand setId(long id) {
            intent.putExtra(DownloadService.TASK_ID_EXTRA, id);
            return this;
        }

        public void submit() {
            context.startService(intent);
        }
    }

    private static class StartCommand {
        private Context context;
        private Intent intent;

        private StartCommand(Context context) {
            this.context = context;
            this.intent = new Intent(context, DownloadService.class);
            intent.setAction(DownloadService.START_DOWNLOAD_ACTION);
        }

        public void submit() {
            context.startService(intent);
        }

        public StartCommand setUrl(String url) {
            intent.putExtra(DownloadService.URL_EXTRA, url);
            return this;
        }

        public StartCommand setTitle(String title) {
            intent.putExtra(DownloadService.TITLE_EXTRA, title);
            return this;
        }
    }

}
