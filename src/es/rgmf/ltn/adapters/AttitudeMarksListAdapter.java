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
import android.widget.TextView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.orm.AttitudeStudentMark;

/**
 * A custom adapter class to inflate attitudes marks of the student enrolled in a course
 * information.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class AttitudeMarksListAdapter extends BaseAdapter {
	/**
	 * The application context.
	 */
	Context mContext;
	/**
	 * The list of courses.
	 */
	List<AttitudeStudentMark> mAttitudesList;
	
	/**
	 * Private view holder class. 
	 * 
	 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
	 */
    private class ViewHolder {
        TextView txtDate;
        TextView txtName;
        TextView txtDescription;
        TextView txtWeight;
    }
	
	/**
	 * Contructor.
	 * 
	 * @param context The application context.
	 * @param items The items.
	 */
	public AttitudeMarksListAdapter(Context context, List<AttitudeStudentMark> items) {
        this.mContext = context;
        this.mAttitudesList = items;
    }
	
	/**
	 * Inflate the layout for each row.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
         
        LayoutInflater mInflater = (LayoutInflater)
            mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_attitudes_list, null);
            holder = new ViewHolder();
            holder.txtDate = (TextView) convertView.findViewById(R.id.attitudes_date);
            holder.txtName = (TextView) convertView.findViewById(R.id.attitudes_name);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.attitudes_description);
            holder.txtWeight = (TextView) convertView.findViewById(R.id.attitudes_weight);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
         
        AttitudeStudentMark row = (AttitudeStudentMark) getItem(position);
        
        holder.txtDate.setText(row.getAttitudeDate());
        holder.txtName.setText(row.getAttitude().getAllAttitude().getName());
        holder.txtDescription.setText(row.getAttitude().getAllAttitude().getDescription());
        holder.txtWeight.setText(String.valueOf(row.getAttitude().getWeight()));
        
        return convertView;
    }

	@Override
	public int getCount() {
		return mAttitudesList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAttitudesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mAttitudesList.indexOf(getItem(position));
	}
}
