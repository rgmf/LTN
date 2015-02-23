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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.orm.Course;

/**
 * A custom adapter class to inflate the items into navigation drawer.
 * 
 * There are differents kinds of itmes into this navigation drawer.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class NavigationDrawerAdapter extends BaseAdapter {
	/**
	 * The context of the application.
	 */
	Context context;
	/**
	 * The regular menu items.
	 */
	private String[] mMenus = {"Home"};
	/**
	 * The list of courses.
	 */
	private ArrayList<Course> mCourses;
	
	/**
	 * Private view holder class (Course item).
	 * 
	 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
	 */
    private class CourseHolder {
    	TextView txtId;
        TextView txtLevel;
        TextView txtName;
        TextView txtGroup;
        TextView txtTutor;
    }
    
    /**
	 * Private view holder class (Menu item). 
	 * 
	 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
	 */
    private class MenuHolder {
        TextView txtName;
    }
	
	/**
	 * Contructor.
	 * 
	 * @param context The application context.
	 */
	public NavigationDrawerAdapter(Context context) {
        this.context = context;
        mCourses = ReaderModel.getCourses(context);
    }
	
	/**
	 * Inflate the layout for each row.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if(position < mMenus.length) {
			view = getMenuView((String)getItem(position), position, convertView, parent);
		}
		else {
			view = getCourseView((Course)getItem(position), position, convertView, parent);
		}
		
		return view;
    }
	
	/**
	 * Create and return the course item view.
	 * 
	 * @param item
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	private View getCourseView(Course item, int position, View convertView, ViewGroup parent) {
		CourseHolder holder = null;
        
        LayoutInflater mInflater = (LayoutInflater)
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_courses_nav_drawer_list, null);
            holder = new CourseHolder();
            holder.txtId = (TextView) convertView.findViewById(R.id.nd_course_id);
            holder.txtLevel = (TextView) convertView.findViewById(R.id.nd_course_level);
            holder.txtName = (TextView) convertView.findViewById(R.id.nd_course_name);
            convertView.setTag(holder);
        }
        else {
            holder = (CourseHolder) convertView.getTag();
        }

        holder.txtId.setText(String.valueOf(item.getId()));
        holder.txtLevel.setText(item.getLevel());
        holder.txtName.setText(item.getName());
		
		return convertView;
	}

	/**
	 * Create and return the menu item view.
	 * 
	 * @param item
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	private View getMenuView(String item, int position, View convertView, ViewGroup parent) {
		MenuHolder holder = null;
        
        LayoutInflater mInflater = (LayoutInflater)
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_menu_list, null);
            holder = new MenuHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.menu_item_name);
            convertView.setTag(holder);
        }
        else {
            holder = (MenuHolder) convertView.getTag();
        }

        holder.txtName.setText(item);
         
        return convertView;
	}

	/**
	 * The number of the items in the navigation drawer are:
	 * - All regular item menus.
	 * - Plus all courses we have in the database.
	 */
	@Override
	public int getCount() {
		return mMenus.length + mCourses.size();
	}

	/**
	 * Return the item that is in the position.
	 */
	@Override
	public Object getItem(int position) {
		if(position < mMenus.length) {
			return mMenus[position];
		}
		else {
			return mCourses.get(position - mMenus.length);	
		}
	}

	/**
	 * Return the position.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Return a title of the item position.
	 * 
	 * @param position
	 */
	public String getTitleOfItem(int position) {
		if(position < mMenus.length){
			return mMenus[position];
		}
		else if(position < (mCourses.size() + mMenus.length)) {
			return mCourses.get(position - mMenus.length).getLevel() + " " + 
					mCourses.get(position - mMenus.length).getName() + " " +
					mCourses.get(position - mMenus.length).getGroup();
		}
		else {
			return "";
		}
	}
}
