package org.dandelion.radiot.podcasts.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.core.PodcastListConsumer;

class PodcastListAdapter extends ArrayAdapter<PodcastVisual> implements PodcastListConsumer {
    private final Bitmap defaultPodcastImage;
    private Activity activity;

    public PodcastListAdapter(Activity activity) {
        super(activity, 0);
        this.activity = activity;
        defaultPodcastImage = BitmapFactory
                .decodeResource(activity.getResources(),
                        R.drawable.default_podcast_image);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(R.layout.podcast_list_item, parent, false);
        }

        return fillRowWithData(row, getItem(position));
    }

    private View fillRowWithData(View row, PodcastVisual pv) {
        setElementText(row, R.id.podcast_item_view_number, pv.podcast.getNumberString());
        setElementText(row, R.id.podcast_item_view_date, pv.podcast.getPubDate());
        setElementText(row, R.id.podcast_item_view_shownotes, pv.podcast.getShowNotes());
        setPodcastIcon(row, pv);
        return row;
    }

    private void setPodcastIcon(View row, PodcastVisual pv) {
        ImageView image = (ImageView) row.findViewById(R.id.podcast_item_icon);
        image.setImageBitmap(pv.getThumbnail(defaultPodcastImage));
    }

    private void setElementText(View row, int resourceId, String value) {
        TextView view = (TextView) row.findViewById(resourceId);
        view.setText(value);
    }

    @Override
    public void updatePodcasts(PodcastList podcasts) {
        clear();
        for (PodcastItem item : podcasts) {
            add(new PodcastVisual(item));
        }
    }
}
