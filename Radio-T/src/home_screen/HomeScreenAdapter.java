package home_screen;

import java.util.ArrayList;

import org.dandelion.radiot.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class HomeScreenAdapter extends ArrayAdapter<HomeScreenItem> implements OnItemClickListener {

	private static final float SEPARATOR_HEIGHT_DIP = 16;
	private static final HomeScreenItem SEPARATOR = new HomeScreenItem(null, 0, null);
	
	private LayoutInflater layoutInflater;

	public HomeScreenAdapter(Activity activity) {
		super(activity, R.layout.home_screen_item, new ArrayList<HomeScreenItem>());
		layoutInflater = activity.getLayoutInflater();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HomeScreenItem item = getItem(position);
		if (item.isSeparator()) {
			return buildSeparatorRow();
		} else {
			View row = layoutInflater.inflate(
					R.layout.home_screen_item, null, false);
			return buildViewForItem(row, item);
		}
	}

	private View buildSeparatorRow() {
		View view = new View(getContext());
		view.setMinimumHeight(getSeparatorHeight());
		return view;
	}

	private int getSeparatorHeight() {
		float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (scale * SEPARATOR_HEIGHT_DIP + 0.5f);
	}

	private View buildViewForItem(View row, HomeScreenItem item) {
		TextView text = (TextView) row
				.findViewById(R.id.home_screen_item_title);
		text.setText(item.title);

		ImageView icon = (ImageView) row
				.findViewById(R.id.home_screen_item_icon);
		if (item.hasIcon()) {
			icon.setImageResource(item.iconId);
		}

		return row;
	}
	
	public void clickItem(int position) {
		getItem(position).execute();
	}
	
	public void addItem(int titleId, int iconId, HomeScreenItem.OnClickListener listener) {
		add(new HomeScreenItem(getContext().getString(titleId), iconId, listener));
	}

	@Override
	public boolean isEnabled(int position) {
		return !this.getItem(position).isSeparator();
	}

	public void addSeparator() {
		add(SEPARATOR);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		this.getItem(position).execute();
	}
}

class HomeScreenItem {
	public interface OnClickListener {
		void onClick(HomeScreenItem item);
	}

	public String title;
	public int iconId = 0;
	private OnClickListener listener;

	public HomeScreenItem(String title, int iconId, OnClickListener listener) {
		this.title = title;
		this.iconId = iconId;
		this.listener = listener;
	}

	public void execute() {
		listener.onClick(this);
	}

	public boolean hasIcon() {
		return iconId > 0;
	}

	public boolean isSeparator() {
		return title == null;
	}
}
