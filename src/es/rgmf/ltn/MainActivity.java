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

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import es.rgmf.ltn.fragments.CourseStudentFragment;
import es.rgmf.ltn.fragments.StudentCourseEvaluationFragment;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.orm.Evaluation;
import es.rgmf.ltn.util.Dates;
import es.rgmf.ltn.util.Session;

/**
 * Main activity of the app.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	private static final String SECTION_NUMBER = "section_number";
	
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private int mSectionNumber;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    
    private Menu mMenu;

    /**
     * Method that is called when this activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	/************* If you want to see where the database is in your device ***********
    	File dbFile = getDatabasePath("libreteachernotebook.db");
    	Log.i("Database path", dbFile.getAbsolutePath());
    	*********************************************************************************/
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);

        /************ Create and set up the navigation drawer **************/
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        /********************************************************************/
        
        if(savedInstanceState == null) {
        	initSession();
        }
        
        // Restore the section number.
        if(savedInstanceState != null) {
        	mSectionNumber = savedInstanceState.getInt(SECTION_NUMBER);
        	onSectionAttached(mSectionNumber);
        }
    }
    
    /**
     * Clear resources calling garbage collection.
     */
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	System.gc();
        Runtime.getRuntime().gc();
    }
    
    /**
     * Save the section number to restore after screen rotation.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putInt(SECTION_NUMBER, this.mSectionNumber);
    }

    /**
     * This method insert generic information for the application in the Session object.
     */
	private void initSession() {
		// Initialize evaluations information.
		ArrayList<Evaluation> evaluationsList = ReaderModel.getEvaluations(this);
		if(evaluationsList.size() > 0) {
			Session.setEvaluations(evaluationsList);
		} 
		long today = Calendar.getInstance().getTimeInMillis();
		int idx = 0;
		int i = 0;
		for (Evaluation e : evaluationsList) {
			if (Dates.getCalendarFromString(e.getStartDate()).getTimeInMillis() <= today && 
				Dates.getCalendarFromString(e.getEndDate()).getTimeInMillis() >= today) {
				idx = i;
				break;
			}
			i++;
		}
		Session.setEvaluationIndex(idx);
		
		// Initialize courses fragment selected in the detail fragment section (see StudentCourseEvaluationFragment).
		ArrayList<Integer> coursesId = ReaderModel.getCoursesId(this);
		if (coursesId.size() > 0) {
			Session.initializeCourseFragmentSelected(coursesId, StudentCourseEvaluationFragment.HOME);
		}
	}

	/**
     * This method is called when user click over a section into Navigation Drawer.
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
    	FragmentManager fragmentManager = getFragmentManager();
    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    	
    	switch(position) {
    	case 0:
    		fragmentTransaction.replace(R.id.container, PlaceholderFragment.newInstance(position + 1));
    		break;
    	default:
    		if(mNavigationDrawerFragment != null) {
	    		Integer courseId = mNavigationDrawerFragment.getCourseSelectedId(position);
	    		if(courseId != null) {
		    		fragmentTransaction.replace(R.id.container, CourseStudentFragment.newInstance(position + 1, courseId));
	    		}
	    		else {
	    			fragmentTransaction.replace(R.id.container, PlaceholderFragment.newInstance(1));
	    		}
    		}
    		break;
    	}
    	
    	fragmentTransaction.commit();
    }

    /**
     * This method is called when a section is attached and the only thing we do is 
     * set up the section title to show in the bar.
     * 
     * @param number Position of the menu item clicked.
     */
    public void onSectionAttached(int number) {
    	if(mNavigationDrawerFragment != null) { 
    		mSectionNumber = number;
    		mTitle = mNavigationDrawerFragment.getTitleOfSection(number - 1);
    	}
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	menu.clear();
    	getMenuInflater().inflate(R.menu.main, menu);
    	mMenu = menu;
    	MenuItem setEvaluationItem = menu.findItem(R.id.action_set_evaluation);
    	if(setEvaluationItem != null) {
	    	SubMenu subMenuEvaluation = setEvaluationItem.getSubMenu();
	    	ArrayList<Evaluation> el = Session.getEvaluations();
	    	if(el != null) {
	    		for(int i = 0; i < el.size(); i++) {
	    			subMenuEvaluation.add(0, i, Menu.NONE, el.get(i).getName());
	    		}
	    		
	    		if(Session.getEvaluationIndex() != null) {
	    			setEvaluation(Session.getEvaluationIndex());
	    		}
	    	}
	    	else {
	    		Session.clearEvaluations();
	    	}
    	}
    	return super.onPrepareOptionsMenu(menu);
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        
        // It sees if the user has selected one of the evaluation to setting.
        ArrayList<Evaluation> el = Session.getEvaluations();
    	if(el != null) {
    		for(int i = 0; i < el.size(); i++) {
    			if(id == i) {
    				//Toast.makeText(this, el.get(i).getName(), Toast.LENGTH_SHORT).show();
    				setEvaluation(i);
    				// Go to home screen.
    				onNavigationDrawerItemSelected(mSectionNumber - 1);
    				return true;
    			}
    		}
    	}
        
        return super.onOptionsItemSelected(item);
    }

	/**
	 * Set the "Set Evaluation" menu in action bar with the name of the evaluation
	 * selected.
	 * 
	 * @param idx The index of the evaluation list where is the evaluation selected.
	 */
    private void setEvaluation(Integer idx) {
    	ArrayList<Evaluation> evals = Session.getEvaluations();
    	if(evals.size() > 0) {
	    	mMenu.findItem(R.id.action_set_evaluation).setTitle(evals.get(idx).getName());
			Session.setEvaluationIndex(idx);
    	}
	}

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String SECTION_NUMBER = "section_number";
        
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private int mSectionNumber;
        
        /**
         * The root view.
         */
        View mRootView;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.mSectionNumber = sectionNumber;
            return fragment;
        }
        
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	super.onCreateView(inflater, container, savedInstanceState);
            mRootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            if(savedInstanceState != null)
            	mSectionNumber = savedInstanceState.getInt(SECTION_NUMBER);
            
            return mRootView;
        }
        
        @Override
        public void onResume() {
        	super.onResume();
        	// If there are not courses disable the attendance option.
            boolean areThereCourses = ReaderModel.areThereCourses(getActivity());
            if (!areThereCourses) {
            	mRootView.findViewById(R.id.attendance_linear_layout).setVisibility(View.GONE);
            }
            else {
            	mRootView.findViewById(R.id.attendance_linear_layout).setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAttach(Activity activity) {
        	super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(mSectionNumber);
        }
        
        @Override
        public void onSaveInstanceState(Bundle outState) {
        	super.onSaveInstanceState(outState);
        	outState.putInt(SECTION_NUMBER, mSectionNumber);
        }
    }

    /**
     * This method is called when user click on course icon (see {@link fragment_main.xml} where is
     * this method specified).
     * 
     * @param view The View that was clicked (in this case the course icon).
     */
    public void startCourseManagementActivity(View view) {
    	Intent intent = new Intent(this, CourseManagementActivity.class);
    	startActivity(intent);
    }
    
    /**
     * This method is called when user click on evaluation icon (see {@link fragment_main.xml} where is
     * this method specified).
     * 
     * @param view The View that was clicked (in this case the evaluation icon).
     */
    public void startEvaluationManagementActivity(View view) {
    	Intent intent = new Intent(this, EvaluationManagementActivity.class);
    	startActivity(intent);
    }
    
    /**
     * This method is called when user click on attendance icon (see {@link fragment_main.xml} where is
     * this method specified).
     * 
     * @param view The View that was clicked (in this case the attendance icon).
     */
    public void startAttendanceManagementActivity(View view) {
    	Intent intent = new Intent(this, AttendanceManagementActivity.class);
    	startActivity(intent);
    }
}
