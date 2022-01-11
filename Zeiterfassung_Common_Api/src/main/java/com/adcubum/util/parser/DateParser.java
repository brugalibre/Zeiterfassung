/**
 * 
 */
package com.adcubum.util.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;

/**
 * @author Dominic
 *
 */
public class DateParser {

   private static final String HOUR_MIN_PATTERN = "HH:mm";
   public static final String HOUR_MIN_SEC_PATTERN = "HH:mm:ss";
   public static final String DD_MM_YYYY = "dd.MM.yyyy";
   public static final String YYYY_MM_DD_T_HH_MINMIN_SS_SSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
   private static final String DATE_WITH_SEC_PATTERN = "dd-MM-yyyy " + HOUR_MIN_SEC_PATTERN;
   public static final String DATE_PATTERN = "dd.MM.yyyy HH:mm";

   private DateParser() {
      // private 
   }

   private static final String DOUBLE_POINT = ":";

   /**
    * Returns the String representation for the given {@link DateTime} instance
    * 
    * @param duration
    * @return the String representation for the given {@link DateTime} instance
    */
   public static String parse2String(DateTime time) {
      return parse2String(time, HOUR_MIN_PATTERN);
   }

   /**
    * Returns the String representation for the given {@link DateTime} instance
    * 
    * @param duration
    * @return the String representation for the given {@link DateTime} instance
    */
   public static String parse2String(DateTime time, String pattern) {
      SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT);
      df.applyPattern(pattern);
      Date date = new Date(time.getTime());
      return df.format(date);
   }

   /**
    * Returns the default representation of a date using the given pattern
    * 
    * @param date
    *        the {@link Date} to parse
    * @param pattern
    *        the pattern to use
    * @return the default representation of a date using the given pattern
    */
   public static String parse2String(LocalDate date, String pattern) {
      return date.format(DateTimeFormatter.ofPattern(pattern));
   }

   public static DateTime getTime(String input, DateTime currentSetDate) {
      try {
         return getTime(input, new Date(currentSetDate.getTime()));
      } catch (ParseException e) {
         // ignore since we do not care
      }
      return currentSetDate;
   }

   /**
    * Tries to parse the given input String into a {@link DateTime} instance
    * 
    * @param input
    *        the given input value
    * @param currentDate
    *        the current Date which is used to append the given input
    * @return a new {@link DateTime} instance
    * @throws ParseException
    */
   public static DateTime getTime(String input, Date currentDate) throws ParseException {
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
      return DateTimeFactory.createNew(date.getTime(), RoundMode.SEC);
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
   public static String convertInputWithSeconds(String input) throws ParseException {
      try {
         String neutralizedInput = input.replace(DOUBLE_POINT, "");
         if (neutralizedInput.length() == 3) {
            neutralizedInput = "0" + neutralizedInput;
         }
         String hour = neutralizedInput.substring(0, 2);
         String min = neutralizedInput.substring(2, 4);
         String sec = "00";
         if (containsSeconds(neutralizedInput)) {
            sec = neutralizedInput.substring(4, 6);
         }
         return new StringBuilder(hour + DOUBLE_POINT + min + DOUBLE_POINT + sec).toString();
      } catch (StringIndexOutOfBoundsException e) {
         throw new ParseException(input, 0);
      }
   }

   private static boolean containsSeconds(String neutralizedInput) {
      return neutralizedInput.length() >= 6;
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
   public static String convertInput(String input) throws ParseException {
      String convertedInputWithSeconds = convertInputWithSeconds(input);
      return convertedInputWithSeconds.substring(0, convertedInputWithSeconds.lastIndexOf(":"));
   }

   public static Date parse2Date(String readLine, String dateRepPattern) throws ParseException {
      SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance();
      df.applyPattern(dateRepPattern);
      return df.parse(readLine);
   }

   /**
    * Converts the given String input and parses the resulting value to a {@link DateTime}
    * With the given {@link TimeUnit} it's possible to define the accuracy.
    * E.g.: The input '800': This will be converted to '08:00' whereas this value
    * is parsed into a Time. Use {@link TimeUnit#SECONDS} if you want to include seconds in the resulting Time instance
    * 
    * 
    * @param timeAsString
    *        the time as a String
    * @param timeUnit
    *        the {@link TimeUnit} which defines the accuracy of the returned time
    * @return a {@link DateTime} instance for the given time value as a String
    */
   public static DateTime convertAndParse2Time(String timeAsString, TimeUnit timeUnit) {
      return convertString2Time(timeAsString, timeUnit);
   }

   private static DateTime convertString2Time(String timeAsString, TimeUnit timeUnit) {
      try {
         String convertedTimeStampValue;
         if (timeUnit == TimeUnit.SECONDS) {
            convertedTimeStampValue = DateParser.convertInputWithSeconds(timeAsString);
         } else {
            convertedTimeStampValue = DateParser.convertInput(timeAsString);
         }
         return DateParser.getTime(convertedTimeStampValue, new Date());
      } catch (ParseException e) {
         throw new IllegalStateException(e);
      }
   }
}
