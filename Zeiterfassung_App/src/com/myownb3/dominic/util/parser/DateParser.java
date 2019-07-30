/**
 * 
 */
package com.myownb3.dominic.util.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.myownb3.dominic.timerecording.core.work.date.Time;

/**
 * @author Dominic
 *
 */
public class DateParser {

    /**
     * Returns the String representation for the given {@link Time} instance
     * 
     * @param duration
     * @return the String representation for the given {@link Time} instance
     */
    public static String parse2String(Time time) {
	SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT);
	df.applyPattern("HH:mm:ss");
	Date date = new Date(time.getTime());
	return df.format(date);
    }

    public static Time getTime(String input, Time currentSetDate) {
	try {
	    return getTime(input, new Date(currentSetDate.getTime()));
	} catch (ParseException e) {
	    System.err.println(e.getMessage());
	}
	return currentSetDate;
    }

    /**
     * Tries to parse the given input String into a {@link Time} instance
     * 
     * @param input
     *            the given input value
     * @param currentDate
     *            the current Date which is used to append the given input
     * @return a new {@link Time} instance
     * @throws ParseException
     */
    public static Time getTime(String input, Date currentDate) throws ParseException {
	SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT);
	df.applyPattern("dd-MM-yyyy HH:mm:ss");

	// Parse the current Date Value
	String currentDateAsString = df.format(currentDate);
	// Parse the current set Date in order to receive information about
	// year, month
	// and day
	String yearMonthDayInfo = currentDateAsString.substring(0, currentDateAsString.length() - 8);
	// Append the information about hour, minutes and seconds to the given
	// information
	Date date = df.parse(yearMonthDayInfo + input);
	return new Time(date.getTime());
    }

    public static Date parse2Date(String readLine, String dateRepPattern) throws ParseException {
	SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance();
	df.applyPattern(dateRepPattern);
	return df.parse(readLine);
    }
}
