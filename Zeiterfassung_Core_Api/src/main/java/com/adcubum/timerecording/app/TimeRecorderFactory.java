package com.adcubum.timerecording.app;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * Factory used to create and instantiate new {@link TimeRecorder} instances
 * 
 * @author DStalder
 *
 */
public class TimeRecorderFactory extends AbstractFactory {
   private static final String BEAN_NAME = "timerecorder";
   private static final TimeRecorderFactory INSTANCE = new TimeRecorderFactory();

   private TimeRecorderFactory() {
      super("modul-configration.xml");
   }

   /**
    * Creates a new Instance of the {@link TimeRecorder} or returns an already created instance
    * 
    * @return a new Instance of the {@link TimeRecorder} or returns an already created instance
    */
   public static TimeRecorder createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }
}
