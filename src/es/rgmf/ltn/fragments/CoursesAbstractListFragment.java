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

package es.rgmf.ltn.fragments;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.adapters.CourseListAdapter;
import es.rgmf.ltn.adapters.StudentListAdapter;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.orm.Course;

/**
 * The fragment with the list of courses.
 * 
 * This fragment communicate to {@link CourseDetailFragment.java} fragment to
 * inflate the values of the course selected int this list fragment.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
abstract public class CoursesAbstractListFragment extends ListFragment {
	private static final String STATE_COURSE_ID = "courseId";
	private static final String STATE_SELECTED_ITEM = "selectedItem";

	/**
	 * The list view that need to be inflate.
	 */
	protected ListView mListView;
	/**
	 * The list of courses that will be showed in the list view.
	 */
	protected ArrayList<Course> mCoursesList;
	/**
	 * The adapter.
	 */
	protected CourseListAdapter mAdapter;
	/**
	 * The course id.
	 */
	protected int mCourseId;
	/**
	 * The course selected index.
	 */
	protected int mSelectedItem;

	/**
	 * This method is called when this fragment is created.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mCoursesList = ReaderModel.getCourses(getActivity());
		mAdapter = new CourseListAdapter(getActivity(), mCoursesList);
		setListAdapter(mAdapter);

		if (savedInstanceState == null) {
			selectFirstCourse();
		} else {
			mSelectedItem = (int) savedInstanceState
					.getLong(STATE_SELECTED_ITEM);
			mCourseId = savedInstanceState.getInt(STATE_COURSE_ID);
			mAdapter.setSelectedItem(mSelectedItem);
			setCourse((Course) mAdapter.getItem(mSelectedItem));
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/**
	 * This method is called when the fragment is showed to the user.
	 */
	@Override
	public void onResume() {
		super.onResume();

		mCoursesList = ReaderModel.getCourses(getActivity());
		mAdapter = new CourseListAdapter(getActivity(), mCoursesList);
		setListAdapter(mAdapter);

		if (mAdapter.getCount() > 0) {
			mAdapter.setSelectedItem(mSelectedItem);
			setCourse((Course) mAdapter.getItem(mSelectedItem));
		}
	}

	/**
	 * This method will call when user update a course.
	 */
	public void refresh() {
		mCoursesList = ReaderModel.getCourses(getActivity());
		mAdapter = new CourseListAdapter(getActivity(), mCoursesList);
		setListAdapter(mAdapter);
		/*
		 * BEFORE: CAUTION!!! This method cannot be call in other situations,
		 * only when a course is updated. mCoursesList =
		 * ReaderModel.getCourses(getActivity());
		 * mAdapter.setCoursesList(mCoursesList); setCourse((Course)
		 * mAdapter.getItem(mAdapter.getSelectedItem()));
		 */
	}

	/**
	 * Override this method to save course identify and selected item in the
	 * list view.
	 * 
	 * This method is needed to management the screen rotate. We need to save
	 * the course identify and selected item on list view (students list).
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_COURSE_ID, mCourseId);
		outState.putLong(
				STATE_SELECTED_ITEM,
				mAdapter.getSelectedItem() == StudentListAdapter.NOTHING_SELECTED ? 0
						: mAdapter.getSelectedItem());
	}

	/**
	 * Set course values.
	 * 
	 * @param course
	 *            The course to show.
	 */
	protected void setCourse(Course course) {
		if (course != null) {
			TextView tvId = (TextView) getActivity().findViewById(
					R.id.course_id);
			TextView tvTitle = (TextView) getActivity().findViewById(
					R.id.course_detail_title);
			EditText etLevel = (EditText) getActivity().findViewById(
					R.id.course_level_input);
			EditText etName = (EditText) getActivity().findViewById(
					R.id.course_name_input);
			EditText etGroup = (EditText) getActivity().findViewById(
					R.id.course_group_input);
			EditText etTutor = (EditText) getActivity().findViewById(
					R.id.course_tutor_input);
			EditText etConceptWeight = (EditText) getActivity().findViewById(
					R.id.course_concept_weight_input);
			EditText etProcedureWeight = (EditText) getActivity().findViewById(
					R.id.course_procedure_weight_input);
			EditText etAttitudeWeight = (EditText) getActivity().findViewById(
					R.id.course_attitude_weight_input);

			tvId.setText(String.valueOf(course.getId()));
			tvTitle.setText(getActivity().getString(
					R.string.course_detail_title));
			etLevel.setText(course.getLevel());
			etName.setText(course.getName());
			etGroup.setText(course.getGroup());
			etTutor.setText(course.getTutor());
			etConceptWeight.setText(String.valueOf(course.getConceptWeight()));
			etProcedureWeight.setText(String.valueOf(course
					.getProcedureWeight()));
			etAttitudeWeight
					.setText(String.valueOf(course.getAttitudeWeight()));

			thereAreCourses();
		} else {
			thereAreNotCourses();
		}
	}

	/**
	 * When there are courses show controls and messages.
	 */
	protected void thereAreCourses() {
		getActivity().findViewById(R.id.course_detail_fields).setVisibility(
				View.VISIBLE);
		getActivity().findViewById(R.id.course_delete_button).setVisibility(
				View.VISIBLE);
		getActivity().findViewById(R.id.course_update_button).setVisibility(
				View.VISIBLE);
	}

	/**
	 * When there are not courses hide controls and show a message.
	 * 
	 * This method do all this job.
	 */
	protected void thereAreNotCourses() {
		// Show the message that inform that there are not courses.
		((TextView) getActivity().findViewById(R.id.course_detail_title))
				.setText(getActivity()
						.getString(R.string.there_are_not_courses));

		// Hide the RelativeLayout where all detail fields are.
		getActivity().findViewById(R.id.course_detail_fields).setVisibility(
				View.GONE);

		// Hide buttons in the button bar below.
		getActivity().findViewById(R.id.course_delete_button).setVisibility(
				View.GONE);
		getActivity().findViewById(R.id.course_update_button).setVisibility(
				View.GONE);
	}

	/**
	 * This method is called when an item in the courses list is clicked.
	 */
	@Override
	public abstract void onListItemClick(ListView listView, View view,
			int position, long id);

	/**
	 * This method must be implemented by subclasses to select programmatically
	 * the first course.
	 */
	public abstract void selectFirstCourse();
}
