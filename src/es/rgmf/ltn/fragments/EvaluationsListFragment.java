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

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.rgmf.ltn.R;
import es.rgmf.ltn.adapters.EvaluationListAdapter;
import es.rgmf.ltn.adapters.StudentListAdapter;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.orm.Evaluation;
import es.rgmf.ltn.util.Dates;

/**
 * The fragment with the list of evaluations.
 * 
 * This fragment communicate to {@link EvaluationsDetailFragment.java} fragment
 * to inflate the values of the evaluation selected in this list fragment.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class EvaluationsListFragment extends ListFragment {
	private static final String STATE_SELECTED_ITEM = "selectedItem";

	/**
	 * The list view that need to be inflate.
	 */
	private ListView mListView;
	/**
	 * The list of evaluations that will be showed in the list view.
	 */
	private ArrayList<Evaluation> mEvaluationsList;
	/**
	 * The adapter.
	 */
	private EvaluationListAdapter mAdapter;
	private int mSelectedItem;

	/**
	 * This method is called when this fragment is created.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mEvaluationsList = ReaderModel.getEvaluations(getActivity());
		mAdapter = new EvaluationListAdapter(getActivity(), mEvaluationsList);
		setListAdapter(mAdapter);

		if (savedInstanceState == null) {
			selectFirstEvaluation();
		} else {
			mSelectedItem = (int) savedInstanceState
					.getLong(STATE_SELECTED_ITEM);
			mAdapter.setSelectedItem(mSelectedItem);
			setDetailEvaluation((Evaluation) mAdapter.getItem(mSelectedItem));
		}
	}

	/**
	 * This method is called when the fragment is showed to the user.
	 */
	@Override
	public void onResume() {
		super.onResume();

		mEvaluationsList = ReaderModel.getEvaluations(getActivity());
		mAdapter = new EvaluationListAdapter(getActivity(), mEvaluationsList);
		setListAdapter(mAdapter);

		if (mAdapter.getCount() > 0) {
			mAdapter.setSelectedItem(mSelectedItem);
			setDetailEvaluation((Evaluation) mAdapter.getItem(mSelectedItem));
		}
	}

	/**
	 * Method that can be called when there are changes in the list of
	 * evaluations.
	 */
	public void refresh() {
		mEvaluationsList = ReaderModel.getEvaluations(getActivity());
		mAdapter = new EvaluationListAdapter(getActivity(), mEvaluationsList);
		setListAdapter(mAdapter);
	}

	/**
	 * Override this method to save course identify and selected item in the
	 * list view.
	 * 
	 * This method is needed to management the screen rotate. We need to save
	 * the course identify and selected item on list view (students list).
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(
				STATE_SELECTED_ITEM,
				mAdapter.getSelectedItem() == StudentListAdapter.NOTHING_SELECTED ? 0
						: mAdapter.getSelectedItem());
	}

	/**
	 * This method is called when an item in the courses list is clicked.
	 * 
	 * All we need to do is change the information that user can see in
	 * CourseDetailFragment.
	 */
	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		Evaluation evaluation = (Evaluation) mAdapter.getItem(position);
		// Evaluation evaluation = (Evaluation)
		// listView.getItemAtPosition(position);
		int firstPosition = listView.getFirstVisiblePosition()
				- listView.getHeaderViewsCount(); // This is the same as child
													// #0
		int wantedChild = position - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild
		// will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= listView.getChildCount()) {
			Toast.makeText(getActivity(),
					getString(R.string.error_selecting_item), Toast.LENGTH_LONG)
					.show();
			return;
		}

		// Could also check if wantedPosition is between
		// listView.getFirstVisiblePosition() and
		// listView.getLastVisiblePosition() instead.
		mAdapter.setSelectedItem(wantedChild, position);

		// Set the evaluation detail.
		setDetailEvaluation(evaluation);
	}

	/**
	 * Programmatically check item. If there are not items then show the
	 * message.
	 */
	public void selectFirstEvaluation() {
		if (mEvaluationsList.size() > 0) {
			getListView().setItemChecked(0, true);
			Evaluation evaluations = (Evaluation) getListView()
					.getItemAtPosition(0);
			setDetailEvaluation(evaluations);
		} else {
			((TextView) getActivity()
					.findViewById(R.id.evaluation_detail_title))
					.setText(getActivity().getString(
							R.string.there_are_not_evaluations));
			// Hide the RelativeLayout where all detail fields are.
			thereAreNotEvaluations();
		}
	}

	/**
	 * Set evaluation values.
	 * 
	 * @param evaluation
	 *            The evaluation to show.
	 */
	private void setDetailEvaluation(Evaluation evaluation) {
		if (evaluation != null) {
			TextView tvId = (TextView) getActivity().findViewById(
					R.id.evaluation_id);
			TextView tvTitle = (TextView) getActivity().findViewById(
					R.id.evaluation_detail_title);
			EditText etName = (EditText) getActivity().findViewById(
					R.id.evaluation_name_input);
			DatePicker dpStartDate = (DatePicker) getActivity().findViewById(
					R.id.evaluation_startdate_input);
			DatePicker dpEndDate = (DatePicker) getActivity().findViewById(
					R.id.evaluation_enddate_input);
			Calendar startDate = Dates.getCalendarFromString(evaluation
					.getStartDate());
			Calendar endDate = Dates.getCalendarFromString(evaluation.getEndDate());
	
			tvId.setText(String.valueOf(evaluation.getId()));
			tvTitle.setText(getActivity().getString(
					R.string.evaluation_detail_title));
			etName.setText(evaluation.getName());
			dpStartDate.init(startDate.get(Calendar.YEAR),
					startDate.get(Calendar.MONTH),
					startDate.get(Calendar.DAY_OF_MONTH), null);
			dpEndDate.init(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH),
					endDate.get(Calendar.DAY_OF_MONTH), null);
	
			thereAreEvaluations();
		}
		else {
			thereAreNotEvaluations();
		}
	}

	/**
	 * This method make visible all fields needed.
	 */
	private void thereAreEvaluations() {
		getActivity().findViewById(R.id.evaluation_detail_fields)
				.setVisibility(View.VISIBLE);
		// Show buttons in the button bar below.
		getActivity().findViewById(R.id.evaluation_delete_button).setVisibility(
				View.VISIBLE);
		getActivity().findViewById(R.id.evaluation_update_button).setVisibility(
				View.VISIBLE);
	}

	/**
	 * This method hide all fields needs to hide.
	 */
	private void thereAreNotEvaluations() {
		((TextView) getActivity().findViewById(R.id.evaluation_detail_title))
				.setText(getActivity().getString(
						R.string.there_are_not_evaluations));
		// Hide the RelativeLayout where all detail fields are.
		getActivity().findViewById(R.id.evaluation_detail_fields)
				.setVisibility(View.GONE);
		// Hide buttons in the button bar below.
		getActivity().findViewById(R.id.evaluation_delete_button).setVisibility(
				View.GONE);
		getActivity().findViewById(R.id.evaluation_update_button).setVisibility(
				View.GONE);
	}
}
