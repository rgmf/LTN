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

package es.rgmf.ltn.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import es.rgmf.ltn.model.orm.Evaluation;

/**
 * Session application to mantain importat datas.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class Session extends Application {
	/************************ EVALUATION INFORMATION *************************/
	/**
	 * The index where the evaluation selected be inside of the evaluations list (see below).
	 */
	private static Integer evaluationIndex = null;
	/**
	 * The list of evaluations.
	 */
	private static ArrayList<Evaluation> evaluations = null;
	
	public static void clearEvaluations() {
		Session.evaluationIndex = null;
		if(Session.evaluations != null)
			Session.evaluations.clear();
		Session.evaluations = null;
	}

	public static Integer getEvaluationIndex() {
		return evaluationIndex;
	}

	public static void setEvaluationIndex(Integer evaluationIndex) {
		Session.evaluationIndex = evaluationIndex;
	}

	public static ArrayList<Evaluation> getEvaluations() {
		return evaluations;
	}

	public static void setEvaluations(ArrayList<Evaluation> evaluations) {
		Session.evaluations = evaluations;
	}
	/*************************************************************************/
	
	
	/******************* COURSE STUDENT FRAGMENT SELECTED ********************/
	/**
	 * List with the fragment loaded in CourseStudentFragment for each
	 * course.
	 */
	private static HashMap<Integer, Integer> courseFragmentSelected = new HashMap<Integer, Integer>();
	
	public static Integer getCourseFragmentSelected(int courseId) {
		if (courseFragmentSelected != null && courseFragmentSelected.containsKey(courseId)) {
			return courseFragmentSelected.get(courseId);
		}
		else {
			return null;
		}
	}

	public static void initializeCourseFragmentSelected(ArrayList<Integer> coursesIds, Integer defaultFragment) {
		for (Integer i : coursesIds) {
			courseFragmentSelected.put(i, defaultFragment);
		}
	}
	
	public static void setCourseFragmentSelected(Integer courseId, Integer fragmentId) {
		courseFragmentSelected.put(courseId, fragmentId);
	}
	
	public static Integer getCourseFragmentSelected(Integer courseId) {
		return courseFragmentSelected.get(courseId);
	}
	/*************************************************************************/
}
