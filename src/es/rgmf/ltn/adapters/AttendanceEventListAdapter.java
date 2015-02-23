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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.orm.AttendanceEvent;
import es.rgmf.ltn.util.Utilities;

/**
 * A custom adapter class to inflate attendance event information.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class AttendanceEventListAdapter extends BaseAdapter {
	/**
	 * The application context.
	 */
	Context mContext;
	/**
	 * The list of courses.
	 */
	List<AttendanceEvent> mAttendanceEventList;
	
	/**
	 * Private view holder class. 
	 * 
	 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
	 */
    private class ViewHolder {
    	TextView txtId;
    	ImageView ivIcon;
        TextView txtName;
        TextView txtDescription;
    }
	
	/**
	 * Contructor.
	 * 
	 * @param context The application context.
	 * @param items The items.
	 */
	public AttendanceEventListAdapter(Context context, List<AttendanceEvent> items) {
        this.mContext = context;
        this.mAttendanceEventList = items;
    }
	
	/**
	 * Inflate the layout for each row.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
         
        LayoutInflater mInflater = (LayoutInflater)
            mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.attendance_img_msg_spinner, null);
            holder = new ViewHolder();
            holder.txtId = (TextView) convertView.findViewById(R.id.attendance_spinner_id);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.attendance_spinner_icon);
            holder.txtName = (TextView) convertView.findViewById(R.id.attendance_spinner_msg);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        AttendanceEvent row = (AttendanceEvent) getItem(position);
        
        holder.txtId.setText(Integer.toString(row.getId()));
        holder.ivIcon.setImageDrawable(Utilities.getDrawable(mContext, row.getIcon()));
        holder.txtName.setText(row.getName());
        
        return convertView;
    }

	@Override
	public int getCount() {
		return mAttendanceEventList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAttendanceEventList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mAttendanceEventList.indexOf(getItem(position));
	}

	public int indexOf(Integer id) {
		for(int i = 0; i < getCount(); i++) {
			if(mAttendanceEventList.get(i).getId() == id)
				return i;
			
		}
		return 0;
	}
}
