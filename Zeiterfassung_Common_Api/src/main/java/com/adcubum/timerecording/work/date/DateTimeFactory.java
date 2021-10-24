package com.adcubum.timerecording.work.date;

import java.util.Date;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.settings.round.RoundMode;

/**
 * The {@link DateTimeFactory} used in order to create {@link DateTime} instances
 * 
 * @author Dominic
 *
 */
public class DateTimeFactory extends AbstractFactory {

   private static final String BEAN_NAME = "datetime";
   private static final DateTimeFactory INSTANCE = new DateTimeFactory();

   private DateTimeFactory() {
      super("spring.xml");
   }

   /**
    * @return a new {@link DateTime} instance using it's default constructor
    */
   public static DateTime createNew() {
      return INSTANCE.createNew(BEAN_NAME);
   }

   /**
    * Creates a new {@link DateTime} instance for the given {@link Date}
    * 
    * @param date
    *        the {@link Date} value
    * @return a new {@link DateTime} instance for the given {@link Date}
    */
   public static DateTime createNew(Date date) {
      return createNewWithAgruments(date);
   }

   /**
    * Creates a new {@link DateTime} instance for the given {@link DateTime}
    * 
    * @param date
    *        the {@link DateTime} value
    * @return a new {@link DateTime} instance for the given {@link DateTime}
    */
   public static DateTime createNew(DateTime time) {
      return createNewWithAgruments(time);
   }

   /**
    * Creates a new {@link DateTime} instance for the given long value
    * 
    * @param timeValue
    *        the long value representing the actual time of the instance to create
    * @return a new {@link DateTime} instance for the given long value
    */
   public static DateTime createNew(long timeValue) {
      return createNewWithAgruments(timeValue);
   }

   /**
    * Creates a new {@link DateTime} instance for the given long value
    * 
    * @param time
    *        the long value representing the actual time of the instance to create
    * @param roundMode
    *        the {@link RoundMode} to use
    * 
    * @return a new {@link DateTime} instance for the given long value
    */
   public static DateTime createNew(long time, RoundMode roundMode) {
      return createNewWithAgruments(time, roundMode);
   }

   private static DateTime createNewWithAgruments(Object... args) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, args);
   }
}
