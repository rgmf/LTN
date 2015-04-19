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

package es.rgmf.ltn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import es.rgmf.ltn.fragments.StudentDetailFragment;
import es.rgmf.ltn.fragments.StudentOptionsFragment;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.orm.CourseStudent;
import es.rgmf.ltn.util.ui.StudentOptionsContent;

/**
 * An activity representing a list of Students. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link StudentDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link StudentOptionsFragment} and the item details (if present) is a
 * {@link StudentDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link StudentOptionsFragment.Callbacks} interface to listen for item
 * selections.
 */
public class StudentActivity extends Activity implements
		StudentOptionsFragment.Callbacks {
	public static final String STUDENT_ID = "student_id";
	public static final String COURSE_ID = "course_id";
	public static final String OPTION_ID = "option_id";
	
	/**
	 * The student identify.
	 */
	private Integer mStudentId;
	/**
	 * The course identify.
	 */
	private Integer mCourseId;
	/**
	 * The option index selected in the options list view.
	 */
	private Integer mOptionIdx;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student);

		if (findViewById(R.id.student_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			StudentOptionsFragment sof = ((StudentOptionsFragment) getFragmentManager().findFragmentById(
					R.id.student_options));
			sof.setActivateOnItemClick(true);
			
			// Get student and course information passed to here through arguments.
			Bundle args = getIntent().getExtras();
			mStudentId = args.getInt(STUDENT_ID);
			mCourseId = args.getInt(COURSE_ID);
			
			// Set the text view with name and lastname of the student.
			CourseStudent courseStudent = ReaderModel.getCourseStudent(this, mCourseId, mStudentId);
			TextView tvStudentName = (TextView) findViewById(R.id.student_name);
			tvStudentName.setText(courseStudent.getStudent().getLastname() + ", " + courseStudent.getStudent().getName());
			
			// Select the option (this can be passed from other activity or fragment).
			// By default we select personal data option.
			if (savedInstanceState == null) {
				mOptionIdx = Integer.valueOf(StudentOptionsContent.PERSONAL_DATA);
				if (args.containsKey(OPTION_ID)) {
					//onItemSelected(args.getString(OPTION_ID));
					mOptionIdx = Integer.valueOf(args.getString(OPTION_ID));
					//sof.getListView().requestFocusFromTouch();
					//sof.getListView().setSelection(mOptionIdx);
					//sof.getListView().performItemClick(sof.getListView().getAdapter().getView(mOptionIdx, null, null), mOptionIdx, mOptionIdx);
				}
				sof.getListView().requestFocusFromTouch();
				sof.getListView().setSelection(mOptionIdx);
				sof.getListView().performItemClick(sof.getListView().getAdapter().getView(mOptionIdx, null, null), mOptionIdx, mOptionIdx);
			}
			else {
				mOptionIdx = savedInstanceState.getInt(OPTION_ID);
				sof.getListView().requestFocusFromTouch();
				sof.getListView().setSelection(mOptionIdx);
				sof.getListView().performItemClick(sof.getListView().getAdapter().getView(mOptionIdx, null, null), mOptionIdx, mOptionIdx);
			}
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(OPTION_ID, mOptionIdx);
	}

	/**
	 * Callback method from {@link StudentOptionsFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		mOptionIdx = Integer.valueOf(id);
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(StudentDetailFragment.ARG_ITEM_ID, id);
			arguments.putInt(STUDENT_ID, mStudentId);
			arguments.putInt(COURSE_ID, mCourseId);
			StudentDetailFragment fragment = new StudentDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.student_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, StudentDetailActivity.class);
			detailIntent.putExtra(StudentDetailFragment.ARG_ITEM_ID, id);
			detailIntent.putExtra(STUDENT_ID, mStudentId);
			detailIntent.putExtra(COURSE_ID, mCourseId);
			startActivity(detailIntent);
		}
	}
	
	/**
	 * We finish this activity when user click on "up button" because in the
	 * default behaviour this activity goes to MainActivity that is its
	 * parent and we need to back to the Fragment where it called this 
	 * Activity.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
	        switch (item.getItemId()) {
	        case android.R.id.home:
	        	finish();
	            return true;
	        }

	    return super.onOptionsItemSelected(item);
	}
}
