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
import es.rgmf.ltn.model.orm.Evaluation;

/**
 * A custom adapter class to inflate evaluations information.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class EvaluationListAdapter extends BaseAdapter {
	public static final int NOTHING_SELECTED = -1;
	
	/**
	 * The application context.
	 */
	Context mContext;
	/**
	 * The list of courses.
	 */
	List<Evaluation> mEvaluationsList;
	/**
	 * The last selected item.
	 */
	private int mSelectedItem = NOTHING_SELECTED; // no item selected by default
	/**
	 * The ViewGroup parent.
	 */
	private ViewGroup mViewGroup;
	
	/**
	 * Private view holder class. 
	 * 
	 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
	 */
    private class ViewHolder {
        TextView txtName;
        TextView txtStartDate;
        TextView txtEndDate;
    }
	
	/**
	 * Contructor.
	 * 
	 * @param context The application context.
	 * @param items The items.
	 */
	public EvaluationListAdapter(Context context, List<Evaluation> items) {
        this.mContext = context;
        this.mEvaluationsList = items;
    }
	
	/**
	 * Inflate the layout for each row.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        this.mViewGroup = parent;
        
        LayoutInflater mInflater = (LayoutInflater)
            mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_evaluations_list, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.evaluation_name);
            holder.txtStartDate = (TextView) convertView.findViewById(R.id.evaluation_startdate);
            holder.txtEndDate = (TextView) convertView.findViewById(R.id.evaluation_enddate);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Evaluation rowEvaluation = (Evaluation) getItem(position);
         
        holder.txtName.setText(rowEvaluation.getName());
        holder.txtStartDate.setText(rowEvaluation.getStartDate());
        holder.txtEndDate.setText(rowEvaluation.getEndDate());
        
        highlightItem(position, convertView);
         
        return convertView;
    }
	
	/**
	 * Set the background of the item of the view. The background color
	 * depend on selected or not the item view.
	 * 
	 * @param position The position where the view is.
	 * @param view The view.
	 */
	private void highlightItem(int position, View view) {
		if(position == mSelectedItem || (position == 0 && mSelectedItem == -1)) {
	    	// you can define your own color of selected item here
	    	view.setBackgroundColor(mContext.getResources().getColor(R.color.lv_selected_item));
	    } 
        else {
	    	// you can define your own default selector here
	    	view.setBackgroundColor(mContext.getResources().getColor(R.color.bg_application_color));
	    } 
	}
	
	/**
	 * Change the background color of the item selected.
	 * 
	 * @param relativeSelectedItem The position of the item over all items showed.
	 * @param absoluteSelectedItem The position of the item over all items.
	 */
	public void setSelectedItem(int relativeSelectedItem, int absoluteSelectedItem) {
		/*
		Log.v("Relative", "" + relativeSelectedItem);
		Log.v("Absolute", "" + absoluteSelectedItem);
		*/
		// For all items that are showed.
		for(int i = 0; i < mViewGroup.getChildCount(); i++) {
			if(i != relativeSelectedItem) // If is not the item selected.
				mViewGroup.getChildAt(i).setBackgroundColor(mContext.getResources().getColor(R.color.bg_application_color));
			else // If is the item selected.
				mViewGroup.getChildAt(i).setBackgroundColor(mContext.getResources().getColor(R.color.lv_selected_item));
		}
		this.mSelectedItem = absoluteSelectedItem;
	}

	public void setSelectedItem(int absoluteSelectedItem) {
		this.mSelectedItem = absoluteSelectedItem;
	}
	
	public int getSelectedItem() {
		return mSelectedItem;
	}

	@Override
	public int getCount() {
		return mEvaluationsList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mEvaluationsList.size() > 0)
			return mEvaluationsList.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		if (mEvaluationsList.size() > 0) 
			return mEvaluationsList.indexOf(getItem(position));
		else
			return NOTHING_SELECTED;
	}
	
	public void setEvaluationsList(List<Evaluation> evaluationsList) {
		mEvaluationsList = evaluationsList;
	}
}
