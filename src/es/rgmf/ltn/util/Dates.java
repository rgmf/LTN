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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.DatePicker;

/**
 * Utilities for dates.
 * 
 * @author Román Ginés Martínez Ferrández <rgmf@riseup.net>
 */
public class Dates {
	/**
	 * Convert DatePiker to java.util.Date.
	 * 
	 * @param datePicker
	 * @return
	 */
	public static Date getDateFromDatePicker(DatePicker datePicker){
	    int day = datePicker.getDayOfMonth();
	    int month = datePicker.getMonth();
	    int year =  datePicker.getYear();

	    Calendar calendar = Calendar.getInstance();
	    calendar.set(year, month, day);

	    return calendar.getTime();
	}
	
	/**
	 * Convert a DatePicker to string "yyyy/MM/dd" format.
	 * 
	 * @param datePicker
	 * @return
	 */
	public static String getStringFromDatePicker(DatePicker datePicker) {
		Date date = getDateFromDatePicker(datePicker);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		
		return dateFormat.format(date);
	}
	
	/**
	 * Return a calendar from string. If the string format date is not correct
	 * then return the current date.
	 * 
	 * @param strDate The string representing a date.
	 * @return The date in object.
	 */
	public static Calendar getCalendarFromString(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar = Calendar.getInstance();
		Date date;
		try {
			date = sdf.parse(strDate);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return calendar;
	}
	
	/**
	 * Return a calendar from string. If the string format date is not correct
	 * then return the current date.
	 * 
	 * @param day
	 * @param month
	 * @param year
	 * @return The date in object.
	 */
	public static Calendar getCalendarFromString(String day, String month, String year) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar = Calendar.getInstance();
		Date date;
		try {
			date = sdf.parse(year + "/" + month + "/" + day);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return calendar;
	}
	
	/**
	 * Return the string from the calendar.
	 * 
	 * @param calendar The calendar date.
	 * @return the string representing the calendar date.
	 */
	public static String getStringFromCalendar(Calendar calendar) {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd");
		String strDate = formatter.format(calendar.getTime());
		return strDate;
	}
	
	/**
	 * Return today date.
	 */
	public static String getTodayDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String currentDate = sdf.format(new Date());
		return currentDate;
	}
}
