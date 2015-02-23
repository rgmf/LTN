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

package es.rgmf.ltn.fragments;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.orm.Course;

/**
 * The fragment with the list of courses.
 * 
 * This fragment communicate to {@link CourseDetailFragment.java} fragment
 * to inflate the values of the course selected int this list fragment.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class CoursesListFragment extends CoursesAbstractListFragment {
	/**
	 * This method is called when an item in the courses list is clicked.
	 * 
	 * All we need to do is change the information that user can see in CourseDetailFragment.
	 */
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		Object course = mAdapter.getItem(position);
		int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = position - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if(wantedChild < 0 || wantedChild >= listView.getChildCount()) {
			Toast.makeText(getActivity(), getString(R.string.error_selecting_item), Toast.LENGTH_LONG).show();
			return;
		}
		
		// Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
		mAdapter.setSelectedItem(wantedChild, position);
		
		if(course instanceof Course) {
			setCourse((Course) course);
		}
	}
	
	/**
	 * Programmatically check item. If there are not items then show the message.
	 */
	public void selectFirstCourse() {
		if(mCoursesList.size() > 0) {
			getListView().setItemChecked(0, true);
			Course course = (Course) getListView().getItemAtPosition(0);
			setCourse(course);
		}
		else {
			thereAreNotCourses();
		}
	}
}
