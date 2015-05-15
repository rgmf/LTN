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

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.StudentActivity;
import es.rgmf.ltn.model.WriterModel;
import es.rgmf.ltn.model.orm.Student;
import es.rgmf.ltn.util.Session;
import es.rgmf.ltn.util.ui.StudentOptionsContent;

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
		
		// Click listener on Info text button.
		mRootView.findViewById(R.id.student_info_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, StudentActivity.class);
				intent.putExtra(StudentActivity.STUDENT_ID, mStudent.getId());
				intent.putExtra(StudentActivity.COURSE_ID, mCourseId);
				// Select the option to select from StudentActivity Activity.
				intent.putExtra(StudentActivity.OPTION_ID, StudentOptionsContent.PERSONAL_DATA);
		    	startActivity(intent);
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
		// Hide the button to add marks when we are in home fragment.
		mRootView.findViewById(R.id.mark_add_button).setVisibility(
				View.GONE);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		MarksSummaryFragment fragment = MarksSummaryFragment.newInstance(
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
}
