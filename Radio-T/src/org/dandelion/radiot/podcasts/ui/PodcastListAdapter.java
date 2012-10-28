package org.dandelion.radiot.podcasts.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        PodcastItemView row = (PodcastItemView) convertView;
        if (row == null) {
            row = (PodcastItemView) inflater.inflate(R.layout.podcast_list_item, parent, false);
        }

        PodcastVisual pv = getItem(position);
        row.populateWith(pv);
        return row;
    }

    @Override
    public void updateList(PodcastList podcasts) {
        clear();
        for (PodcastItem item : podcasts) {
            add(new PodcastVisual(item, defaultThumbnail));
        }
    }

    @Override
    public void updateThumbnail(PodcastItem item, byte[] thumbnail) {
        PodcastVisual pv = findVisualForItem(item);
        pv.setThumbnail(createDrawable(thumbnail));
        notifyDataSetChanged();
    }

    private Drawable createDrawable(byte[] thumbnail) {
        final Resources res = getContext().getResources();

        if (thumbnail != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
            if (bitmap != null) {
                return new BitmapDrawable(res, bitmap);
            }
        }
        return null;
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
