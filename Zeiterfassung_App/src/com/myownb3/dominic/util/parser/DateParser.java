/**
 * 
 */
package com.myownb3.dominic.util.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Dominic
 *
 */
public class DateParser {

    public static Date getDate(String input, Date currentSetDate) {
	try {
	    SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT);
	    df.applyPattern("dd-MM-yyyy HH:mm:ss");

	    // Parse the current Date Value
	    String currentDateAsString = df.format(currentSetDate);
	    // Parse the current set Date in order to receive information about year, month
	    // and day
	    String yearMonthDayInfo = currentDateAsString.substring(0, currentDateAsString.length() - 8);
	    // Append the information about hour, minutes and seconds to the given
	    // information
	    return df.parse(yearMonthDayInfo + input);
	} catch (ParseException e) {
	    System.err.println(e.getMessage());
	}
	return currentSetDate;
    }
}
