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
	// These numbers are the positions in the ListView.
	public static final String PERSONAL_DATA = "0";
	public static final String ATTENDANCE = "1";
	public static final String MARKS = "2";

	/**
	 * An array of items.
	 */
	private List<OptionsItem> mItems = new ArrayList<OptionsItem>();
	
	public StudentOptionsContent(Context ctx) {
		addItem(new OptionsItem(PERSONAL_DATA, ctx.getString(R.string.personal_data)));
		addItem(new OptionsItem(ATTENDANCE, ctx.getString(R.string.attendance)));
		addItem(new OptionsItem(MARKS, ctx.getString(R.string.marks)));
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
