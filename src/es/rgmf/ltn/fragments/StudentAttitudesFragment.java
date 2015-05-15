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
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import es.rgmf.ltn.R;
import es.rgmf.ltn.adapters.AttitudeMarksListAdapter;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.WriterModel;
import es.rgmf.ltn.model.orm.AttitudeStudentMark;
import es.rgmf.ltn.util.Dates;
import es.rgmf.ltn.util.Session;

/**
 * This class show attitudes marks of the student enrolled in a course.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class StudentAttitudesFragment extends StudentMarksFragment {
	/**
	 * The adapter.
	 */
	private AttitudeMarksListAdapter mAdapter;
	/**
	 * The list.
	 */
	private ArrayList<AttitudeStudentMark> mAttitudesList;
	/**
	 * The attitude identify that user could select.
	 */
	private static Integer mAttitudeId = null;
	
	/**
	 * Create a new instance of this class.
	 * 
	 * @param studentId the student identifier.
	 * @param courseId the course identifier.
	 * @return the new instance.
	 */
	public static StudentAttitudesFragment newInstance(int studentId, int courseId) {
		StudentAttitudesFragment fragment = new StudentAttitudesFragment();
		fragment.mStudentId = studentId;
		fragment.mCourseId = courseId;
		return fragment;
	}

	/**
	 * Set this ListFragment.
	 */
	@Override
	protected void setListFragment() {
		mAttitudesList = ReaderModel.getStudentCourseAttitudes(mContext, mCourseId, mStudentId, mEvaluationId);
		mAdapter = new AttitudeMarksListAdapter(getActivity(), mAttitudesList);
		setListAdapter(mAdapter);
	}
	
	@Override
	protected void onClickAddMarkButton(View v) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.student_framelayout_detail, PlaceholderFragment.newInstance(mStudentId, mCourseId));
		fragmentTransaction.commit();
	}
	
	/**
	 * To respond to on click event on items in the list.
	 */
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
      mPositionSelected = pos;
	  //Toast.makeText(getActivity(), "Item " + pos + " was clicked", Toast.LENGTH_SHORT).show();
	  changePositionItem(pos);
	}
	
	/**
     * Through this method this Fragment receive events from SetMarkDialogFragment.
     */
	@Override
	public void onDialogPositiveClick(float mark, String comment) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDialogPositiveClickForAllStudents(float vlaue,
			String strComment) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This method is called when user long-click an item of the list and
	 * select change option.
	 * 
	 * @param position The position where item is in the list.
	 */
	@Override
	protected void changePositionItem(int position) {
		AttitudeStudentMark obj = (AttitudeStudentMark) mAdapter.getItem(position);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.student_framelayout_detail, PlaceholderFragment.newInstance(obj, mStudentId, mCourseId));
		fragmentTransaction.commit();
	}
	
	/**
     * A placeholder fragment containing an add concept view.
     */
    public static class PlaceholderFragment extends Fragment {
    	private AttitudeStudentMark mAttitudeStudentMark;
    	private int mStudentId;
    	private int mCourseId;
    	private View mRootView;

    	
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int studentId, int courseId) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.mStudentId = studentId;
            fragment.mCourseId = courseId;
            return fragment;
        }
        
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(AttitudeStudentMark obj, int studentId, int courseId) {
        	 PlaceholderFragment fragment = new PlaceholderFragment();
             fragment.mStudentId = studentId;
             fragment.mCourseId = courseId;
             fragment.mAttitudeStudentMark = obj;
             return fragment;
		}

        /**
         * Constructor.
         */
		public PlaceholderFragment() {
			mAttitudeStudentMark = null;
        }

        /**
         * Create this view.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {        	
            mRootView = inflater.inflate(R.layout.fragment_add_attitude, container, false);
            
            // We have to hide the button to create a new attitude.
            getActivity().findViewById(R.id.mark_add_button).setVisibility(View.INVISIBLE);
            
            // Populate spinner with all attitudes.
            Spinner sAllAttitude = (Spinner) mRootView.findViewById(R.id.all_attitude_spinner);
            SimpleCursorAdapter adapter = ReaderModel.getAllAttitudeAdapter(getActivity());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sAllAttitude.setAdapter(adapter);
            sAllAttitude.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            	/**
            	 * When user selected and item in the spinner.
            	 */
				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
						int position, long id) {
					TextView tvName = (TextView) getActivity().findViewById(R.id.attitude_name_input);
					TextView tvDesc = (TextView) getActivity().findViewById(R.id.attitude_description_input);
					
					switch(position) {
					case 0: /* This is the first item with prompt */
						mAttitudeId = null;
						tvName.setEnabled(true);
						tvDesc.setEnabled(true);
						break;
					default:
						mAttitudeId = (int) id;
						Cursor cursor = (Cursor) parentView.getItemAtPosition(position);
						mAttitudeId = cursor.getInt(0);
						tvName.setText(cursor.getString(1));
						tvDesc.setText(cursor.getString(2));
						tvName.setEnabled(false);
						tvDesc.setEnabled(false);
						break;
					}
				}

				/**
				 * When user not selected anything.
				 */
				@Override
				public void onNothingSelected(AdapterView<?> parentView) {
					mAttitudeId = null;
				}
            	
			});
            
            // If there is mAttitudeStudentMark it will update and it shows update button and loads all inputs. 
            // Otherwise it will create and it shows add button.
            if(mAttitudeStudentMark != null) {
	            mRootView.findViewById(R.id.update_attitude_button).setVisibility(View.VISIBLE);
	            mRootView.findViewById(R.id.add_attitude_button).setVisibility(View.GONE);
	            
	            DatePicker dpDate = (DatePicker) mRootView.findViewById(R.id.attitude_date_input);
	            EditText etName = (EditText) mRootView.findViewById(R.id.attitude_name_input);
	            EditText etDescription = (EditText) mRootView.findViewById(R.id.attitude_description_input);
	            EditText etWeight = (EditText) mRootView.findViewById(R.id.attitude_weight_input);
	            
	            Calendar calendar = Dates.getCalendarFromString(mAttitudeStudentMark.getAttitudeDate());
	            dpDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
	            etName.setText(mAttitudeStudentMark.getAttitude().getAllAttitude().getName());
	            etDescription.setText(mAttitudeStudentMark.getAttitude().getAllAttitude().getDescription());
	            etWeight.setText(String.valueOf(mAttitudeStudentMark.getAttitude().getWeight()));
	            
	            for(int i = 0; i < adapter.getCount(); i++) {
	            	if(adapter.getItemId(i) == mAttitudeStudentMark.getAttitude().getAllAttitude().getId()) {
	            		sAllAttitude.setSelection(i);
	            		break;
	            	}
	            }
            }
            else {
            	mRootView.findViewById(R.id.update_attitude_button).setVisibility(View.GONE);
	            mRootView.findViewById(R.id.add_attitude_button).setVisibility(View.VISIBLE);
            }

            // When user click on cancel button it closes the form to add a new concept.
            mRootView.findViewById(R.id.cancel_attitude_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.student_framelayout_detail, StudentAttitudesFragment.newInstance(mStudentId, mCourseId));
					fragmentTransaction.commit();
				}
			});
            
            // When user click on add button it creates the attitude.
            mRootView.findViewById(R.id.add_attitude_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Create the concept (insert it in the database).
					try {
						if(mAttitudeId == null) {
							WriterModel.addAttitude(getActivity(), mCourseId, mStudentId,
									Session.getEvaluations().get(Session.getEvaluationIndex()).getId(),
									Dates.getStringFromDatePicker(((DatePicker) mRootView.findViewById(R.id.attitude_date_input))),
									((EditText) mRootView.findViewById(R.id.attitude_name_input)).getText().toString(),
									((EditText) mRootView.findViewById(R.id.attitude_description_input)).getText().toString(),
									((EditText) mRootView.findViewById(R.id.attitude_weight_input)).getText().toString());
						}
						else {
							WriterModel.addAttitude(getActivity(), mCourseId, mStudentId, mAttitudeId,
									Session.getEvaluations().get(Session.getEvaluationIndex()).getId(),
									Dates.getStringFromDatePicker(((DatePicker) mRootView.findViewById(R.id.attitude_date_input))),
									((EditText) mRootView.findViewById(R.id.attitude_name_input)).getText().toString(),
									((EditText) mRootView.findViewById(R.id.attitude_description_input)).getText().toString(),
									((EditText) mRootView.findViewById(R.id.attitude_weight_input)).getText().toString());
						}
					} catch (Exception e) {
						Log.v("StudentAttitudesFragment", "onCreateView::" + e.getMessage());
						Toast.makeText(getActivity(), getString(R.string.error_adding_attitude) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
					}
					
					// Show the concept fragment.
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.student_framelayout_detail, StudentAttitudesFragment.newInstance(mStudentId, mCourseId));
					fragmentTransaction.commit();
				}
			});
            
            // When user click on update button it updates the attitude.
            mRootView.findViewById(R.id.update_attitude_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Create the concept (insert it in the database).
					try {
						if(mAttitudeId == null) {
							WriterModel.updateAttitude(getActivity(),
									mAttitudeStudentMark.getAttitude().getId(),
									mAttitudeStudentMark.getId(),
									Dates.getStringFromDatePicker(((DatePicker) mRootView.findViewById(R.id.attitude_date_input))),
									((EditText) mRootView.findViewById(R.id.attitude_name_input)).getText().toString(),
									((EditText) mRootView.findViewById(R.id.attitude_description_input)).getText().toString(),
									((EditText) mRootView.findViewById(R.id.attitude_weight_input)).getText().toString());
						}
						else {
							WriterModel.updateAttitude(getActivity(),
									mAttitudeStudentMark.getAttitude().getId(),
									mAttitudeId,
									mAttitudeStudentMark.getId(),
									Dates.getStringFromDatePicker(((DatePicker) mRootView.findViewById(R.id.attitude_date_input))),
									((EditText) mRootView.findViewById(R.id.attitude_weight_input)).getText().toString());
						}
					} catch (Exception e) {
						Log.v("StudentAttitudesFragment", "onCreateView::" + e.getMessage());
						Toast.makeText(getActivity(), getString(R.string.error_updating_attitude) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
					}
					
					// Show the concept fragment.
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.student_framelayout_detail, StudentAttitudesFragment.newInstance(mStudentId, mCourseId));
					fragmentTransaction.commit();
				}
			});
            
            return mRootView;
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
		.setMessage(getResources().getString(R.string.attitude_delete_message))
		.setCancelable(true).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AttitudeStudentMark obj = (AttitudeStudentMark) mAdapter.getItem(position);
				
				// Delete the item from database and if the deletion is ok then delete
				// the item from the list.
				if(WriterModel.deleteAttitudeStudentMark(getActivity(), String.valueOf(obj.getId()))) {
					mAttitudesList.remove(obj);
			        mAdapter.notifyDataSetChanged();
				}
				else {
					Toast.makeText(getActivity(), getString(R.string.error_deleting_attitude), Toast.LENGTH_LONG).show();
				}
			}
		}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create().show();
	}
}
