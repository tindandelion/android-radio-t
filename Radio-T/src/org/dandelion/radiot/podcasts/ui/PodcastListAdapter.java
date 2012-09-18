package org.dandelion.radiot.podcasts.ui;

import org.dandelion.radiot.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.dandelion.radiot.podcasts.core.PodcastItem;

class PodcastListAdapter extends ArrayAdapter<PodcastItem> {
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

    private View fillRowWithData(View row, PodcastItem item) {
        setElementText(row, R.id.podcast_item_view_number, item.getNumberString());
        setElementText(row, R.id.podcast_item_view_date, item.getPubDate());
        setElementText(row, R.id.podcast_item_view_shownotes, item.getShowNotes());
        setPodcastIcon(row, item);
        return row;
    }

    private void setPodcastIcon(View row, PodcastItem item) {
        ImageView image = (ImageView) row.findViewById(R.id.podcast_item_icon);
        Bitmap bitmap = item.getThumbnail();
        if (null == bitmap) {
            bitmap = defaultPodcastImage;
        }
        image.setImageBitmap(bitmap);
    }

    private void setElementText(View row, int resourceId, String value) {
        TextView view = (TextView) row.findViewById(resourceId);
        view.setText(value);
    }
}
