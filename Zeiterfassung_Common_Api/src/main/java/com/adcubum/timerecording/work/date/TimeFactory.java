package com.adcubum.timerecording.work.date;

import java.util.Date;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.settings.round.RoundMode;

/**
 * The {@link TimeFactory} used in order to create {@link Time} instances
 * 
 * @author Dominic
 *
 */
public class TimeFactory extends AbstractFactory {

   private static final String BEAN_NAME = "time";
   private static final TimeFactory INSTANCE = new TimeFactory();

   private TimeFactory() {
      super("spring.xml");
   }

   /**
    * @return a new {@link Time} instance using it's default constructor
    */
   public static Time createNew() {
      return INSTANCE.createNew(BEAN_NAME);
   }

   /**
    * Creates a new {@link Time} instance for the given {@link Date}
    * 
    * @param date
    *        the {@link Date} value
    * @return a new {@link Time} instance for the given {@link Date}
    */
   public static Time createNew(Date date) {
      return createNewWithAgruments(date);
   }

   /**
    * Creates a new {@link Time} instance for the given {@link Time}
    * 
    * @param date
    *        the {@link Time} value
    * @return a new {@link Time} instance for the given {@link Time}
    */
   public static Time createNew(Time time) {
      return createNewWithAgruments(time);
   }

   /**
    * Creates a new {@link Time} instance for the given long value
    * 
    * @param timeValue
    *        the long value representing the actual time of the instance to create
    * @return a new {@link Time} instance for the given long value
    */
   public static Time createNew(long timeValue) {
      return createNewWithAgruments(timeValue);
   }

   /**
    * Creates a new {@link Time} instance for the given long value
    * 
    * @param time
    *        the long value representing the actual time of the instance to create
    * @param roundMode
    *        the {@link RoundMode} to use
    * 
    * @return a new {@link Time} instance for the given long value
    */
   public static Time createNew(long time, RoundMode roundMode) {
      return createNewWithAgruments(time, roundMode);
   }

   private static Time createNewWithAgruments(Object... args) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, args);
   }
}
