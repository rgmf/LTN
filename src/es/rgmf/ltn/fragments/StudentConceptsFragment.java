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
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import es.rgmf.ltn.R;
import es.rgmf.ltn.adapters.ConceptMarksListAdapter;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.WriterModel;
import es.rgmf.ltn.model.orm.ExamStudentMark;
import es.rgmf.ltn.util.Dates;
import es.rgmf.ltn.util.Session;

/**
 * This class show concepts marks of the student enrolled in a course.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class StudentConceptsFragment extends StudentMarksFragment {
	/**
	 * The list.
	 */
	private ArrayList<ExamStudentMark> mConceptsList;
	
	/**
	 * Create a new instance of this class.
	 * 
	 * @param studentId the student identifier.
	 * @param courseId the course identifier.
	 * @return the new instance.
	 */
	public static StudentConceptsFragment newInstance(int studentId, int courseId) {
		StudentConceptsFragment fragment = new StudentConceptsFragment();
		fragment.mStudentId = studentId;
		fragment.mCourseId = courseId;
		return fragment;
	}

	/**
	 * Draw and create the view.
	 */
	@Override
	protected void setListFragment() {
		mConceptsList = ReaderModel.getStudentCourseConcepts(mContext, mCourseId, mStudentId);
		mAdapter = new ConceptMarksListAdapter(getActivity(), mConceptsList);
		setListAdapter(mAdapter);
	}

	/**
	 * This method is called when user click on add button in the button bar below and
	 * this fragment is showing.
	 */
	@Override
	protected void onClickAddMarkButton(View v) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.student_framelayout_detail, PlaceholderFragment.newInstance(mStudentId, mCourseId));
		fragmentTransaction.commit();
	}
	
	/**
     * Through this method this Fragment receive events from SetMarkDialogFragment.
     * 
     * In this method will update the mark of the student to the exam and refresh the list.
     */
	@Override
	public void onDialogPositiveClick(float value, String comment) {
		if(mPositionSelected != null) {
			ExamStudentMark obj = (ExamStudentMark) mAdapter.getItem(mPositionSelected);
			if(obj != null) {
				try {
					WriterModel.setExamStudentMark(mContext, obj.getId(), value, comment);
					// It calls this method to reload/refresh the list of concepts.
					setListFragment();
				} catch (Exception e) {
					Log.v("StudentConceptsFragment", "onDialogPositiveClick::" + e.getMessage());
					Toast.makeText(getActivity(), getString(R.string.error_updating_exam_student_mark) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
     * Through this method this Fragment receive events from SetMarkDialogFragment where for all students is activated.
     */
	@Override
	public void onDialogPositiveClickForAllStudents(float value,
			String comment) {
		if(mPositionSelected != null) {
			try {
				ExamStudentMark obj = (ExamStudentMark) mAdapter.getItem(mPositionSelected);
				WriterModel.setExamForAllStudentMark(mContext, mCourseId, obj.getExam().getId(), value, comment);
				// It calls this method to reload/refresh the list of concepts.
				setListFragment();
			} catch (Exception e) {
				Log.v("StudentConceptsFragment", "onDialogPositiveClickForAllStudents::" + e.getMessage());
				Toast.makeText(getActivity(), getString(R.string.error_updating_exam_student_mark) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method is called when user long-click an item of the list and
	 * select delete option.
	 * 
	 * @param position The position where item is in the list.
	 */
	@Override
	protected void deletePositionItem(final int position) {
		new AlertDialog.Builder(getActivity())
		.setTitle(R.string.delete_button_hint)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setMessage(getResources().getString(R.string.concept_delete_message))
		.setCancelable(true).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ExamStudentMark obj = (ExamStudentMark) mAdapter.getItem(position);
				
				// Delete the item from database and if the deletion is ok then delete
				// the item from the list.
				if(WriterModel.deleteExamStudentMark(getActivity(), String.valueOf(obj.getId()))) {
					mConceptsList.remove(obj);
			        mAdapter.notifyDataSetChanged();
				}
				else {
					Toast.makeText(getActivity(), getString(R.string.error_deleting_concept), Toast.LENGTH_LONG).show();
				}
			}
		}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create().show();
	}
	
	/**
	 * This method is called when user long-click an item of the list and
	 * select change option.
	 * 
	 * @param position The position where item is in the list.
	 */
	@Override
	protected void changePositionItem(int position) {
		ExamStudentMark obj = (ExamStudentMark) mAdapter.getItem(position);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.student_framelayout_detail, PlaceholderFragment.newInstance(obj, mStudentId, mCourseId));
		fragmentTransaction.commit();
	}
	
	/**
     * A placeholder fragment containing an add concept view.
     * 
     * Through this fragment it adds and updates concepts.
     */
    public static class PlaceholderFragment extends Fragment {
    	private int mStudentId;
    	private int mCourseId;
    	private ExamStudentMark mExamStudentMark;
    	private View mRootView;
    	
        /**
         * Returns a new instance of this fragment for the given section to add.
         * number.
         */
        public static PlaceholderFragment newInstance(int studentId, int courseId) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.mStudentId = studentId;
            fragment.mCourseId = courseId;
            return fragment;
        }
        
        /**
         * Returns a new instance of this fragment for the given section to change.
         * @param obj
         * @return
         */
        public static Fragment newInstance(ExamStudentMark obj, int studentId, int courseId) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			fragment.mExamStudentMark = obj;
			fragment.mStudentId = studentId;
            fragment.mCourseId = courseId;
			return fragment;
		}

        /**
         * Constructor.
         */
		public PlaceholderFragment() {
			mExamStudentMark = null;
        }

		/**
		 * This method is called when view is created.
		 */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_add_concept, container, false);
            
            // We have to hide the button to create a new concept.
            getActivity().findViewById(R.id.mark_add_button).setVisibility(View.INVISIBLE);
            
            // If there is mExamStudentMark it will update and it shows update button and loads all inputs. 
            // Otherwise it will create and it shows add button.
            if(mExamStudentMark != null) {
	            mRootView.findViewById(R.id.update_concept_button).setVisibility(View.VISIBLE);
	            mRootView.findViewById(R.id.add_concept_button).setVisibility(View.GONE);
	            
	            DatePicker dpDate = (DatePicker) mRootView.findViewById(R.id.concept_date_input);
	            EditText etName = (EditText) mRootView.findViewById(R.id.concept_name_input);
	            EditText etDescription = (EditText) mRootView.findViewById(R.id.concept_description_input);
	            EditText etWeight = (EditText) mRootView.findViewById(R.id.concept_weight_input);
	            
	            Calendar calendar = Dates.getCalendarFromString(mExamStudentMark.getExam().getExamDate());
	            dpDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
	            etName.setText(mExamStudentMark.getExam().getTest().getName());
	            etDescription.setText(mExamStudentMark.getExam().getTest().getDescription());
	            etWeight.setText(String.valueOf(mExamStudentMark.getExam().getTest().getWeight()));
            }
            else {
            	mRootView.findViewById(R.id.update_concept_button).setVisibility(View.GONE);
	            mRootView.findViewById(R.id.add_concept_button).setVisibility(View.VISIBLE);
            }
            
            // When user click on cancel button it closes the form to add a new concept.
            mRootView.findViewById(R.id.cancel_concept_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.student_framelayout_detail, StudentConceptsFragment.newInstance(mStudentId, mCourseId));
					fragmentTransaction.commit();
				}
			});
            
            // When user click on add button it creates the concept.
            mRootView.findViewById(R.id.add_concept_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Create the concept (insert it in the database).
					try {
						WriterModel.addConcept(getActivity(), mCourseId, 
								Session.getEvaluations().get(Session.getEvaluationIndex()).getId(),
								Dates.getStringFromDatePicker(((DatePicker) mRootView.findViewById(R.id.concept_date_input))),
								((EditText) mRootView.findViewById(R.id.concept_name_input)).getText().toString(),
								((EditText) mRootView.findViewById(R.id.concept_description_input)).getText().toString(),
								((EditText) mRootView.findViewById(R.id.concept_weight_input)).getText().toString());
					} catch (Exception e) {
						Log.v("StudentConceptsFragment", "onCreateView::" + e.getMessage());
						Toast.makeText(getActivity(), getString(R.string.error_adding_concept) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
					}
					
					// Show the concept fragment.
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.student_framelayout_detail, StudentConceptsFragment.newInstance(mStudentId, mCourseId));
					fragmentTransaction.commit();
				}
			});
            
            // When user click on change button it changes the concept.
            mRootView.findViewById(R.id.update_concept_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Create the concept (insert it in the database).
					try {
						WriterModel.updateConcept(getActivity(), mExamStudentMark.getExam().getId(),
								Dates.getStringFromDatePicker(((DatePicker) mRootView.findViewById(R.id.concept_date_input))),
								((EditText) mRootView.findViewById(R.id.concept_name_input)).getText().toString(),
								((EditText) mRootView.findViewById(R.id.concept_description_input)).getText().toString(),
								((EditText) mRootView.findViewById(R.id.concept_weight_input)).getText().toString());
					} catch (Exception e) {
						Log.v("StudentConceptsFragment", "onCreateView::" + e.getMessage());
						Toast.makeText(getActivity(), getString(R.string.error_updating_exam_student_mark) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
					}
					
					// Show the concept fragment.
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.student_framelayout_detail, StudentConceptsFragment.newInstance(mStudentId, mCourseId));
					fragmentTransaction.commit();
				}
			});
            
            return mRootView;
        }
    }
}
