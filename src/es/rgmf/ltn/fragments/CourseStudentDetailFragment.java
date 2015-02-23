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
import android.widget.Button;
import android.widget.EditText;
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
public class CourseStudentDetailFragment extends Fragment {
	/**
	 * The View. It can be used to access xml elements of this View.
	 */
	private View rootView;
	/**
	 * The context.
	 */
	private Context context;
	
	/**
	 * Create an instance of this fragment.
	 * 
	 * @return The fragment created.
	 */
	public static CourseStudentDetailFragment newInstance() {
		return new CourseStudentDetailFragment();
	}
	
	/**
	 * Method that is called when the view is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		rootView = inflater.inflate(R.layout.fragment_course_student_detail, container, false);

		if(getActivity() != null) {
			this.context = getActivity().getApplicationContext();
		}
		
		Button deleteButton = ((Button) rootView.findViewById(R.id.course_delete_button));
		Button updateButton = ((Button) rootView.findViewById(R.id.course_update_button));
		
		/******************* Delete button click event ***********************/
		if(deleteButton != null) {
			deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(rootView.getContext())
					.setTitle(R.string.delete_button_hint)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setMessage(getResources().getString(R.string.course_delete_message))
					.setCancelable(true).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// We try to delete the course and if the course is correctly deleted then
							// we refresh data of CoursesListFragment.
							String id = ((TextView) rootView.findViewById(R.id.course_id)).getText().toString();
							if(WriterModel.deleteCourse(context, id)) {
								CoursesListFragment fragment = (CoursesListFragment) getFragmentManager()
										.findFragmentById(R.id.courses_list_fragment);
								if(fragment != null) {
									fragment.onResume();
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
		}
		/*********************************************************************/
		
		/******************* Update button click event ***********************/
		if(updateButton != null) {
			updateButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					EditText etLevel = (EditText) rootView.findViewById(R.id.course_level_input);
					EditText etName = (EditText) rootView.findViewById(R.id.course_name_input);
					EditText etGroup = (EditText) rootView.findViewById(R.id.course_group_input);
					EditText etTutor = (EditText) rootView.findViewById(R.id.course_tutor_input);
					EditText etConceptWeight = (EditText) rootView.findViewById(R.id.course_concept_weight_input);
					EditText etProcedureWeight = (EditText) rootView.findViewById(R.id.course_procedure_weight_input);
					EditText etAttitudeWeight = (EditText) rootView.findViewById(R.id.course_attitude_weight_input);
					
					Course course;
					course = new Course(
							context,
							etLevel.getText().toString(),
							etName.getText().toString(),
							etGroup.getText().toString(),
							etTutor.getText().toString(),
							Float.valueOf(etConceptWeight.getText().toString()),
							Float.valueOf(etProcedureWeight.getText().toString()),
							Float.valueOf(etAttitudeWeight.getText().toString()));
					WriterModel.updateCourse(context, course);
					Toast.makeText(context, R.string.course_update_ok, Toast.LENGTH_LONG).show();
					CoursesListFragment fragment = (CoursesListFragment) getFragmentManager()
							.findFragmentById(R.id.courses_list_fragment);
					if(fragment != null) {
						fragment.onResume();
						fragment.selectFirstCourse();
					}
				}
			});
		}
		/*********************************************************************/
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
	}
}
