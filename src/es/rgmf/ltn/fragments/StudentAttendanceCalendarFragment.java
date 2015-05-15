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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.orm.Attendance;
import es.rgmf.ltn.views.CalendarView;

/**
 * This fragment show the information of the Student attendance for a Course in
 * a Calendar.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class StudentAttendanceCalendarFragment extends Fragment {
	/**
	 * Course identify.
	 */
	private Integer mCourseId;
	/**
	 * Student identify.
	 */
	private Integer mStudentId;
	/**
	 * Attendance list.
	 */
	private List<Attendance> mAttendanceList;
	/**
	 * The root View.
	 */
	private View mRootView;

	/**
	 * Create a instance of this class.
	 * 
	 * @param courseId
	 *            The course id where student is.
	 * @param studentId
	 *            The student id.
	 * @return The StudentAttendanceCalendarFragment object created.
	 */
	public static StudentAttendanceCalendarFragment newInstance(int courseId,
			int studentId, List<Attendance> attendanceList) {
		StudentAttendanceCalendarFragment fragment = new StudentAttendanceCalendarFragment();
		fragment.mCourseId = courseId;
		fragment.mStudentId = studentId;
		fragment.mAttendanceList = attendanceList;
		return fragment;
	}

	/**
	 * Method that is called when the view is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		mRootView = inflater
				.inflate(R.layout.fragment_student_attendance_calendar,
						container, false);

		if(savedInstanceState == null) {
			// Get student attendance information to this course and put them inside
			// a map with color information we want to the calendar view.
			Map<String, Integer> colorMap = new HashMap<String, Integer>();
			
			for (Attendance a : mAttendanceList) {
				switch (a.getEvent().getName()) {
				case "Falta no justificada":
					colorMap.put(
							a.getAttendanceDate(),
							Color.RED);
					break;
				case "Falta justificada":
					colorMap.put(
							a.getAttendanceDate(),
							Color.YELLOW);
					break;
				case "Retraso":
					colorMap.put(
							a.getAttendanceDate(),
							Color.CYAN);
					break;
				}
			}
				
			// Create and put the calendar view inside layout.
			LinearLayout linearLayout = (LinearLayout) mRootView
					.findViewById(R.id.linear_layout_calendar);
			View calendarView = new CalendarView(getActivity(), colorMap);
			linearLayout.addView(calendarView);
		}

		return mRootView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
