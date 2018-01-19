/**
 * 
 */
package com.myownb3.dominic.timerecording.work.date;

import java.util.HashMap;
import java.util.Map;

import com.myownb3.dominic.librarys.text.res.TextLabel;

/**
 * Defines what a specific type of time is, eg. a day, a minute and so on.
 * This is used to define in which format the total amount of time is calculated
 * @author Dominic
 */
public class TimeType
{
   public enum TIME_TYPE
   {
      HOUR,
      MIN,
      SEC,
   }
   
   public static final Map<TIME_TYPE, String> timeTypeValueUnitMap = createMap ();

   /**
    * @return
    */
   private static Map<TIME_TYPE, String> createMap ()
   {
      Map<TIME_TYPE, String> map = new HashMap<TIME_TYPE, String> ();
      
      map.put (TIME_TYPE.HOUR, TextLabel.HOUR);
      map.put (TIME_TYPE.MIN, TextLabel.MINUTES);
      map.put (TIME_TYPE.SEC, TextLabel.SECONDS);
      
      return map;
   }
}
