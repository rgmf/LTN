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

package es.rgmf.ltn.model.orm;

import android.content.Context;

/**
 * Attendance object.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class Attendance extends DBField {
	private Integer id;
	private String attendanceDate;
	private AttendanceEvent event;
	private CourseStudent courseStudent;
	
	public Attendance(Context context, Integer id, String attendanceDate,
			AttendanceEvent event, CourseStudent courseStudent) {
		super();
		this.id = id;
		this.attendanceDate = attendanceDate;
		this.event = event;
		this.courseStudent = courseStudent;
	}
	
	public Attendance(Context context, String attendanceDate,
			AttendanceEvent event, CourseStudent courseStudent) {
		super();
		this.attendanceDate = attendanceDate;
		this.event = event;
		this.courseStudent = courseStudent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(String attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public AttendanceEvent getEvent() {
		return event;
	}

	public void setEvent(AttendanceEvent event) {
		this.event = event;
	}

	public CourseStudent getCourseStudent() {
		return courseStudent;
	}

	public void setCourseStudent(CourseStudent courseStudent) {
		this.courseStudent = courseStudent;
	}
}
