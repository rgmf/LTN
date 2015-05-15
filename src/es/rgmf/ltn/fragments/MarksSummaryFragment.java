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

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.orm.AttitudeStudentMark;
import es.rgmf.ltn.model.orm.Course;
import es.rgmf.ltn.model.orm.ExamStudentMark;
import es.rgmf.ltn.model.orm.PracticeStudentMark;
import es.rgmf.ltn.util.Session;

/**
 * This fragment show the information of the Student's marks in a Course in a
 * Evaluation.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class MarksSummaryFragment extends Fragment {
	public static final int TEXT_SIZE_DEFAULT = 0;
	public static final int TEXT_SIZE_SMALL = 1;
	public static final int TEXT_SIZE_MEDIUM = 2;
	public static final int TEXT_SIZE_LARGE = 3;
	public static final int TEXT_SIZE_XLARGE = 4;
	
	private int mStudentId;
	private int mCourseId;
	private int mEvaluationId;
	
	private int mTextSize;
	
	private float mAvgConcepts;
	private float mAvgProcedures;
	private float mAttitudesMark;
	private float mTotalMark;
	
	/**
	 * Returns a new instance of this fragment with student, course
	 * and evaluation id's.
	 */
	public static MarksSummaryFragment newInstance(int studentId,
			int courseId, int evaluationId, int textSize) {
		MarksSummaryFragment fragment = new MarksSummaryFragment(studentId, courseId, evaluationId, textSize);
		return fragment;
	}
	
	/**
	 * Returns a new instance of this fragment with student, course
	 * and evaluation id's.
	 */
	public static MarksSummaryFragment newInstance(int studentId,
			int courseId, int evaluationId) {
		return newInstance(studentId, courseId, evaluationId, TEXT_SIZE_DEFAULT);
	}

	/**
	 * Returns a new instance of this fragment with student, course
	 * and session evaluation information (or the first evaluation).
	 */
	public static MarksSummaryFragment newInstance(int studentId,
			int courseId) {
		if(Session.getEvaluationIndex() != null) {
			return newInstance(
					studentId,
					courseId,
					Session.getEvaluations().get(Session.getEvaluationIndex()).getId(),
					TEXT_SIZE_DEFAULT);
		}
		else {
			return newInstance(
					studentId,
					courseId,
					0,
					TEXT_SIZE_DEFAULT);
		}
	}
	
	/**
	 * Constructor.
	 */
	public MarksSummaryFragment() {
	}

	/**
	 * Constructor.
	 */
	public MarksSummaryFragment(int studentId,
			int courseId, int evaluationId, int textSize) {
		mStudentId = studentId;
		mCourseId = courseId;
		mEvaluationId = evaluationId;
		mTextSize = textSize;
		
		// Get marks.
		ArrayList<ExamStudentMark> exams = ReaderModel
				.getStudentCourseConcepts(getActivity(), mCourseId,
						mStudentId, mEvaluationId);
		ArrayList<PracticeStudentMark> practices = ReaderModel
				.getStudentCourseProcedures(getActivity(), mCourseId,
						mStudentId, mEvaluationId);
		ArrayList<AttitudeStudentMark> attitudes = ReaderModel
				.getStudentCourseAttitudes(getActivity(), mCourseId,
						mStudentId, mEvaluationId);
		Course course = ReaderModel.getCourse(getActivity(),
				String.valueOf(mCourseId));

		// Calculate the averages of marks.
		// Concepts.
		mAvgConcepts = 0f;
		mAvgProcedures = 0f;
		mAttitudesMark = 0f;
		float accMarks = 0f;
		float accWeights = 0f;
		for (int i = 0; i < exams.size(); i++) {
			accMarks += (exams.get(i).getMark() * exams.get(i).getExam()
					.getTest().getWeight());
			accWeights += exams.get(i).getExam().getTest().getWeight();
		}
		if (accWeights != 0)
			mAvgConcepts = (accMarks / accWeights);

		// Procedures.
		accMarks = 0;
		accWeights = 0;
		for (int i = 0; i < practices.size(); i++) {
			accMarks += (practices.get(i).getMark() * practices.get(i)
					.getPractice().getTest().getWeight());
			accWeights += practices.get(i).getPractice().getTest()
					.getWeight();
		}
		if (accWeights != 0)
			mAvgProcedures = (accMarks / accWeights);

		// Attitudes.
		for (int i = 0; i < attitudes.size(); i++) {
			mAttitudesMark += (attitudes.get(i).getAttitude().getWeight());
		}
		mAttitudesMark += 10;
		if (mAttitudesMark < 0)
			mAttitudesMark = 0;
		else if (mAttitudesMark > 10) 
			mAttitudesMark = 10;

		// Load information about marks.
		mTotalMark = 0f;
		if (course != null) {
			mTotalMark = mAvgConcepts * (course.getConceptWeight() / 100)
					+ mAvgProcedures * (course.getProcedureWeight() / 100)
					+ (mAttitudesMark)
					* (course.getAttitudeWeight() / 100);
		}
	}

	/**
	 * Method that is called when this fragment is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_student_course_evaluation_home,
				container, false);
		
		// Setting text values.
		TextView tvConcepts = ((TextView) rootView.findViewById(R.id.concepts_average_mark));
		TextView tvProcedures = ((TextView) rootView.findViewById(R.id.procedures_average_mark));
		TextView tvAttitudes = ((TextView) rootView.findViewById(R.id.attitudes_average_mark));
		TextView tvTotal = ((TextView) rootView.findViewById(R.id.total_mark));
		
		DecimalFormat df = new DecimalFormat("0.00");
		tvConcepts.setText(String.valueOf(df.format(mAvgConcepts)));
		tvProcedures.setText(String.valueOf(df.format(mAvgProcedures)));
		tvAttitudes.setText(String.valueOf(df.format(mAttitudesMark)));
		tvTotal.setText(String.valueOf(df.format(mTotalMark)));
		
		// Setting text sizes.
		switch (mTextSize) {
		case TEXT_SIZE_SMALL:
			tvConcepts.setTextSize(getResources().getDimension(R.dimen.text_size_small));
			tvProcedures.setTextSize(getResources().getDimension(R.dimen.text_size_small));
			tvAttitudes.setTextSize(getResources().getDimension(R.dimen.text_size_small));
			tvTotal.setTextSize(getResources().getDimension(R.dimen.text_size_small));
			((TextView) rootView.findViewById(R.id.title_home_text)).setTextSize(getResources().getDimension(R.dimen.text_size_small));
			((TextView) rootView.findViewById(R.id.concepts_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_small));
			((TextView) rootView.findViewById(R.id.procedures_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_small));
			((TextView) rootView.findViewById(R.id.attitudes_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_small));
			((TextView) rootView.findViewById(R.id.total_mark_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_small));
			break;
		case TEXT_SIZE_MEDIUM:
			tvConcepts.setTextSize(getResources().getDimension(R.dimen.text_size_medium));
			tvProcedures.setTextSize(getResources().getDimension(R.dimen.text_size_medium));
			tvAttitudes.setTextSize(getResources().getDimension(R.dimen.text_size_medium));
			tvTotal.setTextSize(getResources().getDimension(R.dimen.text_size_medium));
			((TextView) rootView.findViewById(R.id.title_home_text)).setTextSize(getResources().getDimension(R.dimen.text_size_medium));
			((TextView) rootView.findViewById(R.id.concepts_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_medium));
			((TextView) rootView.findViewById(R.id.procedures_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_medium));
			((TextView) rootView.findViewById(R.id.attitudes_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_medium));
			((TextView) rootView.findViewById(R.id.total_mark_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_medium));
			break;
		case TEXT_SIZE_LARGE:
			tvConcepts.setTextSize(getResources().getDimension(R.dimen.text_size_large));
			tvProcedures.setTextSize(getResources().getDimension(R.dimen.text_size_large));
			tvAttitudes.setTextSize(getResources().getDimension(R.dimen.text_size_large));
			tvTotal.setTextSize(getResources().getDimension(R.dimen.text_size_large));
			((TextView) rootView.findViewById(R.id.title_home_text)).setTextSize(getResources().getDimension(R.dimen.text_size_large));
			((TextView) rootView.findViewById(R.id.concepts_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_large));
			((TextView) rootView.findViewById(R.id.procedures_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_large));
			((TextView) rootView.findViewById(R.id.attitudes_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_large));
			((TextView) rootView.findViewById(R.id.total_mark_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_large));
			break;
		case TEXT_SIZE_XLARGE:
			tvConcepts.setTextSize(getResources().getDimension(R.dimen.text_size_xlarge));
			tvProcedures.setTextSize(getResources().getDimension(R.dimen.text_size_xlarge));
			tvAttitudes.setTextSize(getResources().getDimension(R.dimen.text_size_xlarge));
			tvTotal.setTextSize(getResources().getDimension(R.dimen.text_size_xlarge));
			((TextView) rootView.findViewById(R.id.title_home_text)).setTextSize(getResources().getDimension(R.dimen.text_size_xlarge));
			((TextView) rootView.findViewById(R.id.concepts_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_xlarge));
			((TextView) rootView.findViewById(R.id.procedures_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_xlarge));
			((TextView) rootView.findViewById(R.id.attitudes_average_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_xlarge));
			((TextView) rootView.findViewById(R.id.total_mark_hint)).setTextSize(getResources().getDimension(R.dimen.text_size_xlarge));
			break;
		}
		
		return rootView;
	}

	/**
	 * When this fragment is attached.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	public float getTotalMark() {
		return mTotalMark;
	}
}
