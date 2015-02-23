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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.WriterModel;
import es.rgmf.ltn.model.orm.CheckFieldException;
import es.rgmf.ltn.model.orm.Evaluation;
import es.rgmf.ltn.util.Dates;

/**
 * The fragment with a evaluation detail.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class EvaluationDetailFragment extends Fragment {
	/**
	 * The View. It can be used to access xml elements of this View.
	 */
	private View rootView;
	/**
	 * The context.
	 */
	private Context context;
	
	/**
	 * Method that is called when the view is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_evaluation_detail, container, false);

		if(getActivity() != null) {
			this.context = getActivity().getApplicationContext();
		}
		
		/******************* Delete button click event ***********************/
		((ImageView) rootView.findViewById(R.id.evaluation_delete_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(rootView.getContext())
				.setTitle(R.string.delete_button_hint)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(getResources().getString(R.string.evaluation_delete_message))
				.setCancelable(true).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// We try to delete the course and if the course is correctly deleted then
						// we refresh data of CoursesListFragment.
						String id = ((TextView) rootView.findViewById(R.id.evaluation_id)).getText().toString();
						if(WriterModel.deleteEvaluation(context, id)) {
							EvaluationsListFragment fragment = (EvaluationsListFragment) getFragmentManager()
									.findFragmentById(R.id.evaluations_list_fragment);
							if(fragment != null) {
								fragment.refresh();
								fragment.selectFirstEvaluation();
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
		((ImageView) rootView.findViewById(R.id.evaluation_update_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Evaluation evaluation;
					evaluation = new Evaluation(
							context,
							((TextView) rootView.findViewById(R.id.evaluation_id)).getText().toString(),
							((EditText) rootView.findViewById(R.id.evaluation_name_input)).getText().toString(),
							Dates.getStringFromDatePicker((DatePicker) rootView.findViewById(R.id.evaluation_startdate_input)),
							Dates.getStringFromDatePicker((DatePicker) rootView.findViewById(R.id.evaluation_enddate_input)));
					WriterModel.updateEvaluation(context, evaluation);
					Toast.makeText(context, R.string.evaluation_update_ok, Toast.LENGTH_LONG).show();
					EvaluationsListFragment fragment = (EvaluationsListFragment) getFragmentManager()
							.findFragmentById(R.id.evaluations_list_fragment);
					if(fragment != null) {
						fragment.refresh();
						fragment.selectFirstEvaluation();
					}
				} catch (CheckFieldException e) {
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				    return;
				}
			}
		});
		/*********************************************************************/
		
		return rootView;
	}
}
