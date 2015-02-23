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
 * A Test object.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class Test extends DBField {
	private Integer id;
	private String name;
	private String description;
	private Float weight;
	private CourseEvaluation courseEvaluation;
	
	public Test(Context context, Integer id, String name, String description, Float weight,
			CourseEvaluation courseEvaluation) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.courseEvaluation = courseEvaluation;
	}
	
	public Test(Context context, String name, String description, Float weight,
			CourseEvaluation courseEvaluation) {
		super();
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.courseEvaluation = courseEvaluation;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	public CourseEvaluation getCourseEvaluation() {
		return courseEvaluation;
	}
	public void setCourseEvaluation(CourseEvaluation courseEvaluation) {
		this.courseEvaluation = courseEvaluation;
	}
	
}
