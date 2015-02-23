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
 * A AttitudeStudentMark object.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class AttitudeStudentMark extends DBField {
	private Integer id;
	private Student student;
	private String attitudeDate;
	private Attitude attitude;
	
	public AttitudeStudentMark(Context context, Integer id, Student student,
			String attitudeDate, Attitude attitude) {
		super();
		this.id = id;
		this.student = student;
		this.attitudeDate = attitudeDate;
		this.attitude = attitude;
	}
	
	public AttitudeStudentMark(Context context, Student student,
			String attitudeDate, Attitude attitude) {
		super();
		this.student = student;
		this.attitudeDate = attitudeDate;
		this.attitude = attitude;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public String getAttitudeDate() {
		return attitudeDate;
	}
	public void setAttitudeDate(String attitudeDate) {
		this.attitudeDate = attitudeDate;
	}
	public Attitude getAttitude() {
		return attitude;
	}
	public void setAttitude(Attitude attitude) {
		this.attitude = attitude;
	}
}
