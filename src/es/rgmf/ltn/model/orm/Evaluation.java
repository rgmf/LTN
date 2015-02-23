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

/**
 * The Evaluation class ORM.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class Evaluation extends DBField {
	private Integer id;
	private String name;
	private String startDate;
	private String endDate;
	
	public Evaluation(Context context, String id, String name, String startDate, String endDate) throws CheckFieldException {
		super();
		this.id = Integer.valueOf(id);
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		
		if(isNull(id) || isNull(name) || isNull(startDate) || isNull(endDate)) {
			throw new CheckFieldException(context.getString(R.string.the_fields) + ": \"" + 
					context.getString(R.string.evaluation_id_hint) + ", " +
					context.getString(R.string.evaluation_name_hint) + ", " +
					context.getString(R.string.evaluation_startdate_hint) + ", " + 
					context.getString(R.string.evaluation_enddate_hint) + "\" " + 
					context.getString(R.string.are_required));
		}
	}
	
	public Evaluation(Context context, Integer id, String name, String startDate, String endDate) throws CheckFieldException {
		super();
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		
		if(isNull(id) || isNull(name) || isNull(startDate) || isNull(endDate)) {
			throw new CheckFieldException(context.getString(R.string.the_fields) + ": \"" + 
					context.getString(R.string.evaluation_id_hint) + ", " +
					context.getString(R.string.evaluation_name_hint) + ", " +
					context.getString(R.string.evaluation_startdate_hint) + ", " + 
					context.getString(R.string.evaluation_enddate_hint) + "\" " + 
					context.getString(R.string.are_required));
		}
	}
	
	public Evaluation(Context context, String name, String startDate, String endDate) throws CheckFieldException {
		super();
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		
		if(isNull(name) || isNull(startDate) || isNull(endDate)) {
			throw new CheckFieldException(context.getString(R.string.the_fields) + ": \"" + 
					context.getString(R.string.evaluation_name_hint) + ", " +
					context.getString(R.string.evaluation_startdate_hint) + ", " + 
					context.getString(R.string.evaluation_enddate_hint) + "\" " + 
					context.getString(R.string.are_required));
		}
	}

	public Evaluation() {
		// TODO Auto-generated constructor stub
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
