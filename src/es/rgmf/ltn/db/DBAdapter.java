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

package es.rgmf.ltn.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import es.rgmf.ltn.model.orm.AllAttitude;
import es.rgmf.ltn.model.orm.Attendance;
import es.rgmf.ltn.model.orm.AttendanceEvent;
import es.rgmf.ltn.model.orm.Attitude;
import es.rgmf.ltn.model.orm.AttitudeStudentMark;
import es.rgmf.ltn.model.orm.CheckFieldException;
import es.rgmf.ltn.model.orm.Course;
import es.rgmf.ltn.model.orm.CourseEvaluation;
import es.rgmf.ltn.model.orm.CourseStudent;
import es.rgmf.ltn.model.orm.Evaluation;
import es.rgmf.ltn.model.orm.Exam;
import es.rgmf.ltn.model.orm.ExamStudentMark;
import es.rgmf.ltn.model.orm.Practice;
import es.rgmf.ltn.model.orm.PracticeStudentMark;
import es.rgmf.ltn.model.orm.Student;
import es.rgmf.ltn.model.orm.Test;
import es.rgmf.ltn.util.Session;

public class DBAdapter {
	private static DBHelper dbHelperSingleton;
	private SQLiteDatabase db;
	private Context context;
	
	/**
	 * Constructor.
	 * 
	 * @param ctx The application context.
	 */
	public DBAdapter(Context ctx) {
		context = ctx;
		dbHelperSingleton = getDbHelperSingleton(context);
	}
	
	/**
	 * Return the DBHelper.
	 * 
	 * @param context The application context.
	 * @return The DBHelper object.
	 */
	private static DBHelper getDbHelperSingleton(Context context) {
        if (dbHelperSingleton == null) {
            dbHelperSingleton = new DBHelper(context);
        }
        return dbHelperSingleton;
    }
	
	/**
	 * Open the connection if necessary.
	 * 
	 * @return the DBAdapter.
	 */
	public DBAdapter open() {
        if (db == null || !db.isOpen() || db.isReadOnly()) {
            try {
                db = dbHelperSingleton.getWritableDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
                db = dbHelperSingleton.getReadableDatabase();
            }
        }
        return this;
    }

	/**
	 * Close the connection.
	 */
    public void close() {
        db.close();
    }
    
    /**
     * Add course to database.
	 * 
	 * When it adds a course its associate to all evaluations.
     * 
     * @param course The course to add.
     * @return The id of the course inserted or -1 if error.
     */
    public long addCourse(Course course) {
    	long courseId = -1;
    	
    	try {
    		db.beginTransaction();
    		
	    	ContentValues values = new ContentValues();
	    	values.put(DBHelper.LEVEL_FIELD_NAME, course.getLevel());
	    	values.put(DBHelper.NAME_FIELD_NAME, course.getName());
	    	values.put(DBHelper.GROUP_FIELD_NAME, course.getGroup());
	    	values.put(DBHelper.TUTOR_FIELD_NAME, course.getTutor());
	    	values.put(DBHelper.CONCEPT_WEIGHT_FIELD_NAME, course.getConceptWeight());
	    	values.put(DBHelper.PROCEDURE_WEIGHT_FIELD_NAME, course.getProcedureWeight());
	    	values.put(DBHelper.ATTITUDE_WEIGHT_FIELD_NAME, course.getAttitudeWeight());
	    	courseId = db.insert(DBHelper.COURSE_TBL_NAME, null, values);
	    	
	    	if(courseId != -1) {
	    		ArrayList<Evaluation> evals = getEvaluations();
	    		for(int i = 0; i < evals.size(); i++) {
	    			addCourseEvaluation(courseId, evals.get(i).getId());
	    		}
	    	}
	    	
	    	db.setTransactionSuccessful();
    	} catch(SQLException e) {
    		throw e;
    	} finally {
     	   db.endTransaction();
    	}
    	
    	return courseId;
    }
    
    /**
     * Add evaluation to database.
     * 
     * When it adds an evaluation it is associated with all courses.
     * 
     * @param evaluation The evaluation to add.
     * @return The id of the evaluation inserted or -1 if error.
     */
    public long addEvaluation(Evaluation evaluation) {
    	long evaluationId = -1;
    	
    	try {
    		db.beginTransaction();
    		
	    	ContentValues values = new ContentValues();
	    	values.put(DBHelper.NAME_FIELD_NAME, evaluation.getName());
	    	values.put(DBHelper.START_DATE_FIELD_NAME, evaluation.getStartDate());
	    	values.put(DBHelper.END_DATE_FIELD_NAME, evaluation.getEndDate());
			evaluationId = db.insert(DBHelper.EVALUATION_TBL_NAME, null, values);
			
			if(evaluationId != -1) {
	    		ArrayList<Course> courses = getCourses();
	    		for(int i = 0; i < courses.size(); i++) {
	    			addCourseEvaluation(courses.get(i).getId(), evaluationId);
	    		}
	    	}
			
			db.setTransactionSuccessful();
    	} catch(SQLException e) {
    		throw e;
    	} finally {
    		db.endTransaction();
    	}
		
		return evaluationId;
    }
    
    /**
     * Add student to database.
     * 
     * @param student The student to add.
     * @return The id of the student inserted or -1 if error.
     */
    public long addStudent(Student student) {
    	ContentValues values = new ContentValues();
    	values.put(DBHelper.NAME_FIELD_NAME, student.getName());
    	values.put(DBHelper.LASTNAME_FIELD_NAME, student.getLastname());
    	values.put(DBHelper.PHOTO_FIELD_NAME, student.getPhoto());
    	return db.insert(DBHelper.STUDENT_TBL_NAME, null, values);
	}
    
    /**
     * Enrolled a student in the course identify.
     * 
     * @param student
     * @param courseId
     */
    public void addStudentCourse(Student student, int courseId) {
    	try {
    		db.beginTransaction();
			long studentId = addStudent(student);
			if(studentId != -1)
				addCourseStudent(courseId, studentId);
			db.setTransactionSuccessful();
    	} catch(SQLException e) {
    		throw e;
    	} finally {
    		db.endTransaction();
    	}
	}
    
    /**
     * Add a register into course_student table where is relation course
     * and student.
     * 
     * @param courseId The id of the course.
     * @param studentId The id of the student.
     * @return The id of the row inserted or -1 if error.
     */
    public long addCourseStudent(long courseId, long studentId) {
    	ContentValues values = new ContentValues();
    	values.put(DBHelper.ID_COURSE_FIELD_NAME, courseId);
    	values.put(DBHelper.ID_STUDENT_FIELD_NAME, studentId);
    	return db.insert(DBHelper.COURSE_STUDENT_TBL_NAME, null, values);
	}
    
    /**
     * Add a new concept. To insert a concept it needs to insert a Test and an Exam.
     * It is doing with transaction.
     * 
     * Also it needs to insert a register for each student enrolled in a course in the
     * table exam_student_mark (ExamStudentMark object).
     * 
     * @param courseId
     * @param evaluationId
     * @param date
     * @param name
     * @param description
     * @param weight
     * @return The exam identify inserted.
     */
    public long addConcept(int courseId, int evaluationId, String date,
			String name, String description, String weight) {
		long examId = -1;
    	
    	try {
    		db.beginTransaction();
			CourseEvaluation courseEvaluation = getCourseEvaluation(courseId, evaluationId);
			if(courseEvaluation != null) {
				long testId = addTest(name, description, weight, courseEvaluation.getId());
				if(testId != -1) {
					examId = addExam(date, testId);
					if(examId != -1) {
						createExamStudentRegisterForEachStudent(courseId, examId);
					}
				}
			}
			db.setTransactionSuccessful();
    	} catch(SQLException e) {
    		throw e;
    	} finally {
    	   db.endTransaction();
    	}
		
    	return examId;
	}
    
    /**
     * Add a procedure. To insert a procedure it needs to insert a Test and a Practice.
     * It is doing with transaction.
     * 
     * Also it needs to insert a register for each student enrolled in a course in the
     * table practice_student_mark (PracticeStudentMark object).
     * 
     * @param courseId
     * @param evaluationId
     * @param startDate
     * @param endDate
     * @param name
     * @param description
     * @param weight
     * @return The practice identify inserted.
     */
    public long addProcedure(int courseId, Integer evaluationId,
			String startDate, String endDate, String name, String description,
			String weight) {
    	long practiceId = -1;
    	
    	try {
    		db.beginTransaction();
			CourseEvaluation courseEvaluation = getCourseEvaluation(courseId, evaluationId);
			if(courseEvaluation != null) {
				long testId = addTest(name, description, weight, courseEvaluation.getId());
				if(testId != -1) {
					practiceId = addPractice(startDate, endDate, testId);
					if(practiceId != -1) {
						createPracticeStudentRegisterForEachStudent(courseId, practiceId);
					}
				}
			}
			db.setTransactionSuccessful();
    	} catch(SQLException e) {
    		throw e;
    	} finally {
    	   db.endTransaction();
    	}
		
    	return practiceId;
	}
    
    /**
     * Add an existing attitude to student.
     * 
     * @param courseId
     * @param studentId
     * @param allAttitudeId
     * @param evaluationId
     * @param date
     * @param name
     * @param description
     * @param weight
     */
    public long addAttitude(int courseId, int studentId, int allAttitudeId,
			int evaluationId, String date, String name, String description,
			String weight) {
    	long attitudeId = -1;
    	
    	try {
    		db.beginTransaction();
			CourseEvaluation courseEvaluation = getCourseEvaluation(courseId, evaluationId);
			if(courseEvaluation != null) {
				attitudeId = addAttitude(allAttitudeId, weight, courseEvaluation.getId());
				addAttitudeStudent(studentId, date, attitudeId);
			}
			db.setTransactionSuccessful();
    	} catch(SQLException e) {
    		throw e;
    	} finally {
    	   db.endTransaction();
    	}
		
    	return attitudeId;
	}
    
    /**
     * Add the attitude id in the attitude table.
     * 
     * @param attitudeId
     * @param weight
     * @param id
     * @return
     */
    private long addAttitude(int allAttitudeId, String weight, Integer courseEvaluationId) {
    	ContentValues values = new ContentValues();
    	values.put(DBHelper.WEIGHT_FIELD_NAME, weight);
    	values.put(DBHelper.COURSE_EVALUATION_FIELD_NAME, courseEvaluationId);
    	values.put(DBHelper.ALL_ATTITUDE_FIELD_NAME, allAttitudeId);
    	return db.insert(DBHelper.ATTITUDE_TBL_NAME, null, values);
	}

	/**
     * Add an attitude to the student to the course.
     * 
     * @param courseId
     * @param studentId
     * @param evaluationId
     * @param date
     * @param name
     * @param description
     * @param weight
     */
    public long addAttitude(int courseId, int studentId, int evaluationId, String date,
			String name, String description, String weight) {
    	long attitudeId = -1;
    	
    	try {
    		db.beginTransaction();
			CourseEvaluation courseEvaluation = getCourseEvaluation(courseId, evaluationId);
			if(courseEvaluation != null) {
				long allAttitudeId = addAllAttitude(name, description);
				if(allAttitudeId != -1) {
					attitudeId = addAttitude(allAttitudeId, weight, courseEvaluation.getId());
					if(attitudeId != -1) {
						addAttitudeStudent(studentId, date, attitudeId);
					}
				}
			}
			db.setTransactionSuccessful();
    	} catch(SQLException e) {
    		throw e;
    	} finally {
    	   db.endTransaction();
    	}
		
    	return attitudeId;
	}

    /**
     * Add the attitude to the student enrolled in the course.
     * 
     * @param studentId
     * @param date
     * @param attitudeId
     * @return
     */
    private long addAttitudeStudent(int studentId, String date, long attitudeId) {
    	ContentValues values = new ContentValues();
    	values.put(DBHelper.STUDENT_FIELD_NAME, studentId);
    	values.put(DBHelper.ATTITUDEDATE_FIELD_NAME, date);
    	values.put(DBHelper.ATTITUDE_FIELD_NAME, attitudeId);
    	return db.insert(DBHelper.ATTITUDE_STUDENT_MARK_TBL_NAME, null, values);
	}

    /**
     * Add the attitude with weight and the course evaluation identify.
     * 
     * @param weight
     * @param courseEvaluationId
     * @return
     */
	private long addAttitude(long attitudeId, String weight, Integer courseEvaluationId) {
		ContentValues values = new ContentValues();
    	values.put(DBHelper.ALL_ATTITUDE_FIELD_NAME, attitudeId);
    	values.put(DBHelper.WEIGHT_FIELD_NAME, weight);
    	values.put(DBHelper.COURSE_EVALUATION_FIELD_NAME, courseEvaluationId);
    	return db.insert(DBHelper.ATTITUDE_TBL_NAME, null, values);
	}

	/**
	 * Add the attitude into AllAttitude object (all_attitude table).
	 * 
	 * @param name The name of the attitude.
	 * @param description The description of the attitude.
	 * @return
	 */
	private long addAllAttitude(String name, String description) {
		ContentValues values = new ContentValues();
    	values.put(DBHelper.NAME_FIELD_NAME, name);
    	values.put(DBHelper.DESCRIPTION_FIELD_NAME, description);
    	return db.insert(DBHelper.ALL_ATTITUDE_TBL_NAME, null, values);
	}

	/**
     * Create a register for each student enrolled in the course for the exam.
     * 
     * @param courseId
     * @param examId
     */
	private void createExamStudentRegisterForEachStudent(int courseId, long examId) {
    	try {
    		ArrayList<Student> students = getCourseStudents(courseId);
    		ContentValues values;
    		for(int i = 0; i < students.size(); i++) {
	    		values = new ContentValues();
	        	values.put(DBHelper.STUDENT_FIELD_NAME, students.get(i).getId());
	        	values.put(DBHelper.EXAM_FIELD_NAME, examId);
	        	db.insert(DBHelper.EXAM_STUDENT_MARK_TBL_NAME, null, values);
    		}
    	} catch(SQLException e) {
    		throw e;
    	}
	}
	
	/**
	 * Create a register for each student enrolled in the course for the practice.
	 * 
	 * @param courseId
	 * @param practiceId
	 */
	private void createPracticeStudentRegisterForEachStudent(int courseId, long practiceId) {
		try {
    		ArrayList<Student> students = getCourseStudents(courseId);
    		ContentValues values;
    		for(int i = 0; i < students.size(); i++) {
	    		values = new ContentValues();
	        	values.put(DBHelper.STUDENT_FIELD_NAME, students.get(i).getId());
	        	values.put(DBHelper.PRACTICE_FIELD_NAME, practiceId);
	        	db.insert(DBHelper.PRACTICE_STUDENT_MARK_TBL_NAME, null, values);
    		}
    	} catch(SQLException e) {
    		throw e;
    	}
	}

	/**
     * Add an exam with the exam date and test identify fields.
     * 
     * @param date
     * @param testId
     * @return
     */
    public long addExam(String date, long testId) {
    	long result = -1;
    	
    	try {
    		ContentValues values = new ContentValues();
        	values.put(DBHelper.EXAMDATE_FIELD_NAME, date);
        	values.put(DBHelper.ID_FIELD_NAME, testId);
        	result = db.insert(DBHelper.EXAM_TBL_NAME, null, values);
    	} catch(SQLException e) {
    		throw e;
    	}
    	
		return result;
	}
    
    /**
     * Add a practice with the start date, end date and test identify fields.
     * 
     * @param startDate
     * @param endDate
     * @param testId
     * @return
     */
    public long addPractice(String startDate, String endDate, long testId) {
    	long result = -1;
    	
    	try {
    		ContentValues values = new ContentValues();
        	values.put(DBHelper.START_DATE_2_FIELD_NAME, startDate);
        	values.put(DBHelper.END_DATE_2_FIELD_NAME, endDate);
        	values.put(DBHelper.ID_FIELD_NAME, testId);
        	result = db.insert(DBHelper.PRACTICE_TBL_NAME, null, values);
    	} catch(SQLException e) {
    		throw e;
    	}
    	
		return result;
	}

    /**
     * Add an test with name, description, weight and course evaluation identify.
     * 
     * @param name
     * @param description
     * @param weight
     * @param courseEvaluationId
     * @return
     */
	public long addTest(String name, String description, String weight,
			long courseEvaluationId) {
		long result = -1;
		
		try {
    		ContentValues values = new ContentValues();
        	values.put(DBHelper.NAME_FIELD_NAME, name);
        	values.put(DBHelper.DESCRIPTION_FIELD_NAME, description);
        	values.put(DBHelper.WEIGHT_FIELD_NAME, weight);
        	values.put(DBHelper.COURSE_EVALUATION_FIELD_NAME, courseEvaluationId);
        	result = db.insert(DBHelper.TEST_TBL_NAME, null, values);
    	} catch(SQLException e) {
    		throw e;
    	}
		
		return result;
	}

	/**
	 * Add a new course evaluation register with course and evaluation identifies.
	 * 
	 * @param courseId
	 * @param evaluationId
	 * @return
	 */
	public long addCourseEvaluation(long courseId, long evaluationId) {
		long result = -1;
    	
    	try {
    		ContentValues values = new ContentValues();
        	values.put(DBHelper.ID_COURSE_FIELD_NAME, courseId);
        	values.put(DBHelper.ID_EVALUATION_FIELD_NAME, evaluationId);
        	result = db.insert(DBHelper.COURSE_EVALUATION_FIELD_NAME, null, values);
    	} catch(SQLException e) {
    		throw e;
    	}
    	
		return result;
	}
	
	/**
	 * Add attendance object to database.
	 * 
	 * @param attendance
	 */
	public long addAttendance(Attendance attendance) {
		long result = -1;
		
		try {
			ContentValues values = new ContentValues();
			values.put(DBHelper.ATTENDANCE_DATE_FIELD_NAME, attendance.getAttendanceDate());
			values.put(DBHelper.EVENT_FIELD_NAME, attendance.getEvent().getId());
        	values.put(DBHelper.COURSE_STUDENT_FIELD_NAME, attendance.getCourseStudent().getId());
        	result = db.insert(DBHelper.ATTENDANCE_TBL_NAME, null, values);
		} catch(SQLException e) {
			throw e;
		}
		
		return result;
	}

	/**
     * Delete the course that is identified by id.
     * 
     * @param id The id of the course.
     * @return true if the course is correctly deleted.
     */
	public boolean deleteCourse(String id) {
		return db.delete(DBHelper.COURSE_TBL_NAME, 
				DBHelper.ID_FIELD_NAME + "=" + id,
				null) > 0;
	}
	
	/**
     * Delete the evaluation that is identified by id.
     * 
     * @param id The id of the evaluation.
     * @return true if the evaluation is correctly deleted.
     */
	public boolean deleteEvaluation(String id) {
		return db.delete(DBHelper.EVALUATION_TBL_NAME, 
				DBHelper.ID_FIELD_NAME + "=" + id,
				null) > 0;
	}
	
	/**
	 * Delete the student that is identified by id.
	 * 
	 * @param id the id of the student.
	 * @return true if the student is correctly deleted.
	 */
	public boolean deleteStudent(String id) {
		return db.delete(DBHelper.STUDENT_TBL_NAME,
				DBHelper.ID_FIELD_NAME + "=" + id,
				null) > 0;
	}
	
	/**
	 * Delete the ExamStudentMark object.
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteExamStudentMark(String id) {
		return db.delete(DBHelper.EXAM_STUDENT_MARK_TBL_NAME,
				DBHelper.ID_FIELD_NAME + "=" + id,
				null) > 0;
	}
	
	/**
	 * Delete the PracticeStudentMark object.
	 * 
	 * @param id
	 * @return
	 */
	public boolean deletePracticeStudentMark(String id) {
		return db.delete(DBHelper.PRACTICE_STUDENT_MARK_TBL_NAME,
				DBHelper.ID_FIELD_NAME + "=" + id,
				null) > 0;
	}
	
	/**
	 * Delete the AttitudeStudentMark object.
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteAttitudeStudentMark(String id) {
		return db.delete(DBHelper.ATTITUDE_STUDENT_MARK_TBL_NAME,
				DBHelper.ID_FIELD_NAME + "=" + id,
				null) > 0;
	}
	
	/**
	 * Update the course.
	 * 
	 * @param course the object course.
	 */
	public void updateCourse(Course course) {
		ContentValues values = new ContentValues();
    	values.put(DBHelper.LEVEL_FIELD_NAME, course.getLevel());
    	values.put(DBHelper.NAME_FIELD_NAME, course.getName());
    	values.put(DBHelper.GROUP_FIELD_NAME, course.getGroup());
    	values.put(DBHelper.TUTOR_FIELD_NAME, course.getTutor());
    	values.put(DBHelper.CONCEPT_WEIGHT_FIELD_NAME, course.getConceptWeight());
    	values.put(DBHelper.PROCEDURE_WEIGHT_FIELD_NAME, course.getProcedureWeight());
    	values.put(DBHelper.ATTITUDE_WEIGHT_FIELD_NAME, course.getAttitudeWeight());
		db.update(DBHelper.COURSE_TBL_NAME, values, 
				DBHelper.ID_FIELD_NAME + "=" + String.valueOf(course.getId()), null);
	}
	
	/**
	 * Update the evaluation.
	 * 
	 * @param evaluation the object evaluation.
	 */
	public void updateEvaluation(Evaluation evaluation) {
		ContentValues values = new ContentValues();
    	values.put(DBHelper.NAME_FIELD_NAME, evaluation.getName());
    	values.put(DBHelper.START_DATE_FIELD_NAME, evaluation.getStartDate());
    	values.put(DBHelper.END_DATE_FIELD_NAME, evaluation.getEndDate());
		db.update(DBHelper.EVALUATION_TBL_NAME, values, 
				DBHelper.ID_FIELD_NAME + "=" + String.valueOf(evaluation.getId()), null);
	}
	
	/**
	 * Update the mark of the ExamStudentMark.
	 * 
	 * @param id
	 * @param mark
	 * @param comment
	 */
	public void updateExamStudentMark(Integer id, float mark, String comment) {
		try {
			ContentValues values = new ContentValues();
	    	values.put(DBHelper.MARK_FIELD_NAME, mark);
	    	values.put(DBHelper.COMMENT_FIELD_NAME, comment);
			db.update(DBHelper.EXAM_STUDENT_MARK_TBL_NAME, values, 
					DBHelper.ID_FIELD_NAME + "=" + String.valueOf(id), null);
		} catch(SQLException e) {
			throw e;
		}
	}
	
	/**
	 * Update the mark of the ExamStudentMark for all students from course
	 * identifier.
	 * 
	 * @param courseId Course identifier.
	 * @param examId Exam identifier.
	 * @param mark The mark for all students.
	 * @param comment The comment for all students.
	 */
	public void updateExamStudentMarkForAllStudents(Integer courseId, Integer examId, float mark, String comment) {
		try {
			ArrayList<Student> students = getCourseStudents(courseId);
			for (Student student: students) {
				ContentValues values = new ContentValues();
		    	values.put(DBHelper.MARK_FIELD_NAME, mark);
		    	values.put(DBHelper.COMMENT_FIELD_NAME, comment);
				db.update(DBHelper.EXAM_STUDENT_MARK_TBL_NAME, values, 
						DBHelper.STUDENT_FIELD_NAME + "=" + String.valueOf(student.getId()) + " AND " +
								DBHelper.EXAM_FIELD_NAME + "=" + examId, null);
			}
		} catch(SQLException e) {
			throw e;
		}
	}
	
	/**
	 * Update the mark of the PracticeStudentMark.
	 * 
	 * @param id
	 * @param mark
	 * @param comment
	 */
	public void updatePracticeStudentMark(Integer id, float mark, String comment) {
		try {
			ContentValues values = new ContentValues();
	    	values.put(DBHelper.MARK_FIELD_NAME, mark);
	    	values.put(DBHelper.COMMENT_FIELD_NAME, comment);
			db.update(DBHelper.PRACTICE_STUDENT_MARK_TBL_NAME, values, 
					DBHelper.ID_FIELD_NAME + "=" + String.valueOf(id), null);
		} catch(SQLException e) {
			throw e;
		}
	}
	
	/**
	 * Update the mark of the PracticeStudentMark for all students from course
	 * identifier.
	 * 
	 * @param courseId Course identifier.
	 * @param practiceId Practice identifier.
	 * @param mark The mark for all students.
	 * @param comment The comment for all students.
	 */
	public void updatePracticeStudentMarkForAllStudents(Integer courseId, Integer practiceId, float mark, String comment) {
		try {
			ArrayList<Student> students = getCourseStudents(courseId);
			for (Student student: students) {
				ContentValues values = new ContentValues();
		    	values.put(DBHelper.MARK_FIELD_NAME, mark);
		    	values.put(DBHelper.COMMENT_FIELD_NAME, comment);
				db.update(DBHelper.PRACTICE_STUDENT_MARK_TBL_NAME, values, 
						DBHelper.STUDENT_FIELD_NAME + "=" + String.valueOf(student.getId()) + " AND " +
								DBHelper.PRACTICE_FIELD_NAME + "=" + practiceId, null);
			}
		} catch(SQLException e) {
			throw e;
		}
	}
	
	/**
     * Update a concept.
     * 
     * @param examId
     * @param courseId
     * @param evaluationId
     * @param date
     * @param name
     * @param description
     * @param weight
     * @return The exam identify inserted.
     */
    public long updateConcept(int examId, String date, String name, String description, String weight) {
    	try {
    		db.beginTransaction();
			updateTest(examId, name, description, weight);
			updateExam(examId, date);
			db.setTransactionSuccessful();
    	} catch(SQLException e) {
    		throw e;
    	} finally {
    	   db.endTransaction();
    	}
		
    	return examId;
	}
    
    /**
     * Update a procedure.
     * 
     * @param practiceId
     * @param courseId
     * @param evaluationId
     * @param date
     * @param name
     * @param description
     * @param weight
     * @return The exam identify inserted.
     */
    public long updateProcedure(int practiceId, String startDate, String endDate, String name, String description, String weight) {
    	try {
    		db.beginTransaction();
			updateTest(practiceId, name, description, weight);
			updatePractice(practiceId, startDate, endDate);
			db.setTransactionSuccessful();
    	} catch(SQLException e) {
    		throw e;
    	} finally {
    	   db.endTransaction();
    	}
		
    	return practiceId;
	}
    
    /**
     * Update a practice.
     * 
     * @param id
     * @param startDate
     * @param endDate
     */
    private void updatePractice(int id, String startDate, String endDate) {
    	try {
			ContentValues values = new ContentValues();
	    	values.put(DBHelper.START_DATE_2_FIELD_NAME, startDate);
	    	values.put(DBHelper.END_DATE_2_FIELD_NAME, endDate);
			db.update(DBHelper.PRACTICE_TBL_NAME, values, 
					DBHelper.ID_FIELD_NAME + "=" + id, null);
		} catch(SQLException e) {
			throw e;
		}
	}

	/**
     * Update an exam.
     * 
     * @param id
     * @param date
     */
    public void updateExam(int id, String date) {
    	try {
			ContentValues values = new ContentValues();
	    	values.put(DBHelper.EXAMDATE_FIELD_NAME, date);
			db.update(DBHelper.EXAM_TBL_NAME, values, 
					DBHelper.ID_FIELD_NAME + "=" + id, null);
		} catch(SQLException e) {
			throw e;
		}
	}

    /**
     * Update a test.
     * 
     * @param id
     * @param name
     * @param description
     * @param weight
     */
	public void updateTest(int id, String name, String description,	String weight) {
		try {
			ContentValues values = new ContentValues();
	    	values.put(DBHelper.NAME_FIELD_NAME, name);
	    	values.put(DBHelper.DESCRIPTION_FIELD_NAME, description);
	    	values.put(DBHelper.WEIGHT_FIELD_NAME, weight);
			db.update(DBHelper.TEST_TBL_NAME, values, 
					DBHelper.ID_FIELD_NAME + "=" + id, null);
		} catch(SQLException e) {
			throw e;
		}
	}
	
	/**
	 * Update an attitude, and AttitudeStudentMark and insert a new all_attitude register.
	 * 
	 * @param attitudeId
	 * @param attitudeStudentMarkId
	 * @param date
	 * @param name
	 * @param description
	 * @param weight
	 */
	public void updateAttitude(Integer attitudeId, Integer attitudeStudentMarkId, 
			String date, String name, String description, String weight) {
		try {
			db.beginTransaction();
			
			Integer allAttitutdeId = (int) addAllAttitude(name, description);
			
			ContentValues values1 = new ContentValues();
	    	values1.put(DBHelper.WEIGHT_FIELD_NAME, weight);
	    	values1.put(DBHelper.ALL_ATTITUDE_FIELD_NAME, allAttitutdeId);
			db.update(DBHelper.ATTITUDE_TBL_NAME, values1, 
					DBHelper.ID_FIELD_NAME + "=" + attitudeId, null);
			
			ContentValues values2 = new ContentValues();
			values2.put(DBHelper.ATTITUDEDATE_FIELD_NAME, date);
			db.update(DBHelper.ATTITUDE_STUDENT_MARK_TBL_NAME, values2, 
					DBHelper.ID_FIELD_NAME + "=" + attitudeStudentMarkId, null);
			
			db.setTransactionSuccessful();
		} catch(SQLException e) {
			throw e;
		} finally {
	    	   db.endTransaction();
	    }
	}
	
	/**
	 * Update an attitude and all_attitude register.
	 * 
	 * @param attitudeId
	 * @param allAttitudeId
	 * @param attitudeStudentMarkId
	 * @param date
	 * @param name
	 * @param description
	 * @param weight
	 */
	public void updateAttitude(Integer attitudeId, Integer allAttitudeId, Integer attitudeStudentMarkId, String date, String weight) {
		try {
			db.beginTransaction();
			
			ContentValues values1 = new ContentValues();
	    	values1.put(DBHelper.WEIGHT_FIELD_NAME, weight);
	    	values1.put(DBHelper.ALL_ATTITUDE_FIELD_NAME, allAttitudeId);
			db.update(DBHelper.ATTITUDE_TBL_NAME, values1, 
					DBHelper.ID_FIELD_NAME + "=" + attitudeId, null);
			
			ContentValues values2 = new ContentValues();
			values2.put(DBHelper.ATTITUDEDATE_FIELD_NAME, date);
			db.update(DBHelper.ATTITUDE_STUDENT_MARK_TBL_NAME, values2, 
					DBHelper.ID_FIELD_NAME + "=" + attitudeStudentMarkId, null);
			
			db.setTransactionSuccessful();
		} catch(SQLException e) {
			throw e;
		} finally {
	    	   db.endTransaction();
	    }
	}
	
	/**
	 * Update the student.
	 * 
	 * @param student
	 */
	public void updateStudent(Student student) {
		try {
			ContentValues values = new ContentValues();
	    	values.put(DBHelper.NAME_FIELD_NAME, student.getName());
	    	values.put(DBHelper.LASTNAME_FIELD_NAME, student.getLastname());
	    	values.put(DBHelper.PHOTO_FIELD_NAME, student.getPhoto());
			db.update(DBHelper.STUDENT_TBL_NAME, values, 
					DBHelper.ID_FIELD_NAME + "=" + student.getId(), null);
		} catch(SQLException e) {
			throw e;
		}
	}
	
	/**
	 * Update the attendance.
	 * 
	 * @param attendance
	 */
	public void updateAttendance(Attendance attendance) {
		try {
			ContentValues values = new ContentValues();
	    	values.put(DBHelper.ATTENDANCE_DATE_FIELD_NAME, attendance.getAttendanceDate());
	    	values.put(DBHelper.EVENT_FIELD_NAME, attendance.getEvent().getId());
	    	values.put(DBHelper.COURSE_STUDENT_FIELD_NAME, attendance.getCourseStudent().getId());
			db.update(DBHelper.ATTENDANCE_TBL_NAME, values, 
					DBHelper.ID_FIELD_NAME + "=" + attendance.getId(), null);
		} catch(SQLException e) {
			throw e;
		}
	}

	/**
     * Get all courses and insert they into an ArrayList.
     * 
     * @return The ArrayList with all Courses.
     */
    public ArrayList<Course> getCourses() {
    	String[] columns = {DBHelper.ID_FIELD_NAME, DBHelper.LEVEL_FIELD_NAME,
    			DBHelper.NAME_FIELD_NAME, DBHelper.GROUP_FIELD_NAME,
    			DBHelper.TUTOR_FIELD_NAME, DBHelper.CONCEPT_WEIGHT_FIELD_NAME,
    			DBHelper.PROCEDURE_WEIGHT_FIELD_NAME, DBHelper.ATTITUDE_WEIGHT_FIELD_NAME};
    	
    	Cursor cursor = db.query(DBHelper.COURSE_TBL_NAME, columns,
    			null, null, null, null,
    			DBHelper.ID_FIELD_NAME + " COLLATE NOCASE ASC");
    	
    	ArrayList<Course> coursesList = new ArrayList<Course>(cursor.getCount());
    	
    	if(cursor.moveToFirst()) {
            do {
                Course course;
				course = new Course(
						context,
						cursor.getInt(0),
						cursor.getString(1),
						cursor.getString(2),
						cursor.getString(3),
						cursor.getString(4),
						cursor.getFloat(5),
						cursor.getFloat(6),
						cursor.getFloat(7));
                coursesList.add(course);
            } while (cursor.moveToNext());
        }
    	cursor.close();
    	
    	return coursesList;
    }
    
    /**
     * Get all courses identifies and insert they into an ArrayList.
     * 
     * @return The ArrayList with all Courses identifies.
     */
    public ArrayList<Integer> getCoursesId() {
    	String[] columns = {DBHelper.ID_FIELD_NAME};
    	
    	Cursor cursor = db.query(DBHelper.COURSE_TBL_NAME, columns,
    			null, null, null, null,
    			DBHelper.ID_FIELD_NAME + " COLLATE NOCASE ASC");
    	
    	ArrayList<Integer> coursesId = new ArrayList<Integer>(cursor.getCount());
    	
    	if(cursor.moveToFirst()) {
            do {
                coursesId.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
    	cursor.close();
    	
    	return coursesId;
    }
    
    /**
     * Get course specified by id or null if not exist.
     * 
     * @return The Course if exist.
     */
    public Course getCourse(String id) {
    	String[] columns = {DBHelper.ID_FIELD_NAME, DBHelper.LEVEL_FIELD_NAME,
    			DBHelper.NAME_FIELD_NAME, DBHelper.GROUP_FIELD_NAME,
    			DBHelper.TUTOR_FIELD_NAME, DBHelper.CONCEPT_WEIGHT_FIELD_NAME,
    			DBHelper.PROCEDURE_WEIGHT_FIELD_NAME, DBHelper.ATTITUDE_WEIGHT_FIELD_NAME};
    	
    	Cursor cursor = db.query(DBHelper.COURSE_TBL_NAME, columns,
    			DBHelper.ID_FIELD_NAME + "=" + id, null, null, null, null);
    	
    	Course course = null;
    	
    	if(cursor.moveToFirst()) {
			course = new Course(
					context,
					cursor.getInt(0),
					cursor.getString(1),
					cursor.getString(2),
					cursor.getString(3),
					cursor.getString(4),
					cursor.getFloat(5),
					cursor.getFloat(6),
					cursor.getFloat(7));
        }
    	cursor.close();
    	
    	return course;
    }
    
    /**
     * Get student specified by id or null if not exist.
     * 
     * @return The Student if exist.
     */
    public Student getStudent(String id) {
    	String[] columns = {DBHelper.ID_FIELD_NAME, DBHelper.NAME_FIELD_NAME,
    			DBHelper.LASTNAME_FIELD_NAME, DBHelper.PHOTO_FIELD_NAME};
    	
    	Cursor cursor = db.query(DBHelper.STUDENT_TBL_NAME, columns,
    			DBHelper.ID_FIELD_NAME + "=" + id, null, null, null, null);
    	
    	Student student = null;
    	
    	if(cursor.moveToFirst()) {
			try {
				student = new Student(
						context,
						cursor.getInt(0),
						cursor.getString(1),
						cursor.getString(2),
						cursor.getString(3));
			} catch (CheckFieldException e) {
				e.printStackTrace();
			}
        }
    	cursor.close();
    	
    	return student;
    }
    
    /**
     * Get all evaluations and return they.
     * 
     * @return An array list with all evaluations.
     */
    public ArrayList<Evaluation> getEvaluations() {
    	String[] columns = {DBHelper.ID_FIELD_NAME, DBHelper.NAME_FIELD_NAME,
    			DBHelper.START_DATE_FIELD_NAME, DBHelper.END_DATE_FIELD_NAME};
    	
    	Cursor cursor = db.query(DBHelper.EVALUATION_TBL_NAME, columns,
    			null, null, null, null,
    			DBHelper.ID_FIELD_NAME + " COLLATE NOCASE ASC");
    	
    	ArrayList<Evaluation> evaluationsList = new ArrayList<Evaluation>(cursor.getCount());
    	
    	if(cursor.moveToFirst()) {
            do {
                Evaluation evaluation;
				try {
					evaluation = new Evaluation(
							context,
							cursor.getInt(0),
							cursor.getString(1),
							cursor.getString(2),
							cursor.getString(3));
	                evaluationsList.add(evaluation);
				} catch (CheckFieldException e) {
					e.printStackTrace();
				}
            } while (cursor.moveToNext());
        }
    	cursor.close();
    	
    	return evaluationsList;
	}
    
    /**
     * Get the evaluation identify by unique course and evaluation identifies.
     * 
     * @param courseId
     * @param evaluationId
     * @return
     */
    private CourseEvaluation getCourseEvaluation(int courseId, int evaluationId) {
    	CourseEvaluation courseEvaluation = null;
		
		String[] columns = {DBHelper.ID_FIELD_NAME, DBHelper.ID_COURSE_FIELD_NAME,
    			DBHelper.ID_EVALUATION_FIELD_NAME};
		
		String where = DBHelper.ID_COURSE_FIELD_NAME + "=" + String.valueOf(courseId) + 
				" AND " + DBHelper.ID_EVALUATION_FIELD_NAME + "=" + String.valueOf(evaluationId);
    	
    	Cursor cursor = db.query(DBHelper.COURSE_EVALUATION_TBL_NAME,
    			columns,
    			where,
    			null,
    			null,
    			null,
    			DBHelper.ID_FIELD_NAME + " COLLATE NOCASE ASC");
    	
    	if(cursor.moveToFirst()) {
			courseEvaluation = new CourseEvaluation(
					context,
					cursor.getInt(0),
					cursor.getInt(1),
					cursor.getInt(2));
        }
    	cursor.close();
		
		return courseEvaluation;
	}

    /**
     * Retrieve students of the course ordered by {@code DBHelper.STUDENT_FIELD_LASTNAME}.
     * 
     * @return An array list of students ordered by {@code DBHelper.STUDENT_FIELD_LASTNAME}.
     */
	public ArrayList<Student> getCourseStudents(int courseId) {
		String query = "SELECT " + 
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.LASTNAME_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.PHOTO_FIELD_NAME + " " +
				"FROM " +
				DBHelper.COURSE_STUDENT_TBL_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + " " +
				"WHERE " + 
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_COURSE_FIELD_NAME + "=" + String.valueOf(courseId) + " " +
				"AND " +
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_STUDENT_FIELD_NAME + "=" + 
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " " +
				"ORDER BY " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.LASTNAME_FIELD_NAME;
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	ArrayList<Student> studentsList = new ArrayList<Student>(cursor.getCount());
    	
    	if(cursor.moveToFirst()) {
            do {
                Student student;
				try {
					student = new Student(
							context,
							cursor.getInt(0),
							cursor.getString(1),
							cursor.getString(2),
							cursor.getString(3));
	                studentsList.add(student);
				} catch (CheckFieldException e) {
					e.printStackTrace();
				}
            } while (cursor.moveToNext());
        }
    	cursor.close();
    	
    	return studentsList;
	}

	/**
	 * Query to get concepts of the student enrolled in the course.
	 * 
	 * @param courseId The course identify.
	 * @param studentId The student identify.
	 * @return
	 */
	public ArrayList<ExamStudentMark> getStudentCourseConcepts(int courseId,
			int studentId) {
		String query = "SELECT " +
			DBHelper.EXAM_STUDENT_MARK_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " + 	// 0
			DBHelper.EXAM_STUDENT_MARK_TBL_NAME + "." + DBHelper.MARK_FIELD_NAME + ", " +	// 1
			DBHelper.EXAM_STUDENT_MARK_TBL_NAME + "." + DBHelper.COMMENT_FIELD_NAME + ", " + // 2
				
			DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +				// 3
			DBHelper.STUDENT_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +				// 4
			DBHelper.STUDENT_TBL_NAME + "." + DBHelper.LASTNAME_FIELD_NAME + ", " +			// 5
			DBHelper.STUDENT_TBL_NAME + "." + DBHelper.PHOTO_FIELD_NAME + ", " +			// 6
			
			DBHelper.EXAM_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +					// 7
			DBHelper.EXAM_TBL_NAME + "." + DBHelper.EXAMDATE_FIELD_NAME + ", " +			// 8
			
			DBHelper.TEST_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +					// 9
			DBHelper.TEST_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +				// 10
			DBHelper.TEST_TBL_NAME + "." + DBHelper.DESCRIPTION_FIELD_NAME + ", " +			// 11
			DBHelper.TEST_TBL_NAME + "." + DBHelper.WEIGHT_FIELD_NAME + ", " +				// 12
			
			DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +		// 13
			
			DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " + 				// 14
			DBHelper.COURSE_TBL_NAME + "." + DBHelper.LEVEL_FIELD_NAME + ", " +				// 15
			DBHelper.COURSE_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +				// 16
			DBHelper.COURSE_TBL_NAME + "." + DBHelper.GROUP_FIELD_NAME + ", " +				// 17
			DBHelper.COURSE_TBL_NAME + "." + DBHelper.TUTOR_FIELD_NAME + ", " +				// 18
			DBHelper.COURSE_TBL_NAME + "." + DBHelper.CONCEPT_WEIGHT_FIELD_NAME + ", " +    // 19
			DBHelper.COURSE_TBL_NAME + "." + DBHelper.PROCEDURE_WEIGHT_FIELD_NAME + ", " +  // 20
			DBHelper.COURSE_TBL_NAME + "." + DBHelper.ATTITUDE_WEIGHT_FIELD_NAME + ", " +   // 21
			
			DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +			// 22
			DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +			// 23
			DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.START_DATE_FIELD_NAME + ", " +	// 24
			DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.END_DATE_FIELD_NAME + " " +		// 25
			
			"FROM " +
			DBHelper.EXAM_STUDENT_MARK_TBL_NAME + ", " +
			DBHelper.STUDENT_TBL_NAME + ", " +
			DBHelper.EXAM_TBL_NAME + ", " +
			DBHelper.TEST_TBL_NAME + ", " +
			DBHelper.COURSE_EVALUATION_TBL_NAME + ", " +
			DBHelper.COURSE_TBL_NAME + ", " +
			DBHelper.EVALUATION_TBL_NAME + " " +
			
			"WHERE " +
			DBHelper.EXAM_STUDENT_MARK_TBL_NAME + "." + DBHelper.STUDENT_FIELD_NAME + "=" +
			DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
			
			" AND " +
			DBHelper.EXAM_STUDENT_MARK_TBL_NAME + "." + DBHelper.EXAM_FIELD_NAME + "=" + 
			DBHelper.EXAM_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
			
			" AND " +
			DBHelper.EXAM_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" +
			DBHelper.TEST_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
			
			" AND " +
			DBHelper.TEST_TBL_NAME + "." + DBHelper.COURSE_EVALUATION_FIELD_NAME + "=" + 
			DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
			
			" AND " + 
			DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_COURSE_FIELD_NAME + "=" +
			DBHelper.COURSE_TBL_NAME + "." +  DBHelper.ID_FIELD_NAME +
			
			" AND " +
			DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_EVALUATION_FIELD_NAME + "=" +
			DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME +
			
			" AND " +
			DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" + 
			String.valueOf(courseId) + 
			
			" AND " +
			DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" + 
			String.valueOf(studentId);
		
		if(Session.getEvaluationIndex() != null) {
			query += " AND " +
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" +
				String.valueOf(Session.getEvaluations().get(Session.getEvaluationIndex()).getId());
		}
		
		query += " ORDER BY "  + DBHelper.EXAM_TBL_NAME + "." + DBHelper.EXAMDATE_FIELD_NAME + " DESC";
		
		Cursor cursor = db.rawQuery(query, null);
    	
    	ArrayList<ExamStudentMark> examStudentMarkList = new ArrayList<ExamStudentMark>(cursor.getCount());
    	Student student;
    	Exam exam;
    	Test test;
    	CourseEvaluation courseEvaluation;
    	Course course;
    	Evaluation evaluation;
    	ExamStudentMark examStudentMark;
    	
    	if(cursor.moveToFirst()) {
            do {
				try {
					student = new Student(
							context,
							cursor.getInt(3),
							cursor.getString(4),
							cursor.getString(5),
							cursor.getString(6));
					course = new Course(
							context,
							cursor.getInt(14),
							cursor.getString(15),
							cursor.getString(16),
							cursor.getString(17),
							cursor.getString(18),
							cursor.getFloat(19),
							cursor.getFloat(20),
							cursor.getFloat(21));
					evaluation = new Evaluation(
							context,
							cursor.getInt(22),
							cursor.getString(23),
							cursor.getString(24),
							cursor.getString(25));
					courseEvaluation = new CourseEvaluation(
							context,
							cursor.getInt(13),
							course,
							evaluation);
					test = new Test(
							context,
							cursor.getInt(9),
							cursor.getString(10),
							cursor.getString(11),
							cursor.getFloat(12),
							courseEvaluation);
					exam = new Exam(
							context,
							cursor.getInt(7),
							cursor.getString(8),
							test);
					examStudentMark = new ExamStudentMark(
							context,
							cursor.getInt(0),
							student,
							exam,
							cursor.getFloat(1),
							cursor.getString(2));
					examStudentMarkList.add(examStudentMark);
				} catch (CheckFieldException e) {
					e.printStackTrace();
				}
            } while (cursor.moveToNext());
        }
    	cursor.close();
    	
    	return examStudentMarkList;
	}

	/**
	 * Query to get procedures of the student enrolled in the course.
	 * 
	 * @param courseId The course identify.
	 * @param studentId The student identify.
	 * @return
	 */
	public ArrayList<PracticeStudentMark> getStudentCourseProcedures(
			int courseId, int studentId) {
		String query = "SELECT " +
				DBHelper.PRACTICE_STUDENT_MARK_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " + 	// 0
				DBHelper.PRACTICE_STUDENT_MARK_TBL_NAME + "." + DBHelper.MARK_FIELD_NAME + ", " +	// 1
				DBHelper.PRACTICE_STUDENT_MARK_TBL_NAME + "." + DBHelper.COMMENT_FIELD_NAME + ", " + // 2
					
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +					// 3
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +					// 4
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.LASTNAME_FIELD_NAME + ", " +				// 5
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.PHOTO_FIELD_NAME + ", " +				// 6
				
				DBHelper.PRACTICE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +					// 6
				DBHelper.PRACTICE_TBL_NAME + "." + DBHelper.START_DATE_2_FIELD_NAME + ", " +		// 7
				DBHelper.PRACTICE_TBL_NAME + "." + DBHelper.END_DATE_2_FIELD_NAME + ", " +			// 8
				
				DBHelper.TEST_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +						// 10
				DBHelper.TEST_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +					// 11
				DBHelper.TEST_TBL_NAME + "." + DBHelper.DESCRIPTION_FIELD_NAME + ", " +				// 12
				DBHelper.TEST_TBL_NAME + "." + DBHelper.WEIGHT_FIELD_NAME + ", " +					// 13
				
				DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +			// 14
				
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " + 					// 15
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.LEVEL_FIELD_NAME + ", " +					// 16
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +					// 17
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.GROUP_FIELD_NAME + ", " +					// 18
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.TUTOR_FIELD_NAME + ", " +					// 19
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.CONCEPT_WEIGHT_FIELD_NAME + ", " +    	// 20
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.PROCEDURE_WEIGHT_FIELD_NAME + ", " +  	// 21
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ATTITUDE_WEIGHT_FIELD_NAME + ", " +   	// 22
				
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +				// 23
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +				// 24
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.START_DATE_FIELD_NAME + ", " +		// 25
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.END_DATE_FIELD_NAME + " " +			// 26
				
				"FROM " +
				DBHelper.PRACTICE_STUDENT_MARK_TBL_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + ", " +
				DBHelper.PRACTICE_TBL_NAME + ", " +
				DBHelper.TEST_TBL_NAME + ", " +
				DBHelper.COURSE_EVALUATION_TBL_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + ", " +
				DBHelper.EVALUATION_TBL_NAME + " " +
				
				"WHERE " +
				DBHelper.PRACTICE_STUDENT_MARK_TBL_NAME + "." + DBHelper.STUDENT_FIELD_NAME + "=" +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
				
				" AND " +
				DBHelper.PRACTICE_STUDENT_MARK_TBL_NAME + "." + DBHelper.PRACTICE_FIELD_NAME + "=" + 
				DBHelper.PRACTICE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
				
				" AND " +
				DBHelper.PRACTICE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" +
				DBHelper.TEST_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
				
				" AND " +
				DBHelper.TEST_TBL_NAME + "." + DBHelper.COURSE_EVALUATION_FIELD_NAME + "=" + 
				DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
				
				" AND " + 
				DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_COURSE_FIELD_NAME + "=" +
				DBHelper.COURSE_TBL_NAME + "." +  DBHelper.ID_FIELD_NAME +
				
				" AND " +
				DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_EVALUATION_FIELD_NAME + "=" +
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME +
				
				" AND " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" + 
				String.valueOf(courseId) + 
				
				" AND " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" + 
				String.valueOf(studentId);
			
			if(Session.getEvaluationIndex() != null) {
				query += " AND " +
					DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" +
					String.valueOf(Session.getEvaluations().get(Session.getEvaluationIndex()).getId());
			}
			
			query += " ORDER BY "  + DBHelper.PRACTICE_TBL_NAME + "." + DBHelper.START_DATE_2_FIELD_NAME + " DESC";
			
			Cursor cursor = db.rawQuery(query, null);
	    	
	    	ArrayList<PracticeStudentMark> practiceStudentMarkList = new ArrayList<PracticeStudentMark>(cursor.getCount());
	    	Student student;
	    	Practice practice;
	    	Test test;
	    	CourseEvaluation courseEvaluation;
	    	Course course;
	    	Evaluation evaluation;
	    	PracticeStudentMark practiceStudentMark;
	    	
	    	if(cursor.moveToFirst()) {
	            do {
					try {
						student = new Student(
								context,
								cursor.getInt(3),
								cursor.getString(4),
								cursor.getString(5),
								cursor.getString(6));
						course = new Course(
								context,
								cursor.getInt(15),
								cursor.getString(16),
								cursor.getString(17),
								cursor.getString(18),
								cursor.getString(19),
								cursor.getFloat(20),
								cursor.getFloat(21),
								cursor.getFloat(22));
						evaluation = new Evaluation(
								context,
								cursor.getInt(23),
								cursor.getString(24),
								cursor.getString(25),
								cursor.getString(26));
						courseEvaluation = new CourseEvaluation(
								context,
								cursor.getInt(14),
								course,
								evaluation);
						test = new Test(
								context,
								cursor.getInt(10),
								cursor.getString(11),
								cursor.getString(12),
								cursor.getFloat(13),
								courseEvaluation);
						practice = new Practice(
								context,
								cursor.getInt(7),
								cursor.getString(8),
								cursor.getString(9),
								test);
						practiceStudentMark = new PracticeStudentMark(
								context,
								cursor.getInt(0),
								student,
								practice,
								cursor.getFloat(1),
								cursor.getString(2));
						practiceStudentMarkList.add(practiceStudentMark);
					} catch (CheckFieldException e) {
						e.printStackTrace();
					}
	            } while (cursor.moveToNext());
	        }
	    	cursor.close();
	    	
	    	return practiceStudentMarkList;
	}

	/**
	 * Query to get attitudes of the student enrolled in the course.
	 * 
	 * @param courseId The course identify.
	 * @param studentId The student identify.
	 * @return
	 */
	public ArrayList<AttitudeStudentMark> getStudentCourseAttitudes(
			int courseId, int studentId) {
		String query = "SELECT " +
				DBHelper.ATTITUDE_STUDENT_MARK_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " + 			// 0
				DBHelper.ATTITUDE_STUDENT_MARK_TBL_NAME + "." + DBHelper.ATTITUDEDATE_FIELD_NAME + ", " +	// 1
					
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +					// 2
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +					// 3
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.LASTNAME_FIELD_NAME + ", " +				// 4
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.PHOTO_FIELD_NAME + ", " +				// 5
				
				DBHelper.ATTITUDE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +					// 6
				DBHelper.ATTITUDE_TBL_NAME + "." + DBHelper.WEIGHT_FIELD_NAME + ", " +				// 7
				
				DBHelper.ALL_ATTITUDE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +				// 8
				DBHelper.ALL_ATTITUDE_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +			// 9
				DBHelper.ALL_ATTITUDE_TBL_NAME + "." + DBHelper.DESCRIPTION_FIELD_NAME + ", " +		// 10
				
				DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +			// 11
				
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " + 					// 12
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.LEVEL_FIELD_NAME + ", " +					// 13
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +					// 14
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.GROUP_FIELD_NAME + ", " +					// 15
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.TUTOR_FIELD_NAME + ", " +					// 16
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.CONCEPT_WEIGHT_FIELD_NAME + ", " +    	// 17
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.PROCEDURE_WEIGHT_FIELD_NAME + ", " +  	// 18
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ATTITUDE_WEIGHT_FIELD_NAME + ", " +   	// 19
				
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +				// 20
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +				// 21
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.START_DATE_FIELD_NAME + ", " +		// 22
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.END_DATE_FIELD_NAME + " " +			// 23
				
				"FROM " +
				DBHelper.ATTITUDE_STUDENT_MARK_TBL_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + ", " +
				DBHelper.ATTITUDE_TBL_NAME + ", " +
				DBHelper.ALL_ATTITUDE_TBL_NAME + ", " +
				DBHelper.COURSE_EVALUATION_TBL_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + ", " +
				DBHelper.EVALUATION_TBL_NAME + " " +
				
				"WHERE " +
				DBHelper.ATTITUDE_STUDENT_MARK_TBL_NAME + "." + DBHelper.STUDENT_FIELD_NAME + "=" +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
				
				" AND " +
				DBHelper.ATTITUDE_STUDENT_MARK_TBL_NAME + "." + DBHelper.ATTITUDE_FIELD_NAME + "=" + 
				DBHelper.ATTITUDE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
				
				" AND " +
				DBHelper.ATTITUDE_TBL_NAME + "." + DBHelper.ALL_ATTITUDE_FIELD_NAME + "=" +
				DBHelper.ALL_ATTITUDE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
				
				" AND " +
				DBHelper.ATTITUDE_TBL_NAME + "." + DBHelper.COURSE_EVALUATION_FIELD_NAME + "=" + 
				DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + 
				
				" AND " + 
				DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_COURSE_FIELD_NAME + "=" +
				DBHelper.COURSE_TBL_NAME + "." +  DBHelper.ID_FIELD_NAME +
				
				" AND " +
				DBHelper.COURSE_EVALUATION_TBL_NAME + "." + DBHelper.ID_EVALUATION_FIELD_NAME + "=" +
				DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME +
				
				" AND " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" + 
				String.valueOf(courseId) + 
				
				" AND " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" + 
				String.valueOf(studentId);

			if(Session.getEvaluationIndex() != null) {
				query += " AND " +
					DBHelper.EVALUATION_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + "=" +
					String.valueOf(Session.getEvaluations().get(Session.getEvaluationIndex()).getId());
			}
			
			query += " ORDER BY " + DBHelper.ATTITUDE_STUDENT_MARK_TBL_NAME + "." + DBHelper.ATTITUDEDATE_FIELD_NAME + " DESC";
			
			Cursor cursor = db.rawQuery(query, null);
	    	
	    	ArrayList<AttitudeStudentMark> attitudeStudentMarkList = new ArrayList<AttitudeStudentMark>(cursor.getCount());
	    	Student student;
	    	Attitude attitude;
	    	AllAttitude allAttitude;
	    	CourseEvaluation courseEvaluation;
	    	Course course;
	    	Evaluation evaluation;
	    	AttitudeStudentMark attitudeStudentMark;
	    	
	    	if(cursor.moveToFirst()) {
	            do {
					try {
						student = new Student(
								context,
								cursor.getInt(2),
								cursor.getString(3),
								cursor.getString(4),
								cursor.getString(5));
						course = new Course(
								context,
								cursor.getInt(12),
								cursor.getString(13),
								cursor.getString(14),
								cursor.getString(15),
								cursor.getString(16),
								cursor.getFloat(17),
								cursor.getFloat(18),
								cursor.getFloat(19));
						evaluation = new Evaluation(
								context,
								cursor.getInt(20),
								cursor.getString(21),
								cursor.getString(22),
								cursor.getString(23));
						courseEvaluation = new CourseEvaluation(
								context,
								cursor.getInt(11),
								course,
								evaluation);
						allAttitude = new AllAttitude(
								context,
								cursor.getInt(8),
								cursor.getString(9),
								cursor.getString(10));
						attitude = new Attitude(
								context,
								cursor.getInt(6),
								cursor.getFloat(7),
								courseEvaluation,
								allAttitude);
						attitudeStudentMark = new AttitudeStudentMark(
								context,
								cursor.getInt(0),
								student,
								cursor.getString(1),
								attitude);
						attitudeStudentMarkList.add(attitudeStudentMark);
					} catch (CheckFieldException e) {
						e.printStackTrace();
					}
	            } while (cursor.moveToNext());
	        }
	    	cursor.close();
	    	
	    	return attitudeStudentMarkList;
	}

	/**
	 * Return a cursor with all attitude (it is useful to Spinners).
	 * 
	 * @return
	 */
	public Cursor getCursorAllAttitude() {
		String[] columns = {DBHelper.ID_FIELD_NAME + " _id", DBHelper.NAME_FIELD_NAME,
    			DBHelper.DESCRIPTION_FIELD_NAME};
    	
    	Cursor cursor = db.query(DBHelper.ALL_ATTITUDE_TBL_NAME,
    			columns,
    			null,
    			null,
    			null,
    			null,
    			DBHelper.NAME_FIELD_NAME + " COLLATE NOCASE ASC");
    	
    	return cursor;
	}

	/**
	 * Get an return all attendance events.
	 * 
	 * @return
	 */
	public ArrayList<AttendanceEvent> getAllAttendanceEvents() {
		String[] columns = {DBHelper.ID_FIELD_NAME, DBHelper.ICON_FIELD_NAME, 
				DBHelper.NAME_FIELD_NAME, DBHelper.DESCRIPTION_FIELD_NAME};
    	
    	Cursor cursor = db.query(DBHelper.ATTENDANCE_EVENT_TBL_NAME, columns,
    			null, null, null, null,
    			DBHelper.ID_FIELD_NAME + " COLLATE NOCASE ASC");
    	
    	ArrayList<AttendanceEvent> attendanceEventList = new ArrayList<AttendanceEvent>(cursor.getCount());
    	
    	if(cursor.moveToFirst()) {
            do {
            	AttendanceEvent item;
				item = new AttendanceEvent(
						context,
						cursor.getInt(0),
						cursor.getString(1),
						cursor.getString(2),
						cursor.getString(3));
				attendanceEventList.add(item);
            } while (cursor.moveToNext());
        }
    	cursor.close();
    	
    	return attendanceEventList;
	}

	/**
	 * Return the course student object through course and student identifies or null if not exist.
	 * 
	 * @param courseId
	 * @param studentId
	 * @return
	 */
	public CourseStudent getCourseStudent(Integer courseId, Integer studentId) {
		CourseStudent courseStudent = null;
		
		String query = "SELECT " +
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.LEVEL_FIELD_NAME + ", " + 
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.GROUP_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.TUTOR_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.CONCEPT_WEIGHT_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.PROCEDURE_WEIGHT_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ATTITUDE_WEIGHT_FIELD_NAME + ", " +
				
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.LASTNAME_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.PHOTO_FIELD_NAME + " " +
				
				"FROM " +
				DBHelper.COURSE_STUDENT_TBL_NAME + ", " + DBHelper.COURSE_TBL_NAME + ", " + DBHelper.STUDENT_TBL_NAME + " " +
				
				"WHERE " +
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_COURSE_FIELD_NAME + "=" +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_STUDENT_FIELD_NAME + "=" +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_COURSE_FIELD_NAME + "=" + courseId + " AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_STUDENT_FIELD_NAME + "=" + studentId + ";";
		
		Cursor cursor = db.rawQuery(query, null);
    	
    	if(cursor.moveToFirst()) {
			try {
				Course course = new Course(context,
	    				cursor.getInt(1),
	    				cursor.getString(2),
	    				cursor.getString(3),
	    				cursor.getString(4),
	    				cursor.getString(5),
	    				cursor.getFloat(6),
	    				cursor.getFloat(7),
	    				cursor.getFloat(8));
				
				Student student = new Student(context,
						cursor.getInt(9),
						cursor.getString(10),
						cursor.getString(11),
						cursor.getString(12));
				
				courseStudent = new CourseStudent(
						context,
						cursor.getInt(0),
						course,
						student);
			} catch (CheckFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	
    	cursor.close();
		
		return courseStudent;
	}

	/**
	 * Return the Attendance object.
	 * 
	 * @param date
	 * @param courseId
	 * @param studentId
	 * @return
	 */
	public Attendance getAttendance(String date, Integer courseId,
			Integer studentId) {
		Attendance attendance = null;
		
		String query = "SELECT " +
				DBHelper.ATTENDANCE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.ATTENDANCE_TBL_NAME + "." + DBHelper.ATTENDANCE_DATE_FIELD_NAME + ", " +
				
				DBHelper.ATTENDANCE_EVENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.ATTENDANCE_EVENT_TBL_NAME + "." + DBHelper.ICON_FIELD_NAME + ", " +
				DBHelper.ATTENDANCE_EVENT_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +
				DBHelper.ATTENDANCE_EVENT_TBL_NAME + "." + DBHelper.DESCRIPTION_FIELD_NAME + ", " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +

				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.LEVEL_FIELD_NAME + ", " + 
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.GROUP_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.TUTOR_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.CONCEPT_WEIGHT_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.PROCEDURE_WEIGHT_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ATTITUDE_WEIGHT_FIELD_NAME + ", " +
				
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.LASTNAME_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.PHOTO_FIELD_NAME + " " +
				
				"FROM " +
				DBHelper.ATTENDANCE_TBL_NAME + ", " + DBHelper.ATTENDANCE_EVENT_TBL_NAME + ", " +
				DBHelper.COURSE_STUDENT_TBL_NAME + ", " + DBHelper.COURSE_TBL_NAME + ", " + DBHelper.STUDENT_TBL_NAME + " " +
				
				"WHERE " + 
				DBHelper.ATTENDANCE_TBL_NAME + "." + DBHelper.EVENT_FIELD_NAME + "=" + 
				DBHelper.ATTENDANCE_EVENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " AND " +
				
				DBHelper.ATTENDANCE_TBL_NAME + "." + DBHelper.COURSE_STUDENT_FIELD_NAME + "=" +
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_COURSE_FIELD_NAME + "=" +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_STUDENT_FIELD_NAME + "=" +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " AND " +
				
				DBHelper.ATTENDANCE_TBL_NAME + "." + DBHelper.ATTENDANCE_DATE_FIELD_NAME + "='" + date + "' AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_COURSE_FIELD_NAME + "=" + courseId + " AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_STUDENT_FIELD_NAME + "=" + studentId + ";";
				
		Cursor cursor = db.rawQuery(query, null);
    	
    	if(cursor.moveToFirst()) {
			try {
				Course course = new Course(context,
	    				cursor.getInt(7),
	    				cursor.getString(8),
	    				cursor.getString(9),
	    				cursor.getString(10),
	    				cursor.getString(11),
	    				cursor.getFloat(12),
	    				cursor.getFloat(13),
	    				cursor.getFloat(14));
				
				Student student = new Student(context,
						cursor.getInt(15),
						cursor.getString(16),
						cursor.getString(17),
						cursor.getString(18));
				
				CourseStudent courseStudent = new CourseStudent(context,
						cursor.getInt(6),
						course,
						student);
				
				AttendanceEvent event = new AttendanceEvent(context, 
						cursor.getInt(2), 
						cursor.getString(3),
						cursor.getString(4),
						cursor.getString(5));
				
				attendance = new Attendance(context,
						cursor.getInt(0),
						cursor.getString(1),
						event,
						courseStudent);
			} catch (CheckFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	
    	cursor.close();
		
		return attendance;
	}
	
	/**
	 * Return all Attendance student/course.
	 * 
	 * @param courseId
	 * @param studentId
	 * @return
	 */
	public List<Attendance> getAllStudentCourseAttendance(Integer courseId,
			Integer studentId) {
		List<Attendance> attendanceList = new ArrayList<Attendance>();
		
		String query = "SELECT " +
				DBHelper.ATTENDANCE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.ATTENDANCE_TBL_NAME + "." + DBHelper.ATTENDANCE_DATE_FIELD_NAME + ", " +
				
				DBHelper.ATTENDANCE_EVENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.ATTENDANCE_EVENT_TBL_NAME + "." + DBHelper.ICON_FIELD_NAME + ", " +
				DBHelper.ATTENDANCE_EVENT_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +
				DBHelper.ATTENDANCE_EVENT_TBL_NAME + "." + DBHelper.DESCRIPTION_FIELD_NAME + ", " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +

				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.LEVEL_FIELD_NAME + ", " + 
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.GROUP_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.TUTOR_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.CONCEPT_WEIGHT_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.PROCEDURE_WEIGHT_FIELD_NAME + ", " +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ATTITUDE_WEIGHT_FIELD_NAME + ", " +
				
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.NAME_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.LASTNAME_FIELD_NAME + ", " +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.PHOTO_FIELD_NAME + " " +
				
				"FROM " +
				DBHelper.ATTENDANCE_TBL_NAME + ", " + DBHelper.ATTENDANCE_EVENT_TBL_NAME + ", " +
				DBHelper.COURSE_STUDENT_TBL_NAME + ", " + DBHelper.COURSE_TBL_NAME + ", " + DBHelper.STUDENT_TBL_NAME + " " +
				
				"WHERE " + 
				DBHelper.ATTENDANCE_TBL_NAME + "." + DBHelper.EVENT_FIELD_NAME + "=" + 
				DBHelper.ATTENDANCE_EVENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " AND " +
				
				DBHelper.ATTENDANCE_TBL_NAME + "." + DBHelper.COURSE_STUDENT_FIELD_NAME + "=" +
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_COURSE_FIELD_NAME + "=" +
				DBHelper.COURSE_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_STUDENT_FIELD_NAME + "=" +
				DBHelper.STUDENT_TBL_NAME + "." + DBHelper.ID_FIELD_NAME + " AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_COURSE_FIELD_NAME + "=" + courseId + " AND " +
				
				DBHelper.COURSE_STUDENT_TBL_NAME + "." + DBHelper.ID_STUDENT_FIELD_NAME + "=" + studentId + ";";
				
		Cursor cursor = db.rawQuery(query, null);
    	
    	if(cursor.moveToFirst()) {
			try {
				do {
					Course course = new Course(context,
		    				cursor.getInt(7),
		    				cursor.getString(8),
		    				cursor.getString(9),
		    				cursor.getString(10),
		    				cursor.getString(11),
		    				cursor.getFloat(12),
		    				cursor.getFloat(13),
		    				cursor.getFloat(14));
					
					Student student = new Student(context,
							cursor.getInt(15),
							cursor.getString(16),
							cursor.getString(17),
							cursor.getString(18));
					
					CourseStudent courseStudent = new CourseStudent(context,
							cursor.getInt(6),
							course,
							student);
					
					AttendanceEvent event = new AttendanceEvent(context, 
							cursor.getInt(2), 
							cursor.getString(3),
							cursor.getString(4),
							cursor.getString(5));
					
					Attendance attendance = new Attendance(context,
							cursor.getInt(0),
							cursor.getString(1),
							event,
							courseStudent);
					
					attendanceList.add(attendance);
				} while (cursor.moveToNext());
			} catch (CheckFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	
    	cursor.close();
		
		return attendanceList;
	}

	/** 
	 * Return true if there are courses.
	 * 
	 * @return
	 */
	public boolean areThereCourses() {
		String query = "SELECT * FROM " + DBHelper.COURSE_TBL_NAME;
		Cursor cursor = db.rawQuery(query, null);
		
		return (cursor.getCount() != 0);
	}
}
