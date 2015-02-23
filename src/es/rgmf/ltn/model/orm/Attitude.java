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
 * Attitude object.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class Attitude extends DBField {
	private Integer id;
	private Float weight;
	private CourseEvaluation courseEvaluation;
	private AllAttitude allAttitude;
	
	public Attitude(Context context, Integer id, Float weight,
			CourseEvaluation courseEvaluation, AllAttitude allAttitude) {
		super();
		this.id = id;
		this.weight = weight;
		this.courseEvaluation = courseEvaluation;
		this.allAttitude = allAttitude;
	}
	
	public Attitude(Context context, Float weight,
			CourseEvaluation courseEvaluation, AllAttitude allAttitude) {
		super();
		this.weight = weight;
		this.courseEvaluation = courseEvaluation;
		this.allAttitude = allAttitude;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public AllAttitude getAllAttitude() {
		return allAttitude;
	}
	public void setAllAttitude(AllAttitude allAttitude) {
		this.allAttitude = allAttitude;
	}
	
}
