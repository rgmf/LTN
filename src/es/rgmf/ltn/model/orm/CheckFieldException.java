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

public class CheckFieldException extends Exception {
	/**
	 * Auto-generated serial version.
	 */
	private static final long serialVersionUID = -1368981125336184825L;
	
	/**
	 * Constructors.
	 */
	public CheckFieldException() {}
	public CheckFieldException(String msg) {
		super(msg);
	}
	public CheckFieldException(Throwable cause) {
		super(cause);
	}
	public CheckFieldException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
