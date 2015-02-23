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
 * A CourseEvaluation object.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class CourseEvaluation extends DBField {
	private Integer id;
	private Course course;
	private Evaluation evaluation;
	
	public CourseEvaluation(Context context, Integer id, Course course, Evaluation evaluation) {
		super();
		this.id = id;
		this.course = course;
		this.evaluation = evaluation;
	}
	
	public CourseEvaluation(Context context, Course course, Evaluation evaluation) {
		super();
		this.course = course;
		this.evaluation = evaluation;
	}
	
	public CourseEvaluation(Context context, Integer id, Integer courseId, Integer evaluationId) {
		super();
		this.id = id;
		this.course = new Course();
		this.course.setId(courseId);
		this.evaluation = new Evaluation();
		this.evaluation.setId(evaluationId);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Evaluation getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}
	
	
}
