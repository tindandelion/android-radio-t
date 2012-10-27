package org.dandelion.radiot.podcasts.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.loader.PodcastsConsumer;

class PodcastListAdapter extends ArrayAdapter<PodcastVisual> implements PodcastsConsumer {
    private final Drawable defaultThumbnail;
    private LayoutInflater inflater;

    public PodcastListAdapter(Activity activity) {
        super(activity, 0);
        this.inflater = activity.getLayoutInflater();
        defaultThumbnail = loadDefaultThumbnail();
    }

    private Drawable loadDefaultThumbnail() {
        return getContext().getResources().getDrawable(R.drawable.default_podcast_image);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
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
        image.setImageDrawable(pv.thumbnail);
    }

    private void setElementText(View row, int resourceId, String value) {
        TextView view = (TextView) row.findViewById(resourceId);
        view.setText(value);
    }

    @Override
    public void updateList(PodcastList podcasts) {
        final Resources res = getContext().getResources();

        clear();
        for (PodcastItem item : podcasts) {
            add(new PodcastVisual(item, defaultThumbnail, res));
        }
    }

    @Override
    public void updateThumbnail(PodcastItem item, byte[] thumbnail) {
        PodcastVisual visual = findVisualForItem(item);
        visual.setThumbnail(thumbnail);
        notifyDataSetChanged();
    }

    private PodcastVisual findVisualForItem(PodcastItem item) {
        for(int i = 0; i < getCount(); i++) {
            PodcastVisual visual = getItem(i);
            if (visual.podcast == item) {
                return visual;
            }
        }
        return null;
    }
}
