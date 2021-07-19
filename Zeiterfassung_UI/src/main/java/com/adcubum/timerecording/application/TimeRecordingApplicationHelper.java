package com.adcubum.timerecording.application;

/**
 * Contains utility methods for the {@link TimeRecordingApplication}
 * 
 * @author DStalder
 *
 */
public class TimeRecordingApplicationHelper {

   private TimeRecordingApplicationHelper() {
      // private
   }

   /**
    * Tries to retrieve the spring-boot source class which initially started the {@link TimeRecordingApplication} from the given arguments
    * 
    * @param args
    *        the program arguments which must contains the source class
    * @return the spring-boot source class
    * @throws ApplicationLaunchException
    *         if there is no spring-boot source class
    */
   public static Class<?> getSourceClass(String[] args) {
      try {
         if (args.length > 0) {
            return Class.forName(args[args.length - 1]);
         }
         throw new ApplicationLaunchException(
               "No source class provided! Make sure that the last programm argument is the originally spring-boot-class");
      } catch (ClassNotFoundException e) {
         throw new ApplicationLaunchException(e);
      }
   }
}
