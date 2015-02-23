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
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.WriterModel;
import es.rgmf.ltn.model.orm.AttitudeStudentMark;
import es.rgmf.ltn.model.orm.Course;
import es.rgmf.ltn.model.orm.ExamStudentMark;
import es.rgmf.ltn.model.orm.PracticeStudentMark;
import es.rgmf.ltn.model.orm.Student;
import es.rgmf.ltn.util.Session;

/**
 * This fragment show the information of the Student in a Course in a
 * Evaluation.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class StudentCourseEvaluationFragment extends Fragment {
	public static final int HOME = 0;
	public static final int CONCEPT = 1;
	public static final int PROCEDURE = 2;
	public static final int ATTITUDE = 3;
	
	/**
	 * The fragment selected.
	 */
	private static int mFragmentSelected;
	/**
	 * The application context.
	 */
	private Context mContext;
	/**
	 * The student object.
	 */
	private static Student mStudent;
	/**
	 * The identifier of the course where student is matriculate.
	 */
	private static int mCourseId;
	/**
	 * The root view.
	 */
	private View mRootView;

	/**
	 * Create a instance of this class.
	 * 
	 * @param student
	 *            The student object.
	 * @param courseId
	 *            The course id where student is.
	 * @return The StudentFragment object created.
	 */
	public static StudentCourseEvaluationFragment newInstance(Student student,
			int courseId) {
		StudentCourseEvaluationFragment fragment = new StudentCourseEvaluationFragment();
		mStudent = student;
		mCourseId = courseId;
		return fragment;
	}

	/**
	 * Method that is called when the view is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		if (getActivity() != null) {
			this.mContext = getActivity().getApplicationContext();
		}

		mRootView = inflater.inflate(
				R.layout.fragment_student_course_evaluation, container, false);
		
		mFragmentSelected = Session.getCourseFragmentSelected(mCourseId);

		TextView tvId = (TextView) mRootView.findViewById(R.id.student_id_text);
		TextView tvName = (TextView) mRootView
				.findViewById(R.id.student_name_text);
		TextView tvLastname = (TextView) mRootView
				.findViewById(R.id.student_lastname_text);

		tvId.setText(String.valueOf(mStudent.getId()));
		tvName.setText(mStudent.getName());
		tvLastname.setText(mStudent.getLastname());

		// Click listener on delete button.
		mRootView.findViewById(R.id.student_del_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(mRootView.getContext())
								.setTitle(R.string.delete_button_hint)
								.setIcon(android.R.drawable.ic_dialog_alert)
								.setMessage(
										getResources()
												.getString(
														R.string.student_delete_message))
								.setCancelable(true)
								.setPositiveButton(android.R.string.yes,
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// We try to delete the
												// course and if the course
												// is correctly deleted then
												// we refresh data of
												// CourseStudentFragment.
												String id = ((TextView) mRootView
														.findViewById(R.id.student_id_text))
														.getText().toString();
												if (WriterModel.deleteStudent(
														mContext, id)) {
													CourseStudentFragment fragment = (CourseStudentFragment) getFragmentManager()
															.findFragmentById(
																	R.id.container);
													if (fragment != null) {
														fragment.refresh();
													}
												}
											}
										})
								.setNegativeButton(android.R.string.no,
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
											}
										}).create().show();
					}
				});

		// Click listener on update button.
		mRootView.findViewById(R.id.student_update_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// Load the Student Update Fragment.
						FragmentManager fm = getFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Fragment fragment = UpdateCourseStudentFragment
								.newInstance(mStudent, mCourseId);
						ft.replace(R.id.course_student_detail_fragment,
								fragment);
						ft.commit();
					}
				});

		if (savedInstanceState == null) {
			// If the user has created evaluations and selects one it configures
			// the concepts, procedures and attitudes button. Otherwise it shows
			// the advice.
			if (Session.getEvaluationIndex() != null) {
				setMarksButtons();
				
				/*FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				PlaceholderFragment fragment = PlaceholderFragment.newInstance(
						mStudent.getId(), mCourseId);
				fragmentTransaction.replace(R.id.student_framelayout_detail,
						(Fragment) fragment);
				fragmentTransaction.commit();*/
				switch (mFragmentSelected) {
				case CONCEPT:
					loadConceptFragment();
					break;
				case PROCEDURE:
					loadProcedureFragment();
					break;
				case ATTITUDE:
					loadAttitudeFragment();
					break;
				case HOME:
				default:
					loadHomeFragment();
					break;
				}
				
				mRootView.findViewById(R.id.not_evaluation_message)
						.setVisibility(View.GONE);
			}
		} else {
			switch (mFragmentSelected) {
			case CONCEPT:
				loadConceptFragment();
				break;
			case PROCEDURE:
				loadProcedureFragment();
				break;
			case ATTITUDE:
				loadAttitudeFragment();
				break;
			case HOME:
			default:
				loadHomeFragment();
				break;
			}
		}

		return mRootView;
	}

	/**
	 * On save instance state.
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	private void loadHomeFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		PlaceholderFragment fragment = PlaceholderFragment.newInstance(
				mStudent.getId(), mCourseId);
		fragmentTransaction.replace(
				R.id.student_framelayout_detail,
				(Fragment) fragment);
		mRootView.findViewById(R.id.button_home_text)
				.setVisibility(View.VISIBLE);
		mRootView.findViewById(R.id.button_concepts_text)
				.setVisibility(View.GONE);
		mRootView.findViewById(R.id.button_procedures_text)
				.setVisibility(View.GONE);
		mRootView.findViewById(R.id.button_attitudes_text)
				.setVisibility(View.GONE);
		fragmentTransaction.commit();
	}
	
	private void loadConceptFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		StudentConceptsFragment fragment = StudentConceptsFragment.newInstance(
				mStudent.getId(), mCourseId);
		fragmentTransaction.replace(
				R.id.student_framelayout_detail,
				(Fragment) fragment);
		mRootView.findViewById(R.id.button_home_text)
				.setVisibility(View.GONE);
		mRootView.findViewById(R.id.button_concepts_text)
				.setVisibility(View.VISIBLE);
		mRootView.findViewById(R.id.button_procedures_text)
				.setVisibility(View.GONE);
		mRootView.findViewById(R.id.button_attitudes_text)
				.setVisibility(View.GONE);
		fragmentTransaction.commit();
	}
	
	private void loadProcedureFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		StudentProceduresFragment fragment = StudentProceduresFragment
				.newInstance(mStudent.getId(), mCourseId);
		fragmentTransaction.replace(
				R.id.student_framelayout_detail,
				(Fragment) fragment);
		mRootView.findViewById(R.id.button_home_text)
				.setVisibility(View.GONE);
		mRootView.findViewById(R.id.button_concepts_text)
				.setVisibility(View.GONE);
		mRootView.findViewById(R.id.button_procedures_text)
				.setVisibility(View.VISIBLE);
		mRootView.findViewById(R.id.button_attitudes_text)
				.setVisibility(View.GONE);
		fragmentTransaction.commit();
	}
	
	private void loadAttitudeFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		StudentAttitudesFragment fragment = StudentAttitudesFragment.newInstance(
				mStudent.getId(), mCourseId);
		fragmentTransaction.replace(
				R.id.student_framelayout_detail,
				(Fragment) fragment);
		mRootView.findViewById(R.id.button_home_text)
				.setVisibility(View.GONE);
		mRootView.findViewById(R.id.button_concepts_text)
				.setVisibility(View.GONE);
		mRootView.findViewById(R.id.button_procedures_text)
				.setVisibility(View.GONE);
		mRootView.findViewById(R.id.button_attitudes_text)
				.setVisibility(View.VISIBLE);
		fragmentTransaction.commit();
	}

	/**
	 * Set Concepts, Procedures and Attitudes buttons.
	 */
	private void setMarksButtons() {
		/**
		 * When home tab button is clicked is call this method.
		 */
		mRootView.findViewById(R.id.button_home).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						mFragmentSelected = HOME;
						Session.setCourseFragmentSelected(mCourseId, HOME);
						loadHomeFragment();
					}
				});

		/**
		 * When concepts tab button is clicked is call this method.
		 */
		mRootView.findViewById(R.id.button_concepts).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mFragmentSelected = CONCEPT;
						Session.setCourseFragmentSelected(mCourseId, CONCEPT);
						loadConceptFragment();
					}
				});

		/**
		 * When procedures tab button is clicked is call this method.
		 */
		mRootView.findViewById(R.id.button_procedures).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mFragmentSelected = PROCEDURE;
						Session.setCourseFragmentSelected(mCourseId, PROCEDURE);
						loadProcedureFragment();
					}
				});

		/**
		 * When attitudes tab button is clicked is call this method.
		 */
		mRootView.findViewById(R.id.button_attitudes).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mFragmentSelected = ATTITUDE;
						Session.setCourseFragmentSelected(mCourseId, ATTITUDE);
						loadAttitudeFragment();
					}
				});
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private int mStudentId;
		private int mCourseId;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int studentId,
				int courseId) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			fragment.mStudentId = studentId;
			fragment.mCourseId = courseId;
			return fragment;
		}

		/**
		 * Constructor.
		 */
		public PlaceholderFragment() {
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
			// Hide the button to add marks when we are in home fragment.
			getActivity().findViewById(R.id.mark_add_button).setVisibility(
					View.GONE);

			// Get marks.
			ArrayList<ExamStudentMark> exams = ReaderModel
					.getStudentCourseConcepts(getActivity(), mCourseId,
							mStudentId);
			ArrayList<PracticeStudentMark> practices = ReaderModel
					.getStudentCourseProcedures(getActivity(), mCourseId,
							mStudentId);
			ArrayList<AttitudeStudentMark> attitudes = ReaderModel
					.getStudentCourseAttitudes(getActivity(), mCourseId,
							mStudentId);
			Course course = ReaderModel.getCourse(getActivity(),
					String.valueOf(mCourseId));

			// Calculate the averages of marks.
			// Concepts.
			float avgConcepts = 0f;
			float avgProcedures = 0f;
			float attitudesMark = 0f;
			float accMarks = 0f;
			float accWeights = 0f;
			for (int i = 0; i < exams.size(); i++) {
				accMarks += (exams.get(i).getMark() * exams.get(i).getExam()
						.getTest().getWeight());
				accWeights += exams.get(i).getExam().getTest().getWeight();
			}
			if (accWeights != 0)
				avgConcepts = (accMarks / accWeights);

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
				avgProcedures = (accMarks / accWeights);

			// Attitudes.
			for (int i = 0; i < attitudes.size(); i++) {
				attitudesMark += (attitudes.get(i).getAttitude().getWeight());
			}

			// Load information about marks.
			DecimalFormat df = new DecimalFormat("0.00");
			float totalMark = 0f;
			if (course != null) {
				totalMark = avgConcepts * (course.getConceptWeight() / 100)
						+ avgProcedures * (course.getProcedureWeight() / 100)
						+ (10 + attitudesMark)
						* (course.getAttitudeWeight() / 100);
			}
			((TextView) rootView.findViewById(R.id.concepts_average_mark))
					.setText(String.valueOf(df.format(avgConcepts)));
			((TextView) rootView.findViewById(R.id.procedures_average_mark))
					.setText(String.valueOf(df.format(avgProcedures)));
			((TextView) rootView.findViewById(R.id.attitudes_average_mark))
					.setText(String.valueOf(df.format(attitudesMark)));
			((TextView) rootView.findViewById(R.id.total_mark)).setText(String
					.valueOf(df.format(totalMark)));

			return rootView;
		}

		/**
		 * When this fragment is attached.
		 */
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
		}
	}
}
