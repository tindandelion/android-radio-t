package org.dandelion.radiot.podcasts.ui;

import org.dandelion.radiot.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PodcastItemView extends RelativeLayout {
    public PodcastItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNumber(String value) {
        setElementText(R.id.podcast_item_view_number, value);
    }

    public void setPubDate(String value) {
        setElementText(R.id.podcast_item_view_date, value);
    }

    public void setShowNotes(String value) {
        setElementText(R.id.podcast_item_view_shownotes, value);
    }

    public void setThumbnail(Drawable thumbnail) {
        ImageView image = (ImageView) findViewById(R.id.podcast_item_icon);
        image.setImageDrawable(thumbnail);
    }

    private void setElementText(int id, String value) {
        TextView view = (TextView) findViewById(id);
        view.setText(value);
    }

    public void populateWith(PodcastVisual pv) {
        setNumber(pv.number);
        setPubDate(pv.pubDate);
        setShowNotes(pv.showNotes);
        setThumbnail(pv.thumbnail);
    }
}
