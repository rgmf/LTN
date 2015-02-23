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
 * The PracticeStudentMark object.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class PracticeStudentMark extends DBField {
	private Integer id;
	private Student student;
	private Practice practice;
	private Float mark;
	private String comment;
	
	public PracticeStudentMark(Context context, Integer id, Student student, Practice practice,
			Float mark, String comment) {
		super();
		this.id = id;
		this.student = student;
		this.practice = practice;
		this.mark = mark;
		this.comment = comment;
	}
	
	public PracticeStudentMark(Context context, Student student, Practice practice,
			Float mark, String comment) {
		super();
		this.student = student;
		this.practice = practice;
		this.mark = mark;
		this.comment = comment;
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
	public Practice getPractice() {
		return practice;
	}
	public void setPractice(Practice practice) {
		this.practice = practice;
	}
	public Float getMark() {
		return mark;
	}
	public void setMark(Float mark) {
		this.mark = mark;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
