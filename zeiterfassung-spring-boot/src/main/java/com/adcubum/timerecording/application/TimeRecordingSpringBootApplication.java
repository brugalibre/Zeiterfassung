package com.adcubum.timerecording.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class TimeRecordingSpringBootApplication {

   public static void main(String[] initialArgs) {
      initSpringBootApp();
      String[] args = addSourceClassName2TheArguments(initialArgs);
      Application.launch(TimeRecordingApplication.class, args);
   }

   private static String[] addSourceClassName2TheArguments(String[] initialArgs) {
      String[] args = new String[initialArgs.length + 1];
      System.arraycopy(initialArgs, 0, args, 0, initialArgs.length);
      args[args.length - 1] = TimeRecordingSpringBootApplication.class.getName();
      return args;
   }

   private static void initSpringBootApp() {
      // Spring is headless by default. Since we still have our fx-application, active ui mode
      System.setProperty("java.awt.headless", Boolean.FALSE.toString());
   }
}
