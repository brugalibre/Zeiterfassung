/**
 * 
 */
package com.myownb3.dominic.timerecording.work.date;

/**
 * This class is a wrapper, in order to wrap the java.util.Date within this {@link Date} class
 * @author Dominic
 *
 */
public class Date extends java.util.Date
{

   /**
    * Creates an instance of {@link Date} with a time-stamp exactly when it is created
    */
   public Date ()
   {
      super ();
   }
   
   /**
    * Creates an instance of {@link Date} which time-stamp is the given long value
    * @param time
    */
   public Date (long time)
   {
      super (time);
   }

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   public static Date parseDate (String string)
   {
      @SuppressWarnings("deprecation")
      long time = java.sql.Date.parse (string);
      Date date = new Date (time);
      return date;
   }
}
