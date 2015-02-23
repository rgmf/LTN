package es.rgmf.ltn.util.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import es.rgmf.ltn.R;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class StudentOptionsContent {
	public static final String PERSONAL_DATA = "1";
	public static final String ATTENDANCE = "2";

	/**
	 * An array of items.
	 */
	private List<OptionsItem> mItems = new ArrayList<OptionsItem>();
	
	public StudentOptionsContent(Context ctx) {
		addItem(new OptionsItem("1", ctx.getString(R.string.personal_data)));
		addItem(new OptionsItem("2", ctx.getString(R.string.attendance)));
	}

	private void addItem(OptionsItem item) {
		mItems.add(item);
	}
	
	public List<OptionsItem> getItems() {
		return mItems;
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class OptionsItem {
		public String id;
		public String content;

		public OptionsItem(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
