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

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.StudentActivity;
import es.rgmf.ltn.StudentDetailActivity;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.orm.Attendance;

/**
 * A fragment representing a single Student detail screen. This fragment is
 * either contained in a {@link StudentActivity} in two-pane mode (on tablets)
 * or a {@link StudentDetailActivity} on handsets.
 */
public class StudentAttendanceFragment extends Fragment {
	/**
	 * The list of attendance.
	 */
	private List<Attendance> mAttendanceList;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public StudentAttendanceFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Integer courseId = getArguments().getInt(StudentActivity.COURSE_ID);
		Integer studentId = getArguments().getInt(StudentActivity.STUDENT_ID);
		mAttendanceList = ReaderModel.getAllStudentCourseAttendance(getActivity(), courseId, studentId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_attendance,
				container, false);
		
		int noJustify = 0;
		int justify = 0;
		int delay = 0;
		
		for (Attendance a : mAttendanceList) {
			switch (a.getEvent().getName()) {
			case "Falta no justificada":
				noJustify++;
				break;
			case "Falta justificada":
				justify++;
				break;
			case "Retraso":
				delay++;
				break;
			}
		}
		
		TextView tvNotJustify = (TextView) rootView.findViewById(R.id.fsa_student_attendance_not_justify);
		TextView tvJustify = (TextView) rootView.findViewById(R.id.fsa_student_attendance_justify);
		TextView tvDelay = (TextView) rootView.findViewById(R.id.fsa_student_attendance_delay);
		
		tvNotJustify.setText("Faltas no justificadas: " + noJustify);
		tvJustify.setText("Faltas justificadas: " + justify);
		tvDelay.setText("Retrasos: " + delay);

		return rootView;
	}
}
