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

package es.rgmf.ltn;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import es.rgmf.ltn.model.WriterModel;
import es.rgmf.ltn.model.orm.Course;

/**
 * Add Course Activity, an activity that perform courses
 * addition.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */

public class AddCourseActivity extends Activity {
	protected Context mContext;
	
	/**
	 * Method that is called when activity is created.
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        mContext = this;
        
        // Cancel action clicked.
        findViewById(R.id.course_cancel_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
        
        // When user click on add course button it executes this method.
        findViewById(R.id.course_ok_button).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Test the fields.
				EditText etLevel = (EditText) findViewById(R.id.course_level_input);
				EditText etName = (EditText) findViewById(R.id.course_name_input);
				EditText etGroup = (EditText) findViewById(R.id.course_group_input);
				EditText etTutor = (EditText) findViewById(R.id.course_tutor_input);
				EditText etConceptWeight = (EditText) findViewById(R.id.course_concept_weight_input);
				EditText etProcedureWeight = (EditText) findViewById(R.id.course_procedure_weight_input);
				EditText etAttitudeWeight = (EditText) findViewById(R.id.course_attitude_weight_input);
				TextView tvPercentagesSum = (TextView) findViewById(R.id.course_weightes_total_text);
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
				
				// Creates the course.
				Course course;
				course = new Course(
						mContext,
						etLevel.getText().toString(),
						etName.getText().toString(),
						etGroup.getText().toString(),
						etTutor.getText().toString(),
						Float.valueOf(etConceptWeight.getText().toString()),
						Float.valueOf(etProcedureWeight.getText().toString()),
						Float.valueOf(etAttitudeWeight.getText().toString()));
				try {
					WriterModel.addCourse(mContext, course);
				} catch(Exception e) {
					Log.v("AddCourseActivity", "onCreate::" + e.getMessage());
					Toast.makeText(mContext, getString(R.string.error_adding_course) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
				onBackPressed();
			}
        });
	}
}
