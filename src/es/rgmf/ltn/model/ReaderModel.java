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

package es.rgmf.ltn.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.widget.SimpleCursorAdapter;
import es.rgmf.ltn.R;
import es.rgmf.ltn.db.DBAdapter;
import es.rgmf.ltn.model.orm.Attendance;
import es.rgmf.ltn.model.orm.AttendanceEvent;
import es.rgmf.ltn.model.orm.AttitudeStudentMark;
import es.rgmf.ltn.model.orm.Course;
import es.rgmf.ltn.model.orm.CourseStudent;
import es.rgmf.ltn.model.orm.Evaluation;
import es.rgmf.ltn.model.orm.ExamStudentMark;
import es.rgmf.ltn.model.orm.PracticeStudentMark;
import es.rgmf.ltn.model.orm.Student;

/**
 * Provides methods for reading data from LibreTeacherNotebook's database.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class ReaderModel {
	/**
	 * Return all courses.
	 * 
	 * @param context The context application.
	 * @return The courses list.
	 */
	public static ArrayList<Course> getCourses(Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		ArrayList<Course> courses = dbAdapter.getCourses();
		dbAdapter.close();
		
		return courses;
	}
	
	/**
	 * Return all courses identifies.
	 * 
	 * @param context The context application.
	 * @return The courses list.
	 */
	public static ArrayList<Integer> getCoursesId(Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		ArrayList<Integer> coursesId = dbAdapter.getCoursesId();
		dbAdapter.close();
		
		return coursesId;
	}
	
	/**
	 * Return the course with the id specified.
	 */
	public static Course getCourse(Context context, String id) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		Course course = dbAdapter.getCourse(id);
		dbAdapter.close();
		
		return course;
	}
	
	/**
	 * Return the student with the id specified.
	 */
	public static Student getStudent(Context context, String id) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		Student student = dbAdapter.getStudent(id);
		dbAdapter.close();
		
		return student;
	}
	
	/**
	 * This method retrieve students of the course and return they. The students list
	 * is ordered.
	 * 
	 * @param context The context application.
	 * @param courseId The course id.
	 * @return The students list ordered by last name.
	 */
	public static ArrayList<Student> getCourseStudents(Context context, int courseId) {
		
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		ArrayList<Student> result = dbAdapter.getCourseStudents(courseId);
		dbAdapter.close();
		
		return result;
	}

	/**
	 * This method retrieve all evaluations and return they.
	 * 
	 * @param cintext The context application.
	 * @return An array list with all evaluations.
	 */
	public static ArrayList<Evaluation> getEvaluations(Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		ArrayList<Evaluation> evaluations = dbAdapter.getEvaluations();
		dbAdapter.close();
		
		return evaluations;
	}

	/**
	 * This method retrieve all concepts of the student enrolled in the course.
	 * 
	 * @param context The application context.
	 * @param mCourseId The course identify.
	 * @param mStudentId The student identify.
	 * @return
	 */
	public static ArrayList<ExamStudentMark> getStudentCourseConcepts(
			Context context, int courseId, int studentId) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		ArrayList<ExamStudentMark> concepts = dbAdapter.getStudentCourseConcepts(courseId, studentId);
		dbAdapter.close();
		
		return concepts;
	}

	/**
	 * This method retrieve all procedures of the student enrolled in the course.
	 * 
	 * @param context The application context.
	 * @param courseId The course identify.
	 * @param studentId The student identify.
	 * @return
	 */
	public static ArrayList<PracticeStudentMark> getStudentCourseProcedures(
			Context context, int courseId, int studentId) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		ArrayList<PracticeStudentMark> procedures = dbAdapter.getStudentCourseProcedures(courseId, studentId);
		dbAdapter.close();
		
		return procedures;
	}

	/**
	 * This method retrieve all attitudes of the student enrolled in the course.
	 * 
	 * @param context The application context.
	 * @param courseId The course identify.
	 * @param studentId The student identify.
	 * @return
	 */
	public static ArrayList<AttitudeStudentMark> getStudentCourseAttitudes(
			Context context, int courseId, int studentId) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		ArrayList<AttitudeStudentMark> attitudes = dbAdapter.getStudentCourseAttitudes(courseId, studentId);
		dbAdapter.close();
		
		return attitudes;
	}

	/**
	 * Create a SimpleCursorAdapter object to populate the registers stored in 
	 * all_attitude table into Spinner.
	 * 
	 * @param context
	 * @return
	 */
	public static SimpleCursorAdapter getAllAttitudeAdapter(Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		
		// It creates and extra cursor with the first item that will be showed in the Spinner.
		MatrixCursor extra = new MatrixCursor(new String[] { "_id", "name" });
		extra.addRow(new String[] { "-1", context.getString(R.string.select_an_attitude_input_hint) });
		
		// Get all attitudes that will be in the cursor.
		Cursor cursor = dbAdapter.getCursorAllAttitude();
		
		// Create an array with the two cursors.
		Cursor[] cursors = { extra, cursor };
		
		// It merges the cursors and create teh SimpleCursorAdapter with this cursors merged.
		Cursor extendedCursor = new MergeCursor(cursors);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
        		android.R.layout.simple_list_item_1, extendedCursor, new String[] { "name" }, 
        		new int[] { android.R.id.text1 }, 0);
		
		return adapter;
	}

	/**
	 * Return all attendance events.
	 * 
	 * @param context
	 * @return The array list with all attendance events.
	 */
	public static ArrayList<AttendanceEvent> getAllAttendanceEvents(Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		ArrayList<AttendanceEvent> attitudes = dbAdapter.getAllAttendanceEvents();
		dbAdapter.close();
		
		return attitudes;
	}

	/**
	 * Return the course student object through course and student id.
	 * @param context
	 * @param courseId
	 * @param studentId
	 * @return
	 */
	public static CourseStudent getCourseStudent(Context context,
			Integer courseId, Integer studentId) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		CourseStudent obj = dbAdapter.getCourseStudent(courseId, studentId);
		dbAdapter.close();
		
		return obj;
	}

	/**
	 * Return the Attendance object.
	 * 
	 * @param context
	 * @param date
	 * @param studentId
	 * @param courseId
	 * @return
	 */
	public static Attendance getAttendance(Context context, String date,
			Integer courseId, Integer studentId) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		Attendance obj = dbAdapter.getAttendance(date, courseId, studentId);
		dbAdapter.close();
		
		return obj;
	}
	
	/**
	 * Return all Attendance objects for student/course.
	 * 
	 * @param context
	 * @param studentId
	 * @param courseId
	 * @return
	 */
	public static List<Attendance> getAllStudentCourseAttendance(Context context,
			Integer courseId, Integer studentId) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		List<Attendance> obj = dbAdapter.getAllStudentCourseAttendance(courseId, studentId);
		dbAdapter.close();
		
		return obj;
	}

	/**
	 * Return true if there are courses in the database.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean areThereCourses(Context context) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		boolean res = dbAdapter.areThereCourses();
		dbAdapter.close();
		
		return res;
	}
}
