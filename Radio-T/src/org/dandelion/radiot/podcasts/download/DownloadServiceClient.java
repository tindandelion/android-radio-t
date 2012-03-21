package org.dandelion.radiot.podcasts.download;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

import java.net.MalformedURLException;
import java.net.URL;

public class DownloadServiceClient implements PodcastProcessor {

    @Override
    public void process(Context context, PodcastItem podcast) throws IncorrectPodcastAudioUrl {
        new StartCommand(context)
                .setTitle(podcast.getTitle())
                .setUrl(checkUrl(podcast))
                .submit();
    }

    private String checkUrl(PodcastItem podcast) throws IncorrectPodcastAudioUrl {
        try {
            URL url = new URL(podcast.getAudioUri());
            return url.toString();
        } catch (MalformedURLException e) {
            throw new IncorrectPodcastAudioUrl();
        }
    }

    public void downloadCompleted(Context context, long id) {
        new CompletionCommand(context)
                .setId(id)
                .submit();
    }
    
    private static abstract class Command {
        protected Context context;
        protected Intent intent;

        private Command(Context context, String action) {
            this.context = context;
            this.intent = new Intent(context, DownloadService.class);
            this.intent.setAction(action);
        }
        
        public void submit() {
            context.startService(intent);
        }
    }

    private static class CompletionCommand extends Command {

        public CompletionCommand(Context context) {
            super(context, DownloadService.DOWNLOAD_COMPLETE_ACTION);
        }

        public CompletionCommand setId(long id) {
            intent.putExtra(DownloadService.TASK_ID_EXTRA, id);
            return this;
        }
    }

    private static class StartCommand extends Command {
        private StartCommand(Context context) {
            super(context, DownloadService.START_DOWNLOAD_ACTION);
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
