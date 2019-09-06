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

    private static final String DOUBLE_POINT = ":";

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
	    // ignore since we do not care
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
	Date date = df.parse(yearMonthDayInfo + convertInput(input));
	return new Time(date.getTime());
    }

    /*
     * Converts the given input into the format hh:mm:ss regardless if the input is already in that form or not
     */
    private static String convertInput(String input) throws ParseException {
	try {
	    String neutralizedInput = input.replace(DOUBLE_POINT, "");
	    String hour = neutralizedInput.substring(0,2);
	    String min = neutralizedInput.substring(2,4);
	    String sec = neutralizedInput.substring(4,6);
	    return new StringBuilder(hour + DOUBLE_POINT + min + DOUBLE_POINT + sec).toString();
	} catch (StringIndexOutOfBoundsException e) {
	    throw new ParseException(input, 0);
	}
    }

    public static Date parse2Date(String readLine, String dateRepPattern) throws ParseException {
	SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance();
	df.applyPattern(dateRepPattern);
	return df.parse(readLine);
    }
}
