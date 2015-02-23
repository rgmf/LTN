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

import java.util.List;

import android.content.Context;
import es.rgmf.ltn.db.DBAdapter;
import es.rgmf.ltn.model.orm.Attendance;
import es.rgmf.ltn.model.orm.Course;
import es.rgmf.ltn.model.orm.Evaluation;
import es.rgmf.ltn.model.orm.Student;

/**
 * Provides methods for writing data to LibreTeacherNotebook's database.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class WriterModel {
	/**
	 * Add a course.
	 * 
	 * @param context
	 *            The application context.
	 * @param course
	 *            The course object.
	 * @throws Exception
	 */
	public static void addCourse(Context context, Course course)
			throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.addCourse(course);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Add an evaluation.
	 * 
	 * @param context
	 *            The application context.
	 * @param evaluation
	 *            The evaluation object.
	 * @throws Exception
	 */
	public static void addEvaluation(Context context, Evaluation evaluation)
			throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.addEvaluation(evaluation);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Create a new student into the existent course.
	 * 
	 * @param mContext
	 *            The context application.
	 * @param student
	 *            The student object.
	 * @param mCourseId
	 *            The identifier of the course.
	 * @return
	 * @throws Exception
	 */
	public static void addStudentCourse(Context context, Student student,
			int courseId) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.addStudentCourse(student, courseId);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Add a concept.
	 * 
	 * @param context
	 * @param courseId
	 * @param evaluationId
	 * @param date
	 * @param name
	 * @param description
	 * @param weight
	 * @throws Exception
	 */
	public static void addConcept(Context context, int courseId,
			int evaluationId, String date, String name, String description,
			String weight) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.addConcept(courseId, evaluationId, date, name,
					description, weight);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Add a procedure.
	 * 
	 * @param context
	 * @param courseId
	 * @param evaluationId
	 * @param startDate
	 * @param endDate
	 * @param name
	 * @param description
	 * @param weight
	 * @throws Exception
	 */
	public static void addProcedure(Context context, int courseId,
			Integer evaluationId, String startDate, String endDate,
			String name, String description, String weight) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.addProcedure(courseId, evaluationId, startDate, endDate,
					name, description, weight);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Add an existing attitude to student.
	 * 
	 * @param context
	 * @param courseId
	 * @param studentId
	 * @param attitudeId
	 * @param evaluationId
	 * @param date
	 * @param name
	 * @param description
	 * @param weight
	 * @throws Exception
	 */
	public static void addAttitude(Context context, int courseId,
			int studentId, int attitudeId, int evaluationId, String date,
			String name, String description, String weight) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.addAttitude(courseId, studentId, attitudeId,
					evaluationId, date, name, description, weight);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Add an attitude.
	 * 
	 * @param context
	 * @param courseId
	 * @param evaluationId
	 * @param date
	 * @param name
	 * @param description
	 * @param weight
	 * @throws Exception
	 */
	public static void addAttitude(Context context, int courseId,
			int studentId, int evaluationId, String date, String name,
			String description, String weight) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.addAttitude(courseId, studentId, evaluationId, date,
					name, description, weight);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Add or update all attendances.
	 * 
	 * @param context
	 * @param attendanceList
	 */
	public static void addAttendanceStudents(Context context,
			List<Attendance> attendanceList) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		for (int i = 0; i < attendanceList.size(); i++) {
			if (attendanceList.get(i).getId() != null) {
				dbAdapter.updateAttendance(attendanceList.get(i));
			} else {
				dbAdapter.addAttendance(attendanceList.get(i));
			}
		}
		dbAdapter.close();
	}

	/**
	 * Delete a course.
	 * 
	 * @param id
	 *            The identifier of the course to delete.
	 */
	public static void deleteCourse(Context context, Integer id) {
		deleteCourse(context, String.valueOf(id));
	}

	/**
	 * Delete a course.
	 * 
	 * @param id
	 *            The identifier of the course to delete.
	 * @return true if the course is deleted.
	 */
	public static boolean deleteCourse(Context context, String id) {
		boolean result;
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		result = dbAdapter.deleteCourse(id);
		dbAdapter.close();
		return result;
	}

	/**
	 * Delete a evaluation.
	 * 
	 * @param context
	 *            The application context.
	 * @param id
	 *            The evaluation id.
	 * @return true if the evaluation is deleted.
	 */
	public static boolean deleteEvaluation(Context context, String id) {
		boolean result;
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		result = dbAdapter.deleteEvaluation(id);
		dbAdapter.close();
		return result;
	}

	/**
	 * Delete the student.
	 * 
	 * @param context
	 *            The application context.
	 * @param id
	 *            The student identifier.
	 * @return true if the student is deleted.
	 */
	public static boolean deleteStudent(Context context, String id) {
		boolean result;
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		result = dbAdapter.deleteStudent(id);
		dbAdapter.close();
		return result;
	}

	/**
	 * Delete an ExamStudentMark.
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static boolean deleteExamStudentMark(Context context, String id) {
		boolean result;
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		result = dbAdapter.deleteExamStudentMark(id);
		dbAdapter.close();
		return result;
	}

	/**
	 * Delete a PracticeStudentMark.
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static boolean deletePracticeStudentMark(Context context, String id) {
		boolean result;
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		result = dbAdapter.deletePracticeStudentMark(id);
		dbAdapter.close();
		return result;
	}

	/**
	 * Delete a AttitudeStudentMark.
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static boolean deleteAttitudeStudentMark(Context context, String id) {
		boolean result;
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		result = dbAdapter.deleteAttitudeStudentMark(id);
		dbAdapter.close();
		return result;
	}

	/**
	 * Update the course object.
	 * 
	 * @param context
	 *            The context of the application.
	 * @param course
	 *            The course object.
	 */
	public static void updateCourse(Context context, Course course) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		dbAdapter.updateCourse(course);
		dbAdapter.close();
	}

	/**
	 * Update the evaluation object.
	 * 
	 * @param context
	 *            The context of the application.
	 * @param course
	 *            The evaluation object.
	 */
	public static void updateEvaluation(Context context, Evaluation evaluation) {
		DBAdapter dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		dbAdapter.updateEvaluation(evaluation);
		dbAdapter.close();
	}

	/**
	 * Update a concept.
	 * 
	 * @param context
	 * @param examStudentMarkId
	 * @param courseId
	 * @param evaluationId
	 * @param date
	 * @param name
	 * @param description
	 * @param weight
	 * @throws Exception
	 */
	public static void updateConcept(Context context, int examId, String date,
			String name, String description, String weight) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.updateConcept(examId, date, name, description, weight);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Update a procedure.
	 * 
	 * @param context
	 * @param examStudentMarkId
	 * @param courseId
	 * @param evaluationId
	 * @param date
	 * @param name
	 * @param description
	 * @param weight
	 * @throws Exception
	 */
	public static void updateProcedure(Context context, int practiceId,
			String startDate, String endDate, String name, String description,
			String weight) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.updateProcedure(practiceId, startDate, endDate, name,
					description, weight);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Update an attitude.
	 * 
	 * @param context
	 * @param attitudeId
	 * @param attitudeStudentMarkId
	 * @param date
	 * @param name
	 * @param description
	 * @param weight
	 * @throws Exception
	 */
	public static void updateAttitude(Context context, Integer attitudeId,
			Integer attitudeStudentMarkId, String date, String name,
			String description, String weight) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.updateAttitude(attitudeId, attitudeStudentMarkId, date,
					name, description, weight);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Update an attitude.
	 * 
	 * @param context
	 * @param attitudeId
	 * @param attitudeStudentMarkId
	 * @param date
	 * @param weight
	 * @throws Exception
	 */
	public static void updateAttitude(Context context, Integer attitudeId,
			Integer allAttitudeId, Integer attitudeStudentMarkId, String date,
			String weight) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.updateAttitude(attitudeId, allAttitudeId,
					attitudeStudentMarkId, date, weight);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Update a student.
	 * 
	 * @param context
	 * @param student
	 */
	public static void updateStudent(Context context, Student student)
			throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.updateStudent(student);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Update the ExamStudentMark.
	 * 
	 * @param context
	 *            The context of the application.
	 * @param id
	 *            The identify of register to update.
	 * @param mark
	 *            The value of the mark.
	 * @param comment
	 *            The comment the teacher insert to the student mark.
	 * @throws Exception
	 */
	public static void setExamStudentMark(Context context, Integer id,
			float mark, String comment) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.updateExamStudentMark(id, mark, comment);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Update all ExamStudentMark mark for all students of the course identify to
	 * exam identify.
	 *  
	 * @param context The context.
	 * @param courseId The course identifier.
	 * @param examId The exam identifier.
	 * @param mark The mark for all students.
	 * @param comment The comment for all students.
	 * @throws Exception
	 */
	public static void setExamForAllStudentMark(Context context,
			Integer courseId, Integer examId, Float mark, String comment) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.updateExamStudentMarkForAllStudents(courseId, examId, mark, comment);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Update the PracticeStudentMark mark.
	 * 
	 * @param context
	 *            The application context.
	 * @param id
	 *            The identify of register to update.
	 * @param mark
	 *            The value of the mark.
	 * @param comment
	 *            A comment the teacher insert about mark student.
	 */
	public static void setPracticeStudentMark(Context context, Integer id,
			float mark, String comment) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.updatePracticeStudentMark(id, mark, comment);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Update all PracticeStudentMark mark for all students of the course identify
	 * to the practice identifier.
	 *  
	 * @param context The context.
	 * @param courseId The course identifier.
	 * @param practicId The practice identifier.
	 * @param mark The mark for all students.
	 * @param comment The comment for all students.
	 * @throws Exception
	 */
	public static void setPracticeForAllStudentMark(Context context,
			Integer courseId, Integer practiceId, Float mark, String comment) throws Exception {
		try {
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.updatePracticeStudentMarkForAllStudents(courseId, practiceId, mark, comment);
			dbAdapter.close();
		} catch (Exception e) {
			throw e;
		}
	}
}
