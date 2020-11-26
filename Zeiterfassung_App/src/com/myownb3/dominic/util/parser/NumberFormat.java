package com.myownb3.dominic.util.parser;

import java.text.DecimalFormat;
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
         e.printStackTrace();
         throw new NumberFormatException(e.getLocalizedMessage());
      }
      return number.floatValue();
   }

   public static float parseFloat(String float2Parse) {
      java.text.NumberFormat format = java.text.NumberFormat.getInstance();
      try {
         Number number = format.parse(float2Parse);
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
         Number number = format.parse(stringValue2Parse);
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

}
