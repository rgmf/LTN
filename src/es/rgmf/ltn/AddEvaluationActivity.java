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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import es.rgmf.ltn.model.WriterModel;
import es.rgmf.ltn.model.orm.CheckFieldException;
import es.rgmf.ltn.model.orm.Evaluation;
import es.rgmf.ltn.util.Dates;

/**
 * This is the activity that create an evaluation through a form.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class AddEvaluationActivity extends Activity {
	protected Context mContext;

	/**
	 * Method that is called when activity is created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_evaluation);
		mContext = this;

		// Cancel action clicked.
		findViewById(R.id.evaluation_cancel_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

		// When user click on add evaluation button it executes this method.
		findViewById(R.id.evaluation_ok_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							Evaluation evaluation;
							evaluation = new Evaluation(
									mContext,
									((EditText) findViewById(R.id.evaluation_name_input))
											.getText().toString(),
									Dates.getStringFromDatePicker((DatePicker) findViewById(R.id.evaluation_startdate_input)),
									Dates.getStringFromDatePicker((DatePicker) findViewById(R.id.evaluation_enddate_input)));
							try {
								WriterModel.addEvaluation(mContext, evaluation);
							} catch (Exception e) {
								Log.v("AddEvaluationActivity",
										"onCreate::" + e.getMessage());
								Toast.makeText(
										mContext,
										getString(R.string.error_adding_evaluation)
												+ ": " + e.getMessage(),
										Toast.LENGTH_LONG).show();
							}
							onBackPressed();
						} catch (CheckFieldException e) {
							Toast.makeText(mContext, e.getMessage(),
									Toast.LENGTH_LONG).show();
							e.printStackTrace();
							return;
						}
					}
				});
	}
}
