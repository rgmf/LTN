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

import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView;
import android.widget.ListView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.adapters.MarksListAdapter;
import es.rgmf.ltn.fragments.dialogs.SetMarkDialogFragment;
import es.rgmf.ltn.util.Session;

/**
 * The Fragment class that to show the sutdent marks enrolled in a course.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public abstract class StudentMarksFragment extends ListFragment implements
		SetMarkDialogFragment.SetMarkDialogListener {
	/**
	 * The student identifier.
	 */
	protected int mStudentId;
	/**
	 * The course identifier where student is enrolled.
	 */
	protected int mCourseId;
	/**
	 * The evaluation identifier selected.
	 */
	protected int mEvaluationId;
	/**
	 * The list view.
	 */
	protected ListView mListView;
	/**
	 * The application context.
	 */
	protected Context mContext;
	/**
	 * The position selected that determines which item was selected the last
	 * time.
	 */
	protected Integer mPositionSelected = null;
	/**
	 * The adapter.
	 */
	protected MarksListAdapter mAdapter = null;
	
	/**
	 * Constructor
	 */
	public StudentMarksFragment() {
		if(Session.getEvaluationIndex() != null) {
			mEvaluationId = Session.getEvaluations().get(Session.getEvaluationIndex()).getId();
		}
		else {
			mEvaluationId = 0;
		}
	}

	/**
	 * This method is called when this fragment is created.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mContext = getActivity();

		mListView = getListView();
		registerForContextMenu(mListView);

		// Show the button to add mark (concept, procedure or attitude).
		getActivity().findViewById(R.id.mark_add_button).setVisibility(
				View.VISIBLE);

		setListFragment();

		((ImageView) getActivity().findViewById(R.id.mark_add_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onClickAddMarkButton(v);
					}
				});
	}

	/**
	 * To respond to on click event on items in the list.
	 */
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, pos, id);

		// Create the dialog.
		DialogFragment dialog = new SetMarkDialogFragment();
		dialog.setTargetFragment(this, 0);
		dialog.show(getFragmentManager(), null);

		// Set arguments to dialog.
		Float mark = mAdapter.getMarkAtPosition(pos);
		String comment = mAdapter.getCommentAtPosition(pos);
		Bundle args = new Bundle();
		args.putFloat(SetMarkDialogFragment.ARG_MARK, mark);
		args.putString(SetMarkDialogFragment.ARG_COMMENT, comment);
		dialog.setArguments(args);

		// Set the current selected position.
		mPositionSelected = pos;
	}

	/**
	 * It handles the context menu item selected.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = (int) info.id;

		switch (item.getItemId()) {
		/* Delete the mark selected */
		case R.id.context_menu_delete:
			deletePositionItem(position);
			break;
		/* Change the mark selected */
		case R.id.context_menu_change:
			changePositionItem(position);
			break;
		/* Set the same mark for all students enrolled to the course */
		case R.id.context_menu_same_mark:
			// Create the dialog.
			DialogFragment dialog = new SetMarkDialogFragment();
			dialog.setTargetFragment(this, 0);
			dialog.show(getFragmentManager(), null);

			// Set arguments to dialog.
			Float mark = mAdapter.getMarkAtPosition(position);
			String comment = mAdapter.getCommentAtPosition(position);
			Bundle args = new Bundle();
			args.putFloat(SetMarkDialogFragment.ARG_MARK, mark);
			args.putString(SetMarkDialogFragment.ARG_COMMENT, comment);
			args.putBoolean(SetMarkDialogFragment.ARG_FORALL, true);
			dialog.setArguments(args);

			// Set the current selected position.
			mPositionSelected = position;
			break;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * When a item in the list receive a long-click event, the system calls this
	 * method.
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.context_change_delete_menu, menu);
	}

	protected abstract void changePositionItem(int position);

	protected abstract void deletePositionItem(int position);

	protected abstract void onClickAddMarkButton(View v);

	protected abstract void setListFragment();
}
