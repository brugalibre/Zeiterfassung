/**
 * 
 */
package com.adcubum.util.utils;

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
}
