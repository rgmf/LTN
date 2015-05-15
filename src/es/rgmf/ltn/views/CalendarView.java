/**
 * Copyright (C) 2014 Román Ginés Martínez Ferrández <rgmf@riseup.net>
 *
 * This program (LibreTeacherNotebook) is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU General 
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.rgmf.ltn.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Parcelable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import es.rgmf.ltn.R;

/**
 * This fragment show a Calendar in a TableLayout.
 * 
 * This code is based in the code that you can find here:
 * https://github.com/dharmin007/Android-Calendar-Widget
 * 
 * From dharmin007 github's user.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class CalendarView extends TableLayout {
	private static final String MONTH_NUMBER_STATE = "month_number";

	private Context mContext;
	private TextView mTextView;
	private boolean mAnimFlag = false;
	private int mSelectedDay = 0;
	private int mDay = 0, mMonth = 0, mYear = 0;
	private int mFirstDay = Calendar.SUNDAY;
	private TextView mBtn;
	private TranslateAnimation mAnimSet1, mAnimSet2;
	private TableRow mRow;
	private Boolean[] mIsEvent = new Boolean[32];
	private int[] mResDaysSun = { R.string.sunday, R.string.monday,
			R.string.tuesday, R.string.wednesday, R.string.thursday,
			R.string.friday, R.string.saturday };
	private int[] mResDaysMon = { R.string.monday, R.string.tuesday,
			R.string.wednesday, R.string.thursday, R.string.friday,
			R.string.saturday, R.string.sunday };
	private String[] mDays;
	private int[] mMonthIds = { R.string.january, R.string.february,
			R.string.march, R.string.april, R.string.may, R.string.june,
			R.string.july, R.string.august, R.string.september,
			R.string.october, R.string.november, R.string.december };
	private String[] mMonths = new String[12];
	private Calendar mCalendar;
	private Calendar mPrevCalendar; // prevCal will be used to display last few
									// dates of previous month in the
									// calendar.
	private Calendar mToday; // today will be used for
								// setting a box around
								// today's date.
	/**
	 * Map with days of the current month that have custom background color.
	 * 
	 * The key is a String in 'yyyy/MM/dd' format. The value is a Color (like
	 * Color.RED, for instance).
	 */
	private Map<String, Integer> mColorMap = new HashMap<String, Integer>();

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param params
	 */
	public CalendarView(Context context, AttributeSet params) {
		super(context);
		init(context);
	}

	/**
	 * Constructor.
	 * 
	 * @param context
	 */
	public CalendarView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Constructor.
	 * 
	 * @param context
	 */
	public CalendarView(Context context, Map<String, Integer> colorMap) {
		super(context);
		mColorMap = colorMap;
		init(context);
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Log.v("halsdfjlasjf", "lasdjflkas");
		return super.onSaveInstanceState();
	}
		
	/**
	 * Initialized the calendar.
	 * 
	 * @param context
	 */
	private void init(Context context) {
		mContext = context;

		Resources r = getResources();
		for (int i = 0; i < 12; i++)
			mMonths[i] = r.getString(mMonthIds[i]);

		mDays = new String[7];

		setStretchAllColumns(true); // stretch all columns so that calendar's
									// width fits the screen

		mToday = Calendar.getInstance();// get current date and time's instance
		mToday.clear(Calendar.HOUR);// remove the hour,minute,second and
									// millisecond from the today variable
		mToday.clear(Calendar.MINUTE);
		mToday.clear(Calendar.SECOND);
		mToday.clear(Calendar.MILLISECOND);
		mFirstDay = Calendar.getInstance().getFirstDayOfWeek();
		mToday.setFirstDayOfWeek(mFirstDay);

		mCalendar = (Calendar) mToday.clone(); // create exact copy as today for
												// dates display purpose.

		displayMonth(true);
	}

	/**
	 * Called when user change the month.
	 */
	private OnClickListener ChangeMonthListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ImageView tv = (ImageView) v;
			// If previous month is to be displayed subtract one from current
			// month.
			if (tv.getTag().equals("<")) {
				mCalendar.add(Calendar.MONTH, -1);
				mAnimFlag = false;
			}
			// If next month is to be displayed add one to the current month
			else {
				mCalendar.add(Calendar.MONTH, 1);
				mAnimFlag = true;
			}
			mSelectedDay = 0;
			displayMonth(true);
		}
	};

	/**
	 * Called when a day is clicked.
	 */
	private OnClickListener dayClickedListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mTextView != null) {
				try {
					if (mIsEvent[mDay]) {
						mTextView
								.setBackgroundResource(R.drawable.calendar_day_in_month);
					} else
						mTextView
								.setBackgroundResource(R.drawable.calendar_rectangle_grad);
				} catch (Exception ex) {
					mTextView
							.setBackgroundResource(R.drawable.calendar_rectangle_grad);
				}
				mTextView.setPadding(8, 8, 8, 8);
			}
			if (mTextView.getText().toString().trim()
					.equals(String.valueOf(mToday.get(Calendar.DATE)))) {
				mTextView
						.setBackgroundResource(R.drawable.calendar_selected_grad);
			}
			mDay = Integer.parseInt(v.getTag().toString());
			mSelectedDay = mDay;
			mTextView = (TextView) v;
			mTextView.setBackgroundResource(R.drawable.calendar_selected_grad);
			displayMonth(false);

			/*
			 * save the day,month and year in the public int variables day,month
			 * and year so that they can be used when the calendar is closed
			 */
			mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
		}
	};

	/**
	 * Display a month.
	 * 
	 * @param animationEnabled
	 */
	private void displayMonth(boolean animationEnabled) {
		Resources r = getResources();
		String tempDay, strCalendar;
		int firstDayOfWeek, prevMonthDay, nextMonthDay, week;

		if (animationEnabled) {
			mAnimSet1 = new TranslateAnimation(0, getWidth(), 1, 1);
			mAnimSet1.setDuration(300);
			mAnimSet2 = new TranslateAnimation(0, -getWidth(), 1, 1);
			mAnimSet2.setDuration(300);
		}

		for (int i = 0; i < 7; i++) {
			if (mFirstDay == Calendar.MONDAY)
				tempDay = r.getString(mResDaysMon[i]);
			else
				tempDay = r.getString(mResDaysSun[i]);
			mDays[i] = tempDay.substring(0, 3);
		}

		// Clears the calendar so that a new month can be displayed, removes all
		// child elements (days,week numbers, day labels)
		removeAllViews();

		// Set date = 1st of current month so that we can know in next step
		// which day is the first day of the week.
		mCalendar.set(Calendar.DAY_OF_MONTH, 1);

		// get which day is on the first date of the month.
		firstDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (mFirstDay == Calendar.MONDAY) {
			firstDayOfWeek--;
			if (firstDayOfWeek == -1)
				firstDayOfWeek = 6;
		}

		// get which week is the current week.
		week = mCalendar.get(Calendar.WEEK_OF_YEAR) - 1;

		// adjustment for week number when January starts with first day of
		// month as Sunday.
		if (firstDayOfWeek == 0
				&& mCalendar.get(Calendar.MONTH) == Calendar.JANUARY)
			week = 1;
		if (week == 0)
			week = 52;

		// create a calendar item for the previous month by subtracting.
		mPrevCalendar = (Calendar) mCalendar.clone();

		// 1 from the current month get the number of days in the previous month
		// to display last few days of previous month.
		mPrevCalendar.add(Calendar.MONTH, -1);
		prevMonthDay = mPrevCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
				- firstDayOfWeek + 1;

		// set the next month counter to date 1.
		nextMonthDay = 1;
		android.widget.TableRow.LayoutParams lp;
		RelativeLayout rl = (RelativeLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.calendar_month_top, null);

		// create the left arrow button for displaying the previous month.
		ImageView btn1 = (ImageView) rl.findViewById(R.id.imgLeft);
		btn1.setTag("<");
		btn1.setOnClickListener(ChangeMonthListener);
		mBtn = (TextView) rl.findViewById(R.id.txtDay);
		mBtn.setText(mMonths[mCalendar.get(Calendar.MONTH)]);
		((TextView) rl.findViewById(R.id.txtYear)).setText(""
				+ mCalendar.get(Calendar.YEAR));
		// create the right arrow button for displaying the next month
		btn1 = (ImageView) rl.findViewById(R.id.imgRight);
		btn1.setTag(">");
		btn1.setOnClickListener(ChangeMonthListener);

		// add the tablerow containing the next and prev views to the calendar.
		addView(rl);

		// create a new row to add to the tablelayout.
		mRow = new TableRow(mContext);
		mRow.setWeightSum(0.7f);
		lp = new TableRow.LayoutParams();
		lp.weight = 0.1f;

		// Create the day labels on top of the calendar
		for (int i = 0; i < 7; i++) {
			mBtn = new TextView(mContext);
			mBtn.setBackgroundResource(R.drawable.calendar_header);
			mBtn.setPadding(10, 5, 10, 5);
			mBtn.setLayoutParams(lp);
			mBtn.setTextColor(getResources().getColor(R.color.black));
			mBtn.setText(mDays[i]);
			mBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources()
					.getDimension(R.dimen.text_size_medium));
			mBtn.setGravity(Gravity.CENTER);
			mRow.addView(mBtn); // add the day label to the tablerow
		}

		if (animationEnabled) {
			if (mAnimFlag)
				mRow.startAnimation(mAnimSet2);
			else
				mRow.startAnimation(mAnimSet1);
		}

		// add the tablerow to the tablelayout (first row of the calendar).
		addView(mRow);
		mRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		/*
		 * initialize the day counter to 1, it will be used to display the dates
		 * of the month
		 */
		int day = 1;
		lp = new TableRow.LayoutParams();
		lp.weight = 0.1f;
		for (int i = 0; i < 6; i++) {
			if (day > mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
				break;
			mRow = new TableRow(mContext);
			mRow.setWeightSum(0.7f);
			// this loop is used to fill out the days in the i-th row in the
			// calendar
			for (int j = 0; j < 7; j++) {
				mBtn = new TextView(mContext);
				mBtn.setLayoutParams(lp);
				mBtn.setBackgroundResource(R.drawable.calendar_rectangle_grad);
				mBtn.setGravity(Gravity.CENTER);
				mBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources()
						.getDimension(R.dimen.text_size_medium));
				mBtn.setTextColor(Color.GRAY);

				// checks if the first day of the week has arrived or previous
				// month's date should be printed.
				if (j < firstDayOfWeek && day == 1)
					mBtn.setText(Html.fromHtml(String.valueOf("<b>"
							+ prevMonthDay++ + "</b>")));
				// checks to see whether to print next month's date.
				else if (day > mCalendar
						.getActualMaximum(Calendar.DAY_OF_MONTH)) {
					mBtn.setText(Html.fromHtml("<b>" + nextMonthDay++ + "</b>"));
					// day counter is in the current month.
				} else {
					try {
						if (mIsEvent[day])
							mBtn.setBackgroundResource(R.drawable.calendar_day_in_month);
						else
							mBtn.setBackgroundResource(R.drawable.calendar_rectangle_grad);
					} catch (Exception ex) {
						mBtn.setBackgroundResource(R.drawable.calendar_rectangle_grad);
					}

					mCalendar.set(Calendar.DAY_OF_MONTH, day);

					// tag to be used when closing the calendar view.
					mBtn.setTag(day);
					mBtn.setOnClickListener(dayClickedListener);

					// if the day is today then set different background and
					// text color.
					if (mCalendar.equals(mToday)) {
						mTextView = mBtn;
						mBtn.setBackgroundResource(R.drawable.calendar_current_day);
						mBtn.setTextColor(Color.BLACK);
					} else if (mSelectedDay == day) {
						mTextView = mBtn;
						mBtn.setBackgroundResource(R.drawable.calendar_selected_grad);
						mBtn.setTextColor(Color.BLACK);
					} else
						mBtn.setTextColor(Color.BLACK);

					// if this day is in color map set background specify.
					strCalendar = CalendarView.getStringFromCalendar(mCalendar);
					if (mColorMap.containsKey(strCalendar)) {
						mBtn.setBackgroundColor(mColorMap.get(strCalendar));
					}

					// set the text of the day.
					mBtn.setText(Html.fromHtml("<b>" + String.valueOf(day++)
							+ "</b>"));

					// sunday (red text) or saturday (blue text)?
					if (mFirstDay == Calendar.SUNDAY) {
						if (j == 0)
							mBtn.setTextColor(Color.RED);// parseColor("#D73C10"));
						else if (j == 6)
							mBtn.setTextColor(Color.BLUE);// parseColor("#009EF7"));
					} else {
						if (j == 6)
							mBtn.setTextColor(Color.RED);
						else if (j == 5)
							mBtn.setTextColor(Color.BLUE);
					}

					// days from other month (gray text color).
					if ((day == this.mDay + 1)
							&& (this.mMonth == mCalendar.get(Calendar.MONTH) + 1)
							&& (this.mYear == mCalendar.get(Calendar.YEAR)))
						mBtn.setBackgroundColor(Color.GRAY);
				}

				// maintains proper distance between two adjacent days.
				mBtn.setPadding(8, 8, 8, 8);
				mRow.addView(mBtn);
			}
			if (animationEnabled) {
				if (mAnimFlag)
					mRow.startAnimation(mAnimSet2);
				else
					mRow.startAnimation(mAnimSet1);
			}
			// this adds a table row for six times for six different rows in the
			// calendar
			addView(mRow);
		}
	}

	/**
	 * Return the string from the calendar.
	 * 
	 * @param calendar
	 *            The calendar date.
	 * @return the string representing the calendar date.
	 */
	public static String getStringFromCalendar(Calendar calendar) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = formatter.format(calendar.getTime());
		return strDate;
	}
}
