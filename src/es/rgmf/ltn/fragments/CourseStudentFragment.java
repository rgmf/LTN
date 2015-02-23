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

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import es.rgmf.ltn.MainActivity;
import es.rgmf.ltn.R;

/**
 * The main fragment with student course information.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class CourseStudentFragment extends Fragment {
	private static final String STATE_COURSE_ID = "courseId";
	private static final String STATE_SECTION_NUMBER = "sectionNumber";
	
	/**
	 * The View. It can be used to access xml elements of this View.
	 */
	private View mRootView;
	/**
	 * The context.
	 */
	private Context mContext;
	/**
	 * The course id of the students.
	 */
	private int mCourseId;
	/**
	 * The section.
	 */
	private int mSectionNumber;
	/**
	 * The bundle object.
	 */
	private Bundle mSavedInstanceState = null;
	
	/**
	 * Create the course student fragment.
	 * 
	 * @param sectionNumber The section number on the user click.
	 * @return the fragment.
	 */
	public static CourseStudentFragment newInstance(int sectionNumber, int courseId) {
		CourseStudentFragment fragment = new CourseStudentFragment();
		fragment.mCourseId = courseId;
		fragment.mSectionNumber = sectionNumber;
        return fragment;
	}
	
	public CourseStudentFragment() {
	}
	
	/**
	 * Method that is called when the view is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_course_student, container, false);
		
		// If savedInstanceState is not null, then we created the fragments before.
		// With this check we solved the problem when a screen rotate that then recreate
		// all once again.
		this.mSavedInstanceState = savedInstanceState;
		if(savedInstanceState == null) {
			// Load the Course Student List Fragment.
			Fragment fragment1 = CourseStudentListFragment.newInstance(mCourseId);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.course_student_list_fragment, fragment1);
			ft.commit();
			
			// Load the Course Student Detail Fragment.
			Fragment fragment2 = CourseStudentDetailFragment.newInstance();
			ft = fm.beginTransaction();
			ft.replace(R.id.course_student_detail_fragment, fragment2);
			ft.commit();
		}
		else {
			mCourseId = savedInstanceState.getInt(STATE_COURSE_ID);
			mSectionNumber = savedInstanceState.getInt(STATE_SECTION_NUMBER);
		}
		
		if(getActivity() != null) {
			this.mContext = getActivity().getApplicationContext();
		}
		
		return mRootView;
	}
	
	/**
	 * This method is called when fragment is resumed.
	 */
	@Override
	public void onResume() {		
		super.onResume();
		// If savedInstanceState is not null, then we created the fragments before.
		// With this check we solved the problem when a screen rotate that then recreate
		// all once again.
		/*
		if(mSavedInstanceState == null) {
			// Load the Course Student List Fragment.
			Fragment fragment1 = CourseStudentListFragment.newInstance(getArguments().getInt(ARG_COURSE_ID));
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.course_student_list_fragment, fragment1);
			ft.commit();
			
			// Load the Course Student Detail Fragment.
			Fragment fragment2 = CourseStudentDetailFragment.newInstance();
			ft = fm.beginTransaction();
			ft.replace(R.id.course_student_detail_fragment, fragment2);
			ft.commit();
		}
		*/
	}
	
	/**
	 *This method is needed to management the screen rotate. We need to save the course
	 * identify and selected item on list view.
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_COURSE_ID, mCourseId);
		outState.putInt(STATE_SECTION_NUMBER, mSectionNumber);
	}

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(mSectionNumber);
    }

	/**
	 * This method do the same that onResume but not check if mSavedInstanceState is null. 
	 */
	public void refresh() {
		// Load the Course Student List Fragment.
		Fragment fragment1 = CourseStudentListFragment.newInstance(mCourseId);
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.course_student_list_fragment, fragment1);
		ft.commit();
		
		// Load the Course Student Detail Fragment.
		Fragment fragment2 = CourseStudentDetailFragment.newInstance();
		ft = fm.beginTransaction();
		ft.replace(R.id.course_student_detail_fragment, fragment2);
		ft.commit();
	}
}
