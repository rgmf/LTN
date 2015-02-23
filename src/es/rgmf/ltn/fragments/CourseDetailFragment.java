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
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.WriterModel;
import es.rgmf.ltn.model.orm.Course;

/**
 * The fragment with a course detail.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class CourseDetailFragment extends Fragment {
	/**
	 * The View. It can be used to access xml elements of this View.
	 */
	private View mRootView;
	/**
	 * The context.
	 */
	private Context mContext;
	
	/**
	 * Method that is called when the view is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_course_detail, container, false);

		if(getActivity() != null) {
			this.mContext = getActivity().getApplicationContext();
		}
		
		/******************* Delete button click event ***********************/
		((ImageView) mRootView.findViewById(R.id.course_delete_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mRootView.getContext())
				.setTitle(R.string.delete_button_hint)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(getResources().getString(R.string.course_delete_message))
				.setCancelable(true).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// We try to delete the course and if the course is correctly deleted then
						// we refresh data of CoursesListFragment.
						String id = ((TextView) mRootView.findViewById(R.id.course_id)).getText().toString();
						if(WriterModel.deleteCourse(mContext, id)) {
							CoursesListFragment fragment = (CoursesListFragment) getFragmentManager()
									.findFragmentById(R.id.courses_list_fragment);
							if(fragment != null) {
								fragment.refresh();
								fragment.selectFirstCourse();
							}
						}
					}
				}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create().show();
			}
		});
		/*********************************************************************/
		
		/******************* Update button click event ***********************/
		((ImageView) mRootView.findViewById(R.id.course_update_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView tvId = (TextView) getActivity().findViewById(R.id.course_id);
				EditText etLevel = (EditText) mRootView.findViewById(R.id.course_level_input);
				EditText etName = (EditText) mRootView.findViewById(R.id.course_name_input);
				EditText etGroup = (EditText) mRootView.findViewById(R.id.course_group_input);
				EditText etTutor = (EditText) mRootView.findViewById(R.id.course_tutor_input);
				EditText etConceptWeight = (EditText) mRootView.findViewById(R.id.course_concept_weight_input);
				EditText etProcedureWeight = (EditText) mRootView.findViewById(R.id.course_procedure_weight_input);
				EditText etAttitudeWeight = (EditText) mRootView.findViewById(R.id.course_attitude_weight_input);
				TextView tvPercentagesSum = (TextView) mRootView.findViewById(R.id.course_weightes_total_text);
				Boolean errors = false;
				float percentagesSum = 0f;
				
				if(etLevel.getText().toString().length() == 0) {
					etLevel.setError(getString(R.string.error_not_null));
					errors = true;
				}
				else {
					etLevel.setError(null);
				}
				
				if(etName.getText().toString().length() == 0) {
					etName.setError(getString(R.string.error_not_null));
					errors = true;
				}
				else {
					etName.setError(null);
				}
				
				if(etGroup.getText().toString().length() == 0) {
					etGroup.setError(getString(R.string.error_not_null));
					errors = true;
				}
				else {
					etGroup.setError(null);
				}
				
				if(etConceptWeight.getText().toString().length() == 0) {
					etConceptWeight.setError(getString(R.string.error_not_null));
					errors = true;
				}
				else {
					percentagesSum += Float.valueOf(etConceptWeight.getText().toString());
					etConceptWeight.setError(null);
				}
				
				if(etProcedureWeight.getText().toString().length() == 0) {
					etProcedureWeight.setError(getString(R.string.error_not_null));
					errors = true;
				}
				else {
					percentagesSum += Float.valueOf(etProcedureWeight.getText().toString());
					etProcedureWeight.setError(null);
				}
				
				if(etAttitudeWeight.getText().toString().length() == 0) {
					etAttitudeWeight.setError(getString(R.string.error_not_null));
					errors = true;
				}
				else {
					percentagesSum += Float.valueOf(etAttitudeWeight.getText().toString());
					etAttitudeWeight.setError(null);
				}
				
				if(percentagesSum < 99.9 || percentagesSum > 100.0) {
					tvPercentagesSum.setError(getString(R.string.error_sum_percentages));
					errors = true;
				}
				else {
					tvPercentagesSum.setError(null);
				}
				
				if(errors) {
					return;
				}
				
				// Create the course.
				Course course;
				course = new Course(
						mContext,
						tvId.getText().toString(),
						etLevel.getText().toString(),
						etName.getText().toString(),
						etGroup.getText().toString(),
						etTutor.getText().toString(),
						Float.valueOf(etConceptWeight.getText().toString()),
						Float.valueOf(etProcedureWeight.getText().toString()),
						Float.valueOf(etAttitudeWeight.getText().toString()));
				WriterModel.updateCourse(mContext, course);
				Toast.makeText(mContext, R.string.course_update_ok, Toast.LENGTH_LONG).show();
				CoursesListFragment fragment = (CoursesListFragment) getFragmentManager()
						.findFragmentById(R.id.courses_list_fragment);
				if(fragment != null) {
					fragment.refresh();
					fragment.selectFirstCourse();
				}
			}
		});
		/*********************************************************************/
		
		return mRootView;
	}
}
