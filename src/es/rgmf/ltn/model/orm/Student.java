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
import es.rgmf.ltn.R;

public class Student extends DBField {
	private Integer id;
	private String name;
	private String lastname;
	private String photo;
	
	public Student(Context context, Integer id, String name, String lastname, String photo) throws CheckFieldException {
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.photo = photo;
		
		if(isNull(id) || isNull(name) || isNull(lastname)) {
			throw new CheckFieldException(context.getString(R.string.the_fields) + ": \"" + 
					context.getString(R.string.student_id_hint) + ", " +
					context.getString(R.string.student_name_hint) + ", " +
					context.getString(R.string.student_lastname_hint) + "\" " + 
					context.getString(R.string.are_required));
		}
	}
	
	public Student(Context context, String id, String name, String lastname, String photo) throws CheckFieldException {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.lastname = lastname;
		this.photo = photo;
		
		if(isNull(id) || isNull(name) || isNull(lastname)) {
			throw new CheckFieldException(context.getString(R.string.the_fields) + ": \"" + 
					context.getString(R.string.student_id_hint) + ", " +
					context.getString(R.string.student_name_hint) + ", " +
					context.getString(R.string.student_lastname_hint) + "\" " + 
					context.getString(R.string.are_required));
		}
	}
	
	public Student(Context context, String name, String lastname, String photo) throws CheckFieldException {
		this.name = name;
		this.lastname = lastname;
		this.photo = photo;
		
		if(isNull(name) || isNull(lastname)) {
			throw new CheckFieldException(context.getString(R.string.the_fields) + ": \"" + 
					context.getString(R.string.student_name_hint) + ", " +
					context.getString(R.string.student_lastname_hint) + "\" " + 
					context.getString(R.string.are_required));
		}
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
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
