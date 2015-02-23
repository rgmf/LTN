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

package es.rgmf.ltn.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import es.rgmf.ltn.R;

/**
 * This class is a DialogFragment to set a mark.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class SetMarkDialogFragment extends DialogFragment {
	public static final String ARG_MARK = "mark";
	public static final String ARG_COMMENT = "comment";
	public static final String ARG_FORALL = "for_all";

	private EditText markEditText;
	private EditText commentEditText;
	private Boolean forAllStudents = false;

	// Use this instance of the interface to deliver action events
	/**
	 * The instance of the interface (see below of this class) to deliver action
	 * events to back to activity that creates this Dialog.
	 */
	SetMarkDialogListener mListener;

	/**
	 * When the dialog is created this method is execute.
	 * 
	 * It initializes the mListener calling getTargetFragment. In this case it
	 * needs that in the Fragment that calls this DialogFragment is setting the
	 * target fragment with setTargetFragment(the fragment, 0) calling.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			mListener = (SetMarkDialogListener) getTargetFragment();
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Calling Fragment must implement SetMarkDialogListener");
		}
	}

	/**
	 * This method is called when the Dialog is created.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater factory = LayoutInflater.from(getActivity());
		final View v = factory.inflate(R.layout.dialog_set_mark, null);

		Bundle args = getArguments();
		if (args != null) {
			markEditText = (EditText) v.findViewById(R.id.mark_value);
			commentEditText = (EditText) v.findViewById(R.id.comment_value);
			markEditText.setText(String.valueOf(args.getFloat(ARG_MARK, 0f)));
			commentEditText.setText(args.getString(ARG_COMMENT, ""));
			forAllStudents = args.getBoolean(ARG_FORALL, false);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setView(v);

		// Add action buttons
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						EditText etMark = (EditText) ((Dialog) dialog)
								.findViewById(R.id.mark_value);
						EditText etComment = (EditText) ((Dialog) dialog)
								.findViewById(R.id.comment_value);
						String strMark = etMark.getText().toString();
						String strComment = etComment.getText().toString();
						if (forAllStudents)
							mListener.onDialogPositiveClickForAllStudents(Float.valueOf(strMark),
									strComment);
						else
							mListener.onDialogPositiveClick(Float.valueOf(strMark),
									strComment);
					}
				}).setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						SetMarkDialogFragment.this.getDialog().cancel();
					}
				});

		return builder.create();
	}

	/**
	 * Override the Fragment.onAttach() method to instantiate the
	 * NoticeDialogListener.
	 */
	/*
	 * @Override public void onAttach(Activity activity) {
	 * super.onAttach(activity); // Verify that the host activity implements the
	 * callback interface try { // Instantiate the SetMarkDialogListener so we
	 * can send events to the host. // The R.id.student_framelayout_detail is
	 * where is loading the concept, mark and attitude fragments. mListener =
	 * (SetMarkDialogListener)
	 * activity.getFragmentManager().findFragmentById(R.id
	 * .student_framelayout_detail); } catch (ClassCastException e) { // The
	 * activity doesn't implement the interface, throw exception throw new
	 * ClassCastException(activity.toString() +
	 * " must implement SetMarkDialogListener"); } }
	 */

	/**
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface SetMarkDialogListener {
		public void onDialogPositiveClick(float value, String strComment);
		public void onDialogPositiveClickForAllStudents(float vlaue, String strComment);
	}
}
