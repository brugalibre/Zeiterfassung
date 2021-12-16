/**
 * 
 */
package com.adcubum.util.utils;

import java.util.List;

/**
 * @author Dominic
 * 
 */
public class StringUtil {

   private StringUtil() {
      //private 
   }

   public static boolean isNotEmptyOrNull(String toCheck) {
      return !isEmptyOrNull(toCheck);
   }

   public static boolean isEmptyOrNull(String toCheck) {
      return toCheck == null || toCheck.trim().isEmpty();
   }

   public static boolean isEqual(String value, String valueToTest) {
      if (value == null || valueToTest == null) {
         return false;
      } else if (isNotEmptyOrNull(valueToTest) && isNotEmptyOrNull(value)) {
         return value.equals(valueToTest);
      } else if (value.isEmpty() && valueToTest.isEmpty()) {
         return true;
      }
      return false;
   }

   public static String requireNotEmptyAndNotNull(String stringValue) {
      if (isEmptyOrNull(stringValue)) {
         throw new IllegalArgumentException("Value must not be null nor empty!");
      }
      return stringValue;
   }

   /**
    * Concat all String in the given List into one single String
    *
    * @param stringLines
    *       the {@link String}s to concat
    * @return a String representing the given List of Strings
    */
   public static String concat2StringNl(List<String> stringLines) {
      return stringLines.stream()
              .reduce("", (prevLine, nextLine) -> appendStrings(prevLine, nextLine));
   }

   private static String appendStrings(String prevLine, String nextLine) {
      return isEmptyOrNull(nextLine) ? prevLine : prevLine + "\\n" + nextLine;
   }
}
