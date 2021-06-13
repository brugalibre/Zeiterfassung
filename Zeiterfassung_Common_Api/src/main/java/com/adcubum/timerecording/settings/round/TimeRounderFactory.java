package com.adcubum.timerecording.settings.round;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.settings.Settings;

/**
 * The {@link TimeRounderFactory} used in order to create a {@link TimeRounder}
 * 
 * @author Dominic
 *
 */
public class TimeRounderFactory extends AbstractFactory {
   private static final String BEAN_NAME = "timerounder";
   private static final TimeRounderFactory INSTANCE = new TimeRounderFactory();

   private TimeRounderFactory() {
      super("spring.xml");
   }

   /**
    * Creates a new {@link TimeRounder}
    * 
    * @param settings
    *        the {@link Settings} this {@link TimeRounder} uses
    * @return a new created or a already instantiated {@link TimeRounder} instance
    */
   public static TimeRounder createNew(Settings settings) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, settings);
   }
}
