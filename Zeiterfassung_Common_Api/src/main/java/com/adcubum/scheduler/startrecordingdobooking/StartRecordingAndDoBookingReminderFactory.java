package com.adcubum.scheduler.startrecordingdobooking;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * The {@link StartRecordingAndDoBookingReminderFactory} is used in order to create a {@link StartRecordingAndDoBookingReminder}
 * 
 * @author Dominic
 *
 */
public class StartRecordingAndDoBookingReminderFactory extends AbstractFactory {
   private static final StartRecordingAndDoBookingReminderFactory INSTANCE = new StartRecordingAndDoBookingReminderFactory();
   private static final String REMINDER_BEAN_NAME = "reminder";

   private StartRecordingAndDoBookingReminderFactory() {
      super("spring.xml");
   }

   /**
    * Creates a new {@link StartRecordingAndDoBookingReminder}
    * 
    * @param args
    *        the arguments
    * @return a new {@link StartRecordingAndDoBookingReminder}
    */
   public static StartRecordingAndDoBookingReminder createNew(Object... args) {
      return INSTANCE.createNewWithAgruments(REMINDER_BEAN_NAME, args);
   }
}
