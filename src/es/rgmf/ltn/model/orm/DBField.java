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

class DBField {
	/**
	 * Check if a string field is null or empty.
	 * @param f the string to checking.
	 * @return true if the string is null or empty and false otherwise.
	 */
	protected boolean isNull(String f) {
		if(f != null) {
			if(f.trim().length() > 0) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Check if a integer field is null.
	 * @param f the integer to checking.
	 * @return true if the string is null or empty and false otherwise.
	 */
	protected boolean isNull(Integer f) {
		if(f != null) {
			return false;
		}
		
		return true;
	}
}
