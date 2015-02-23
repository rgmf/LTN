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
 * A Course object.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class Course extends DBField {
	private Integer id;
	private String level;
	private String name;
	private String group;
	private String tutor;
	private Float conceptWeight;
	private Float procedureWeight;
	private Float attitudeWeight;
	
	public Course(Context context, Integer id, String level, String name, String group,
			String tutor, Float conceptWeight, Float procedureWeight, Float attitudeWeight) {
		this.id = id;
		this.level = level;
		this.name = name;
		this.group = group;
		this.tutor = tutor;
		this.conceptWeight = conceptWeight;
		this.procedureWeight = procedureWeight;
		this.attitudeWeight = attitudeWeight;
	}
	
	public Course(Context context, String id, String level, String name, String group,
			String tutor, Float conceptWeight, Float procedureWeight, Float attitudeWeight) {
		this.id = Integer.valueOf(id);
		this.level = level;
		this.name = name;
		this.group = group;
		this.tutor = tutor;
		this.conceptWeight = conceptWeight;
		this.procedureWeight = procedureWeight;
		this.attitudeWeight = attitudeWeight;
	}

	public Course(Context context, String level, String name, String group,
			String tutor, Float conceptWeight, Float procedureWeight, Float attitudeWeight) {
		this.level = level;
		this.name = name;
		this.group = group;
		this.tutor = tutor;
		this.conceptWeight = conceptWeight;
		this.procedureWeight = procedureWeight;
		this.attitudeWeight = attitudeWeight;
	}
	
	public Course() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
	}

	public Float getConceptWeight() {
		return conceptWeight;
	}

	public void setConceptWeight(Float conceptWeight) {
		this.conceptWeight = conceptWeight;
	}
	public Float getProcedureWeight() {
		return procedureWeight;
	}

	public void setProcedureWeight(Float procedureWeight) {
		this.procedureWeight = procedureWeight;
	}

	public Float getAttitudeWeight() {
		return attitudeWeight;
	}

	public void setAttitudeWeight(Float attitudeWeight) {
		this.attitudeWeight = attitudeWeight;
	}
}
