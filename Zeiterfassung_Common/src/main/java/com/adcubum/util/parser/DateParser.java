/**
 * 
 */
package com.adcubum.util.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.adcubum.timerecording.work.date.Time;

/**
 * @author Dominic
 *
 */
public class DateParser {

   private static final String HOUR_MIN_PATTERN = "HH:mm";
   private static final String DATE_WITH_SEC_PATTERN = "dd-MM-yyyy HH:mm:ss";
   public static final String DATE_PATTERN = "dd-MM-yyyy HH:mm";

   private DateParser() {
      // private 
   }

   private static final String DOUBLE_POINT = ":";

   /**
    * Returns the String representation for the given {@link Time} instance
    * 
    * @param duration
    * @return the String representation for the given {@link Time} instance
    */
   public static String parse2String(Time time) {
      SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT);
      df.applyPattern(HOUR_MIN_PATTERN);
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
    *        the given input value
    * @param currentDate
    *        the current Date which is used to append the given input
    * @return a new {@link Time} instance
    * @throws ParseException
    */
   public static Time getTime(String input, Date currentDate) throws ParseException {
      SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT);
      df.applyPattern(DATE_WITH_SEC_PATTERN);

      // Parse the current Date Value
      String currentDateAsString = df.format(currentDate);
      // Parse the current set Date in order to receive information about
      // year, month
      // and day
      String yearMonthDayInfo = currentDateAsString.substring(0, currentDateAsString.length() - 8);
      // Append the information about hour, minutes and seconds to the given
      // information
      Date date = df.parse(yearMonthDayInfo + convertInputWithSeconds(input));
      return new Time(date.getTime());
   }

   /**
    * Converts the given input into the format hh:mm regardless if the input is
    * already in that form or not
    * 
    * @param input
    *        the input to parse, e.q. 1215 or 12:15 resp. 700 or 7:00 or 0700
    * @return a converted input, always with ':'
    * @throws ParseException
    */
   public static String convertInput(String input) throws ParseException {
      try {
         String neutralizedInput = input.replace(DOUBLE_POINT, "");
         if (neutralizedInput.length() == 3) {
            neutralizedInput = "0" + neutralizedInput;
         }
         String hour = neutralizedInput.substring(0, 2);
         String min = neutralizedInput.substring(2, 4);
         return new StringBuilder(hour + DOUBLE_POINT + min).toString();
      } catch (StringIndexOutOfBoundsException e) {
         throw new ParseException(input, 0);
      }
   }

   /**
    * Converts the given input into the format hh:mm:ss regardless if the input is
    * already in that form or not
    * 
    * @param input
    *        the input to parse, e.q. 1215 or 12:15 resp. 700 or 7:00 or 0700
    * @return a converted input, always with ':'
    * @throws ParseException
    */
   public static String convertInputWithSeconds(String input) throws ParseException {
      String sec = "00";
      return convertInput(input) + DOUBLE_POINT + sec;
   }

   public static Date parse2Date(String readLine, String dateRepPattern) throws ParseException {
      SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance();
      df.applyPattern(dateRepPattern);
      return df.parse(readLine);
   }
}
