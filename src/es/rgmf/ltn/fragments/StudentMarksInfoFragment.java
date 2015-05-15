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

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.StudentActivity;
import es.rgmf.ltn.StudentDetailActivity;
import es.rgmf.ltn.model.ReaderModel;

/**
 * A fragment representing a single Student marks info detail screen. This fragment is
 * either contained in a {@link StudentActivity} in two-pane mode (on tablets)
 * or a {@link StudentDetailActivity} on handsets.
 */
public class StudentMarksInfoFragment extends Fragment {
	private static final String COURSE_ID_STATE = "course_id";
	private static final String STUDENT_ID_STATE = "student_id";
	
	/**
	 * Course identify.
	 */
	private Integer mCourseId;
	/**
	 * Student identify.
	 */
	private Integer mStudentId;
	/**
	 * Evaluation id's list.
	 */
	private ArrayList<Integer> mEvaluationIds = new ArrayList<Integer>();
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public StudentMarksInfoFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			mCourseId = getArguments().getInt(StudentActivity.COURSE_ID);
			mStudentId = getArguments().getInt(StudentActivity.STUDENT_ID);
			mEvaluationIds = ReaderModel.getEvaluationsIds(getActivity());
		}
		else {
			mCourseId = savedInstanceState.getInt(COURSE_ID_STATE);
			mStudentId = savedInstanceState.getInt(STUDENT_ID_STATE);
			mEvaluationIds = ReaderModel.getEvaluationsIds(getActivity());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		float markAcc = 0f;
		int count = 0;
		
		View rootView = inflater.inflate(R.layout.fragment_student_marks_info,
				container, false);
		
		if (savedInstanceState == null) {
			LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.student_marks_info);
			
			for (Integer id : mEvaluationIds) {
				FrameLayout frameLayout = new FrameLayout(getActivity());
				frameLayout.setId(id);
				frameLayout.setPadding(20, 50, 0, 0);
		        
		        linearLayout.addView(frameLayout);
		        
		        FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				MarksSummaryFragment fragment = MarksSummaryFragment.newInstance(
						mStudentId, mCourseId, id, MarksSummaryFragment.TEXT_SIZE_MEDIUM);
				fragmentTransaction.add(id, fragment);
				fragmentTransaction.commit();
				
				count++;
				markAcc += fragment.getTotalMark();
			}
			
			DecimalFormat df = new DecimalFormat("0.00");
			((TextView) rootView.findViewById(R.id.final_mark)).setText(String.valueOf(df.format(markAcc / count)));
		}

		return rootView;
	}
}
