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

import es.rgmf.ltn.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Database helper.
 * 
 * This class is package-protected (by default) and is only visible by {@file DBAdapter.java}.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 *
 */
class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "libreteachernotebook.db";
    
    public static final String ATTENDANCE_EVENT_NOT_JUSTIFY_NAME = "Falta no justificada";
    public static final String ATTENDANCE_EVENT_JUSTIFY_NAME = "Falta justificada";
    public static final String ATTENDANCE_EVENT_DELAY_NAME = "Retraso";
    
    /*************************** Table names *********************************/
    public static final String COURSE_TBL_NAME = "course";
    public static final String EVALUATION_TBL_NAME = "evaluation";
    public static final String STUDENT_TBL_NAME = "student";
    public static final String ALL_ATTITUDE_TBL_NAME = "all_attitude";
    public static final String COURSE_EVALUATION_TBL_NAME = "course_evaluation";
    public static final String COURSE_STUDENT_TBL_NAME = "course_student";
    public static final String TEST_TBL_NAME = "test";
    public static final String EXAM_TBL_NAME = "exam";
    public static final String PRACTICE_TBL_NAME = "practice";
    public static final String ATTITUDE_TBL_NAME = "attitude";
    public static final String EXAM_STUDENT_MARK_TBL_NAME = "exam_student_mark";
    public static final String PRACTICE_STUDENT_MARK_TBL_NAME = "practice_student_mark";
    public static final String ATTITUDE_STUDENT_MARK_TBL_NAME = "attitude_student_mark";
    public static final String ATTENDANCE_EVENT_TBL_NAME = "attendance_event";
    public static final String ATTENDANCE_TBL_NAME = "attendance";
    public static final String DAY_TYPE_TBL_NAME = "day_type";
    public static final String DAY_COLUMN_TBL_NAME = "day_column";
    public static final String HOUR_ROW_TBL_NAME = "hour_row";
    public static final String HOUR_CELL_TBL_NAME = "hour_cell";
    /*************************************************************************/
    
    /*************************** Fields names ********************************/
    public static final String ID_FIELD_NAME = "id";
    public static final String LEVEL_FIELD_NAME = "level";
    public static final String NAME_FIELD_NAME = "name";
    public static final String GROUP_FIELD_NAME = "cgroup";
    public static final String TUTOR_FIELD_NAME = "tutor";
    public static final String LASTNAME_FIELD_NAME = "lastname";
    public static final String PHOTO_FIELD_NAME = "photo";
    public static final String ID_STUDENT_FIELD_NAME = "id_student";
    public static final String ID_COURSE_FIELD_NAME = "id_course";
    public static final String ID_EVALUATION_FIELD_NAME = "id_evaluation";
    public static final String START_DATE_FIELD_NAME = "start_date";
    public static final String END_DATE_FIELD_NAME = "end_date";
    public static final String EXAMDATE_FIELD_NAME = "examdate";
    public static final String DESCRIPTION_FIELD_NAME = "description";
    public static final String WEIGHT_FIELD_NAME = "weight";
    public static final String STUDENT_FIELD_NAME = "student";
    public static final String EXAM_FIELD_NAME = "exam";
    public static final String MARK_FIELD_NAME = "mark";
    public static final String COURSE_EVALUATION_FIELD_NAME = "course_evaluation";
    public static final String PRACTICE_FIELD_NAME = "practice";
    public static final String START_DATE_2_FIELD_NAME = "startdate";
    public static final String END_DATE_2_FIELD_NAME = "enddate";
    public static final String ATTITUDEDATE_FIELD_NAME = "attitudedate";
    public static final String ATTITUDE_FIELD_NAME = "attitude";
    public static final String COMMENT_FIELD_NAME = "comment";
    public static final String ALL_ATTITUDE_FIELD_NAME = "all_attitude";
    public static final String CONCEPT_WEIGHT_FIELD_NAME = "concept_weight";
    public static final String PROCEDURE_WEIGHT_FIELD_NAME = "procedure_weight";
    public static final String ATTITUDE_WEIGHT_FIELD_NAME = "attitude_weight";
    public static final String ATTENDANCE_DATE_FIELD_NAME = "attendance_date";
    public static final String EVENT_FIELD_NAME = "event";
    public static final String COURSE_STUDENT_FIELD_NAME = "course_student";
    public static final String ICON_FIELD_NAME = "icon";
    /*************************************************************************/
    
    private static final String COURSE_TBL = "create table " + COURSE_TBL_NAME + " (" + 
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		LEVEL_FIELD_NAME + " text not null, " +
    		NAME_FIELD_NAME + " text not null, " + 
    		GROUP_FIELD_NAME + " text not null, " +
    		CONCEPT_WEIGHT_FIELD_NAME + " real not null, " +
    		PROCEDURE_WEIGHT_FIELD_NAME + " real not null, " +
    		ATTITUDE_WEIGHT_FIELD_NAME + " real not null, " +
    		TUTOR_FIELD_NAME + " text);";
    
    private static final String EVALUATION_TBL = "create table " + EVALUATION_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		NAME_FIELD_NAME + " text not null, " +
    		"start_date date not null, " +
    		"end_date date not null);";
    
    private static final String STUDENT_TBL = "create table " + STUDENT_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		NAME_FIELD_NAME + " text not null, " + 
    		LASTNAME_FIELD_NAME + " text not null, " +
    		PHOTO_FIELD_NAME + " text);";
    
    private static final String ALL_ATTITUDE_TBL = "create table " + ALL_ATTITUDE_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		NAME_FIELD_NAME + " text not null, " +
    		DESCRIPTION_FIELD_NAME + " text not null);";
    
    private static final String COURSE_EVALUATION_TBL = "create table " + COURSE_EVALUATION_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		ID_COURSE_FIELD_NAME + " integer not null, " + 
    		ID_EVALUATION_FIELD_NAME + " integer not null, " +
    		"unique (id_course, id_evaluation), " +
    		"foreign key (id_course) references course (id) on delete cascade on update cascade, " +
    		"foreign key (id_evaluation) references evaluation (id) on delete cascade on update cascade);";
    
    private static final String COURSE_STUDENT_TBL = "create table course_student (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		ID_COURSE_FIELD_NAME + " integer not null, " +
    		ID_STUDENT_FIELD_NAME + " integer not null, " +
    		"unique (" + ID_COURSE_FIELD_NAME + ", " + ID_STUDENT_FIELD_NAME + "), " +
    		"foreign key (id_course) references " + COURSE_TBL_NAME + " (id) on delete cascade on update cascade, " +
    		"foreign key (id_student) references " + STUDENT_TBL_NAME + " (id) on delete cascade on update cascade);";
    
    private static final String TEST_TBL = "create table " + TEST_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		NAME_FIELD_NAME + " text not null, " +
    		DESCRIPTION_FIELD_NAME + " text, " +
    		WEIGHT_FIELD_NAME + " real default 1, " +
    		COURSE_EVALUATION_FIELD_NAME + " integer references " + COURSE_EVALUATION_TBL_NAME + " (id) on delete cascade on update cascade);";
    
    private static final String EXAM_TBL = "create table " + EXAM_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key, " +
    		EXAMDATE_FIELD_NAME + " date not null, " +
    		"foreign key (id) references " + TEST_TBL_NAME + " (id) on delete cascade on update cascade);";
    
    private static final String PRACTICE_TBL = "create table " + PRACTICE_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key, " +
    		START_DATE_2_FIELD_NAME + " date not null, " +
    		END_DATE_2_FIELD_NAME + " date not null, " +
    		"foreign key (id) references " + TEST_TBL_NAME + " (id) on delete cascade on update cascade);";
    
    private static final String ATTITUDE_TBL = "create table " + ATTITUDE_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key, " +
    		WEIGHT_FIELD_NAME + " real default -1, " + // A real value between -1 and 1 (1 is a positive and -1 a negative).
    		COURSE_EVALUATION_FIELD_NAME + " integer references " + COURSE_EVALUATION_TBL_NAME + " (id) on delete cascade on update cascade, " +
    		ALL_ATTITUDE_FIELD_NAME + " integer not null references " + ALL_ATTITUDE_TBL_NAME + " (id) on delete cascade on update cascade);";
    
    private static final String EXAM_STUDENT_MARK_TBL = "create table " + EXAM_STUDENT_MARK_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		STUDENT_FIELD_NAME + " integer not null references " + STUDENT_TBL_NAME + " (id) on delete cascade on update cascade, " +
    		EXAM_FIELD_NAME + " integer not null references " + EXAM_TBL_NAME + " (id) on delete cascade on update cascade, " +
    		COMMENT_FIELD_NAME + " text, " +
    		MARK_FIELD_NAME + " real default 0);";
    
    private static final String PRACTICE_STUDENT_MARK_TBL = "create table " + PRACTICE_STUDENT_MARK_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		STUDENT_FIELD_NAME + " integer not null references " + STUDENT_TBL_NAME + " (id) on delete cascade on update cascade, " +
    		PRACTICE_FIELD_NAME + " integer not null references " + PRACTICE_TBL_NAME + " (id) on delete cascade on update cascade, " +
    		COMMENT_FIELD_NAME + " text, " +
    		MARK_FIELD_NAME + " real default 0);";
    
    private static final String ATTITUDE_STUDENT_MARK_TBL = "create table " + ATTITUDE_STUDENT_MARK_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		STUDENT_FIELD_NAME + " integer not null references " + STUDENT_TBL_NAME + " (id) on delete cascade on update cascade, " +
    		ATTITUDEDATE_FIELD_NAME + " date default current_date, " +
    		ATTITUDE_FIELD_NAME + " integer not null references " + ATTITUDE_TBL_NAME + " (id) on delete cascade on update cascade);";
    
    private static final String ATTENDANCE_EVENT_TBL = "create table " + ATTENDANCE_EVENT_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		ICON_FIELD_NAME + " text not null, " + // Name icon into drawable folder.
    		NAME_FIELD_NAME + " text not null, " +
    		DESCRIPTION_FIELD_NAME + " text);";
    
    private static final String ATTENDANCE_TBL = "create table " + ATTENDANCE_TBL_NAME + " (" +
    		ID_FIELD_NAME + " integer primary key autoincrement, " +
    		ATTENDANCE_DATE_FIELD_NAME + " date not null, " +
    		EVENT_FIELD_NAME + " integer not null references " + ATTENDANCE_EVENT_TBL_NAME + " (id) on delete cascade on update cascade, " +
    		COURSE_STUDENT_FIELD_NAME + " integer not null references " + COURSE_STUDENT_TBL_NAME + " (id) on delete cascade on update cascade, " +
    		"unique (" + ATTENDANCE_DATE_FIELD_NAME + ", " + COURSE_STUDENT_FIELD_NAME + "));";
    		
    		
    /**
     * Constructor.
     * 
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method that is called the once time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL(COURSE_TBL);
    	db.execSQL(EVALUATION_TBL);
    	db.execSQL(STUDENT_TBL);
    	db.execSQL(ALL_ATTITUDE_TBL);
    	db.execSQL(COURSE_EVALUATION_TBL);
    	db.execSQL(COURSE_STUDENT_TBL);
    	db.execSQL(TEST_TBL);
    	db.execSQL(EXAM_TBL);
    	db.execSQL(PRACTICE_TBL);
    	db.execSQL(ATTITUDE_TBL);
    	db.execSQL(EXAM_STUDENT_MARK_TBL);
    	db.execSQL(PRACTICE_STUDENT_MARK_TBL);
    	db.execSQL(ATTITUDE_STUDENT_MARK_TBL);
    }

    /**
     * To update the database between versions.
     */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch(newVersion) {
			/* In this version it added tables to management classroom attendance */
			case 2:
				db.execSQL(ATTENDANCE_EVENT_TBL);
				db.execSQL(ATTENDANCE_TBL);
				
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES ('" + ATTENDANCE_EVENT_NOT_JUSTIFY_NAME + "', '" + ATTENDANCE_EVENT_NOT_JUSTIFY_NAME + "');");
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES ('" + ATTENDANCE_EVENT_JUSTIFY_NAME + "', '" + ATTENDANCE_EVENT_JUSTIFY_NAME + "');");
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES ('" + ATTENDANCE_EVENT_DELAY_NAME + "', '" + ATTENDANCE_EVENT_DELAY_NAME + "');");
				
				break;
			/* In this version drop the attendance and create back */
			case 3:
				db.execSQL("DROP TABLE " + ATTENDANCE_TBL_NAME + ";");
				db.execSQL(ATTENDANCE_TBL);
				break;
			/* In this version it adds icon field to attendance event table */
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				db.execSQL("DROP TABLE " + ATTENDANCE_TBL_NAME + ";");
				db.execSQL("DROP TABLE " + ATTENDANCE_EVENT_TBL_NAME + ";");
				
				db.execSQL(ATTENDANCE_EVENT_TBL);
				db.execSQL(ATTENDANCE_TBL);
				
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						ICON_FIELD_NAME + ", " + NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES (" + R.drawable.ic_attendance_yes + ", 'Asiste', 'Asiste');");
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						ICON_FIELD_NAME + ", " + NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES (" + R.drawable.ic_attendance_no + ", 'Falta no justificada', 'Falta no justificada');");
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						ICON_FIELD_NAME + ", " + NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES (" + R.drawable.ic_attendance_no_justify + ", 'Falta justificada', 'Falta justificada');");
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						ICON_FIELD_NAME + ", " + NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES (" + R.drawable.ic_attendance_delay + ", 'Retraso', 'Retraso');");
				break;
			/* In this version we change the type of the ICON_FIELD_NAME to string and load the name of the logo */
			case 9:
			case 10:
				db.execSQL("DROP TABLE " + ATTENDANCE_TBL_NAME + ";");
				db.execSQL("DROP TABLE " + ATTENDANCE_EVENT_TBL_NAME + ";");
				
				db.execSQL(ATTENDANCE_EVENT_TBL);
				db.execSQL(ATTENDANCE_TBL);
				
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						ICON_FIELD_NAME + ", " + NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES ('ic_attendance_yes', 'Asiste', 'Asiste');");
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						ICON_FIELD_NAME + ", " + NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES ('ic_attendance_no', 'Falta no justificada', 'Falta no justificada');");
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						ICON_FIELD_NAME + ", " + NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES ('ic_attendance_no_justify', 'Falta justificada', 'Falta justificada');");
				db.execSQL("INSERT INTO " + ATTENDANCE_EVENT_TBL_NAME + " (" + 
						ICON_FIELD_NAME + ", " + NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME + ") " +
						"VALUES ('ic_attendance_delay', 'Retraso', 'Retraso');");
				break;
		}
	}
}
