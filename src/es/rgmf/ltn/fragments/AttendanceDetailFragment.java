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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import es.rgmf.ltn.R;
import es.rgmf.ltn.StudentActivity;
import es.rgmf.ltn.adapters.AttendanceEventListAdapter;
import es.rgmf.ltn.model.ReaderModel;
import es.rgmf.ltn.model.WriterModel;
import es.rgmf.ltn.model.orm.Attendance;
import es.rgmf.ltn.model.orm.AttendanceEvent;
import es.rgmf.ltn.model.orm.Student;
import es.rgmf.ltn.util.Dates;
import es.rgmf.ltn.util.ui.StudentOptionsContent;

/**
 * The fragment with the attendance detail from a course.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class AttendanceDetailFragment extends Fragment {
	/**
	 * The View. It can be used to access xml elements of this View.
	 */
	private View mRootView;
	/**
	 * The context.
	 */
	private Context mContext;
	/**
	 * The course selected.
	 */
	private Integer mCourseId;
	/**
	 * The attendance list of objects.
	 */
	private List<Attendance> mAttendanceList = new ArrayList<Attendance>();
	/**
	 * The spinner adapter.
	 */
	private AttendanceEventListAdapter mSpinnerAdapter;
	/**
	 * Date to the attendance.
	 */
	private Calendar mCalendar;
	
	/**
	 * Create the instance of this class with course id.
	 * @param id The identifier of the course.
	 * @return the instance of this class.
	 */
	public static Fragment newInstance(Integer courseId) {
		AttendanceDetailFragment fragment = new AttendanceDetailFragment();
		fragment.mCourseId = courseId;
		fragment.mCalendar = Calendar.getInstance();
		return fragment;
	}
	
	/**
	 * Create the instance of this class with course id and calendar.
	 * 
	 * @param courseId The course identify.
	 * @param calendar The calendar.
	 * @return
	 */
	public static Fragment newInstance(Integer courseId, Calendar calendar) {
		AttendanceDetailFragment fragment = new AttendanceDetailFragment();
		fragment.mCourseId = courseId;
		fragment.mCalendar = calendar;
		return fragment;
	}
	
	/**
	 * Method that is called when the view is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_attendance_detail, container, false);
		TableLayout table = (TableLayout) mRootView.findViewById(R.id.attendance_table_layout);

		if(getActivity() != null) {
			this.mContext = getActivity().getApplicationContext();
		}
		
		// On ok click button listener.
		mRootView.findViewById(R.id.add_attendance_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WriterModel.addAttendanceStudents(mContext, mAttendanceList);
				Toast.makeText(mContext, getString(R.string.msg_changes_done), Toast.LENGTH_SHORT).show();
			}
		});
		
		if(mCourseId != null) {
			// Get students enrolled in this course.
			ArrayList<Student> studentList = ReaderModel.getCourseStudents(getActivity(), mCourseId);
			
			// If there are students enrolled then it shows theses students. Otherwise, it shows a 
			// message.
			if(studentList.size() > 0) {
				// Hide the message showed when there are not students.
				mRootView.findViewById(R.id.attendance_detail_msg).setVisibility(View.GONE);
				
				// Create the table.
				table.setStretchAllColumns(true);
		        table.setShrinkAllColumns(true);
		        
		        TableRow rowTitle = new TableRow(getActivity());
		        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
		
		        TableRow rowHeadLabels = new TableRow(getActivity());
		        
		        // title of the table.
		        TextView title = new TextView(getActivity());
		        title.setText(getString(R.string.attendance_table_title));
		        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (int) getResources().getDimension(R.dimen.text_size_xxlarge));
		        title.setGravity(Gravity.CENTER);
		
		        TableRow.LayoutParams params = new TableRow.LayoutParams();
		        params.span = 2;
		
		        rowTitle.addView(title, params);
		        
		        // student and date head columns.
		        // Student.
		        TextView studentTitleLabel = new TextView(getActivity());
		        studentTitleLabel.setText(R.string.student_title_table);
		        studentTitleLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (int) getResources().getDimension(R.dimen.text_size_xlarge));
		        studentTitleLabel.setGravity(Gravity.CENTER);
		        // Date.
		        //final TextView dateTitleLabel = new TextView(getActivity());
		        final Button buttonDate = new Button(mContext);
		        //dateTitleLabel.setText(Dates.getStringFromCalendar(mCalendar));
		        buttonDate.setText(Dates.getStringFromCalendar(mCalendar));
		        //dateTitleLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (int) getResources().getDimension(R.dimen.text_size_xlarge));
		        buttonDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (int) getResources().getDimension(R.dimen.text_size_xlarge));
		        buttonDate.setTextColor(getResources().getColor(R.color.black));
		        //dateTitleLabel.setGravity(Gravity.CENTER);
		        buttonDate.setGravity(Gravity.CENTER);
		        //dateTitleLabel.setClickable(true);
		        //dateTitleLabel.setOnClickListener(new OnClickListener() {
		        buttonDate.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
			                        R.style.AppBaseTheme, datePickerListener,
			                        mCalendar.get(Calendar.YEAR), 
			                        mCalendar.get(Calendar.MONTH),
			                        mCalendar.get(Calendar.DAY_OF_MONTH));
			                datePicker.setCancelable(false);
			                datePicker.setTitle(getString(R.string.select_the_date_hint));
			                datePicker.show();
			            } catch (Exception e) {}
					}

					private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
				        // when dialog box is closed, below method will be called.
				        public void onDateSet(DatePicker view, int selectedYear,
				                int selectedMonth, int selectedDay) {
				            String year = String.valueOf(selectedYear);
				            String month = String.valueOf(selectedMonth + 1);
				            String day = String.valueOf(selectedDay);
				            mCalendar = Dates.getCalendarFromString(day, month, year);
				            buttonDate.setText(Dates.getStringFromCalendar(mCalendar));
				            FragmentManager fm = getFragmentManager();
							FragmentTransaction ft = fm.beginTransaction();
							ft.replace(R.id.attendance_detail, AttendanceDetailFragment.newInstance(mCourseId, mCalendar));
							ft.commit();
				        }
				    };
				});
		        
		        rowHeadLabels.addView(studentTitleLabel);
		        rowHeadLabels.addView(buttonDate);
		        
		        // Add to the table the row with the title and the row with the head labels.
		        table.addView(rowTitle);
		        table.addView(rowHeadLabels);
				
		        // The rows with student information with all students of this course.
		        ArrayList<AttendanceEvent> attendanceEventList = ReaderModel.getAllAttendanceEvents(getActivity());
		        mSpinnerAdapter = new AttendanceEventListAdapter(getActivity(), attendanceEventList);
		        for(int i = 0; i < studentList.size(); i++) {
		            TableRow tableRow = new TableRow(getActivity());
		            tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		            	     LayoutParams.WRAP_CONTENT));
		            
		            TextView tvStudent = new TextView(getActivity());
		            tvStudent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (int) getResources().getDimension(R.dimen.text_size_large));
		            tvStudent.setPadding(0, 10, 0, 10);
		            tvStudent.setText(studentList.get(i).getLastname() + ", " + 
		            		studentList.get(i).getName());
		            tvStudent.setId(studentList.get(i).getId());
		            tvStudent.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, StudentActivity.class);
							intent.putExtra(StudentActivity.STUDENT_ID, v.getId());
							intent.putExtra(StudentActivity.COURSE_ID, mCourseId);
							// Select the option to select from StudentActivity Activity.
							intent.putExtra(StudentActivity.OPTION_ID, StudentOptionsContent.ATTENDANCE);
					    	startActivity(intent);
						}
					});
		            
		            Spinner sAttendance = new Spinner(getActivity());
		            sAttendance.setId(i);
		            sAttendance.setAdapter(mSpinnerAdapter);
		            sAttendance.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
							int i = parentView.getId();
							mAttendanceList.get(i).getEvent().setId(((AttendanceEvent) mSpinnerAdapter.getItem(position)).getId());
							mAttendanceList.get(i).getEvent().setIcon(((AttendanceEvent) mSpinnerAdapter.getItem(position)).getIcon());
							mAttendanceList.get(i).getEvent().setName(((AttendanceEvent) mSpinnerAdapter.getItem(position)).getName());
							mAttendanceList.get(i).getEvent().setDescription(((AttendanceEvent) mSpinnerAdapter.getItem(position)).getDescription());
						}

						@Override
						public void onNothingSelected(AdapterView<?> parentView) {
						}
					});
		            
		            // Retrieve Attendance of the student (if not exist create one new).
		            Attendance obj = ReaderModel.getAttendance(mContext, Dates.getStringFromCalendar(mCalendar), 
		            		mCourseId, studentList.get(i).getId());
		            if(obj == null) {
		            	obj = new Attendance(mContext, Dates.getStringFromCalendar(mCalendar), 
		            			(AttendanceEvent) mSpinnerAdapter.getItem(0), 
		            			ReaderModel.getCourseStudent(mContext, mCourseId, studentList.get(i).getId()));
		            }
		            // We need to create an AttendanceEvent copy of the obj because later we have problems (need a copy, not the object).
		            AttendanceEvent ae = new AttendanceEvent(mContext, obj.getEvent().getId(), obj.getEvent().getIcon(), obj.getEvent().getName(), obj.getEvent().getDescription());
		            mAttendanceList.add(new Attendance(mContext, obj.getId(), obj.getAttendanceDate(), ae, obj.getCourseStudent()));
		            sAttendance.setSelection(mSpinnerAdapter.indexOf(obj.getEvent().getId()));
		            
		            tableRow.addView(tvStudent);
		            tableRow.addView(sAttendance);
		            table.addView(tableRow);
		        }
			}
			else {
				((TextView) mRootView.findViewById(R.id.attendance_detail_msg))
					.setText(getActivity().getString(R.string.there_are_not_student_in_this_course));
				// Hide the RelativeLayout where all detail fields are.
				mRootView.findViewById(R.id.attendance_table_layout).setVisibility(View.GONE);
			}
		}
		
		return mRootView;
	}
}
