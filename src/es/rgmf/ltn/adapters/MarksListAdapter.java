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

package es.rgmf.ltn.adapters;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * The abstract adapter to all kind of marks list adapters.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public abstract class MarksListAdapter extends BaseAdapter {
	/**
	 * The application context.
	 */
	Context mContext;
	public abstract Float getMarkAtPosition(int pos);
	public abstract String getCommentAtPosition(int pos);
}
