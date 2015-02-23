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

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import es.rgmf.ltn.R;
import es.rgmf.ltn.adapters.StudentListAdapter;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.orm.Student;

/**
 * The fragment with the list of courses.
 * 
 * This fragment communicate to {@link CourseDetailFragment.java} fragment
 * to inflate the values of the course selected int this list fragment.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class CourseStudentListFragment extends ListFragment {
	private static final String STATE_COURSE_ID = "courseId";
	private static final String STATE_SELECTED_ITEM = "selectedItem";
	
    /**
     * The application context.
     */
    private Context mContext;
	/**
	 * The list view that need to be inflate.
	 */
	private ListView mListView;
	/**
	 * The list of courses that will be showed in the list view.
	 */
	private ArrayList<Student> mStudentsList;
	/**
	 * The adapter.
	 */
	private StudentListAdapter mAdapter;
	/**
	 * The course id.
	 */
	private int mCourseId;
	
	/**
	 * Create the course student list fragment.
	 * 
	 * @param courseId The section number on 
	 * @return the fragment.
	 */
	public static CourseStudentListFragment newInstance(int courseId) {
		CourseStudentListFragment fragment = new CourseStudentListFragment();
		fragment.mCourseId = courseId;
        return fragment;
	}
	
	public CourseStudentListFragment() {
	}
	
	/**
	 * This method is called when this fragment is created.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		/********************* The rotate screen management ************************/
		if(savedInstanceState == null) {
			mStudentsList = ReaderModel.getCourseStudents(getActivity(), mCourseId);
			mAdapter = new StudentListAdapter(getActivity(), mStudentsList);
			setListAdapter(mAdapter);
			
			if(mAdapter.getCount() > 0) {
	        	Object student = mAdapter.getItem(0);
	        	if(student instanceof Student) {
	    			StudentCourseEvaluationFragment studentFragment = StudentCourseEvaluationFragment.newInstance((Student)student, mCourseId);
	    			FragmentManager fm = getFragmentManager();
	    			FragmentTransaction ft = fm.beginTransaction();
	    			ft.replace(R.id.course_student_detail_fragment, studentFragment);
	    			ft.commit();
	    		}
	        }
		}
		else {
			// Restore the data.
			mCourseId = savedInstanceState.getInt(STATE_COURSE_ID);
			int selectedItem = (int) savedInstanceState.getLong(STATE_SELECTED_ITEM);
			mStudentsList = ReaderModel.getCourseStudents(getActivity(), mCourseId);
			mAdapter = new StudentListAdapter(getActivity(), mStudentsList);
			setListAdapter(mAdapter);
			
			// If there are students we show them replacing the default view.
			if(mAdapter.getCount() > 0) {
	        	Object student = mAdapter.getItem(selectedItem);
	        	mAdapter.setSelectedItem(selectedItem);
	        	if(student instanceof Student) {
	    			StudentCourseEvaluationFragment studentFragment = StudentCourseEvaluationFragment.newInstance((Student)student, mCourseId);
	    			FragmentManager fm = getFragmentManager();
	    			FragmentTransaction ft = fm.beginTransaction();
	    			ft.replace(R.id.course_student_detail_fragment, studentFragment);
	    			ft.commit();
	    		}
	        }
		}
		/***************************************************************************/
		
        mContext = getActivity();
        
        mListView = getListView();
		
		/****************** Add student button click event *******************/
        ImageView ivAddStudent = (ImageView) getActivity().findViewById(R.id.student_add_button);
        if(ivAddStudent != null) {
			ivAddStudent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Load the Course Student Detail Fragment.
					Fragment fragment = AddCourseStudentFragment.newInstance(mCourseId);
					FragmentManager fm = getFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					ft = fm.beginTransaction();
					ft.replace(R.id.course_student_detail_fragment, fragment);
					ft.commit();
				}
			});
        }
		/*********************************************************************/
	}
	
	/**
	 * Method called when the fragment is resumed.
	 */
	@Override
	public void onResume() {
		super.onResume();
	}
	
	/**
	 * Override this method to save course identify and selected item in the list view.
	 * 
	 * This method is needed to management the screen rotate. We need to save the course
	 * identify and selected item on list view (students list).
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_COURSE_ID, mCourseId);
		outState.putLong(STATE_SELECTED_ITEM, mAdapter.getSelectedItem() == StudentListAdapter.NOTHING_SELECTED ? 0 : mAdapter.getSelectedItem());
	}

	/**
	 * This method is called when an item in the course student list is clicked.
	 * 
	 * All we need to do is putting the fragment into the detail fragment.
	 */
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		Object student = mAdapter.getItem(position);
		int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = position - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if(wantedChild < 0 || wantedChild >= listView.getChildCount()) {
			Toast.makeText(mContext, getString(R.string.error_selecting_item), Toast.LENGTH_LONG).show();
			return;
		}
		
		// Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
		mAdapter.setSelectedItem(wantedChild, position);
		
		if(student instanceof Student) {
			StudentCourseEvaluationFragment studentFragment = StudentCourseEvaluationFragment.newInstance((Student)student, mCourseId);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.course_student_detail_fragment, studentFragment);
			ft.commit();
		}
	}
}
