package com.myownb3.dominic.util.parser;

import static java.util.Objects.isNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class NumberFormat {

   private NumberFormat() {
      // private 
   }

   private static final java.text.NumberFormat decimalFormat;

   static {
      decimalFormat = new DecimalFormat("0.00");
   }

   public static float parse(float time, int factor) {

      java.text.NumberFormat format = java.text.NumberFormat.getInstance();
      Number number = Float.valueOf(0);
      try {
         number = format.parse(decimalFormat.format(time / factor));
      } catch (ParseException e) {
         throw new NumberFormatException(e.getLocalizedMessage());
      }
      float floatValue = number.floatValue();
      if (floatValue == -0.00) {
         return 0; // avoid a '-0.00' when start is slightly after stop 
      }
      return floatValue;
   }

   public static float parseFloat(String float2Parse) {
      java.text.NumberFormat format = java.text.NumberFormat.getInstance();
      try {
         String neutralizedValue2Parse = neutralizeDecimalSeparator(float2Parse, format);
         Number number = format.parse(neutralizedValue2Parse);
         return number.floatValue();
      } catch (ParseException e) {
         e.printStackTrace();
         throw new NumberFormatException(e.getLocalizedMessage());
      }
   }

   /**
    * 
    * Returns a parsed Float of the given String or the given default value<code>null</code> if the String in not a number
    * 
    * @param stringValue2Parse
    *        the String to parse
    * @param defaultReturnValue
    *        the desired default value
    * @return a parsed Float of the given String or the given default value<code>null</code> if the String in not a number
    */
   public static float parseFloatOrDefault(String stringValue2Parse, float defaultReturnValue) {
      java.text.NumberFormat format = java.text.NumberFormat.getInstance();
      try {
         String neutralizedValue2Parse = neutralizeDecimalSeparator(stringValue2Parse, format);
         Number number = format.parse(neutralizedValue2Parse);
         return number.floatValue();
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return defaultReturnValue;
   }

   public static String format(float number) {
      java.text.NumberFormat format = java.text.NumberFormat.getInstance();
      return format.format(number);
   }

   /**
    * Returns the same text value but with a neutralized decimal separator.
    * e.g. if the user entered '1,15' and the current systems separator is a '.' we return '1.15' and vice versa
    * 
    * @param value2Parse
    *        the value to parse
    * @param formatter
    *        the current {@link java.text.NumberFormat} instance
    * @return a neutralized value
    */
   public static String neutralizeDecimalSeparator(String value2Parse, java.text.NumberFormat formatter) {
      if (isNull(value2Parse)) {
         return "";
      }
      String decimalSeparator = getDecimalFormatSymbols(formatter);
      return value2Parse.replace(",", decimalSeparator) // for those who use a ',' as decimal separator
            .replace(".", decimalSeparator);// for those who use a '.' as decimal separator
   }

   /*
    * Returns the current decimal format sympol.
    */
   private static String getDecimalFormatSymbols(java.text.NumberFormat formatter) {
      String decimalSeparator = ".";// default
      if (formatter instanceof DecimalFormat) {
         DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
         decimalSeparator = String.valueOf(decimalFormatSymbols.getDecimalSeparator());
      }
      return decimalSeparator;
   }

}
