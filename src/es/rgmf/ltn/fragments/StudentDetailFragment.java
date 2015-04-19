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

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import es.rgmf.ltn.R;
import es.rgmf.ltn.StudentActivity;
import es.rgmf.ltn.StudentDetailActivity;

/**
 * A fragment representing a single Student detail screen. This fragment is
 * either contained in a {@link StudentActivity} in two-pane mode (on tablets)
 * or a {@link StudentDetailActivity} on handsets.
 */
public class StudentDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The content this fragment is presenting.
	 */
	private String mItem;
	/**
	 * The student identify.
	 */
	private Integer mStudentId;
	/**
	 * The course identify.
	 */
	private Integer mCourseId;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public StudentDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mItem = getArguments().getString(ARG_ITEM_ID);
		}
		if (getArguments().containsKey(StudentActivity.STUDENT_ID)) {
			mStudentId = getArguments().getInt(StudentActivity.STUDENT_ID);
		}
		if (getArguments().containsKey(StudentActivity.COURSE_ID)) {
			mCourseId = getArguments().getInt(StudentActivity.COURSE_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_detail_main,
				container, false);
		Bundle arguments;
		FragmentTransaction transaction;

		// Change this fragment to fragment specify by mItem (depend on mItem value).
		switch (mItem) {
		case "0":
			StudentPersonalDataFragment pdFragment = new StudentPersonalDataFragment();
			arguments = new Bundle();
			arguments.putInt(StudentActivity.STUDENT_ID, mStudentId);
			pdFragment.setArguments(arguments);
			
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.student_detail_container, pdFragment);
			transaction.addToBackStack(null); // To preserve back button behaviour.
			transaction.commit();
			break;
		case "1":
			StudentAttendanceFragment aFragment = new StudentAttendanceFragment();
			arguments = new Bundle();
			arguments.putInt(StudentActivity.STUDENT_ID, mStudentId);
			arguments.putInt(StudentActivity.COURSE_ID, mCourseId);
			aFragment.setArguments(arguments);
			
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.student_detail_container, aFragment);
			transaction.addToBackStack(null); // To preserve back button behaviour.
			transaction.commit();
			break;
		}

		return rootView;
	}
}
