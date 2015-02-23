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

package es.rgmf.ltn;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * The Attendance Management Activity.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class AttendanceManagementActivity extends FragmentActivity {
	/**
	 * Method that is called when this activity is created.
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_management);
    }
	
	/**
	 * This method place the menu items defined in {@link attendance_management_actions.xml} into
	 * the action bar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.attendance_management_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * When the user presses one of the action buttons or another item in the action overflow, 
	 * the system calls your activity's onOptionsItemSelected() callback method.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		return false;
		/*
	    switch (item.getItemId()) {
	        case R.id.action_add_course:
	        	// Open the form to create a new course (open an activity that do that).
	        	Intent intent = new Intent(this, AddCourseActivity.class);
	        	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    */
	}
}
