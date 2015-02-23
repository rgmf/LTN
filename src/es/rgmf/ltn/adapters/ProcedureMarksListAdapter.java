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
import android.widget.TextView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.orm.PracticeStudentMark;

/**
 * A custom adapter class to inflate procedures marks of the student enrolled in a course
 * information.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class ProcedureMarksListAdapter extends MarksListAdapter {
	/**
	 * The list of courses.
	 */
	List<PracticeStudentMark> mProceduresList;
	
	/**
	 * Private view holder class. 
	 * 
	 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
	 */
    private class ViewHolder {
        TextView txtStartDate;
        TextView txtEndDate;
        TextView txtName;
        TextView txtMark;
        TextView txtDescription;
        TextView txtComment;
    }
	
	/**
	 * Contructor.
	 * 
	 * @param context The application context.
	 * @param items The items.
	 */
	public ProcedureMarksListAdapter(Context context, List<PracticeStudentMark> items) {
        this.mContext = context;
        this.mProceduresList = items;
    }
	
	/**
	 * Inflate the layout for each row.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
         
        LayoutInflater mInflater = (LayoutInflater)
            mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_procedures_list, null);
            holder = new ViewHolder();
            holder.txtStartDate = (TextView) convertView.findViewById(R.id.procedures_start_date);
            holder.txtEndDate = (TextView) convertView.findViewById(R.id.procedures_end_date);
            holder.txtName = (TextView) convertView.findViewById(R.id.procedures_name);
            holder.txtMark = (TextView) convertView.findViewById(R.id.procedures_mark);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.procedures_description);
            holder.txtComment = (TextView) convertView.findViewById(R.id.procedures_comment);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
         
        PracticeStudentMark row = (PracticeStudentMark) getItem(position);
        
        holder.txtStartDate.setText(row.getPractice().getStartDate());
        holder.txtEndDate.setText(row.getPractice().getEndDate());
        holder.txtName.setText(row.getPractice().getTest().getName());
        holder.txtMark.setText(String.valueOf(row.getMark()));
        holder.txtDescription.setText(row.getPractice().getTest().getDescription());
        holder.txtComment.setText(row.getComment());
        
        return convertView;
    }

	@Override
	public int getCount() {
		return mProceduresList.size();
	}

	@Override
	public Object getItem(int position) {
		return mProceduresList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mProceduresList.indexOf(getItem(position));
	}

	@Override
	public Float getMarkAtPosition(int pos) {
		PracticeStudentMark row = (PracticeStudentMark) getItem(pos);
		return row.getMark();
	}

	@Override
	public String getCommentAtPosition(int pos) {
		PracticeStudentMark row = (PracticeStudentMark) getItem(pos);
		return row.getComment();
	}
}
