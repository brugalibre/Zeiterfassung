package com.adcubum.timerecording.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class TimeRecordingRestApplication {

   public static void main(String[] initialArgs) {
      String[] args = addSourceClassName2TheArguments(initialArgs);
      Application.launch(TimeRecordingApplication.class, args);
   }

   private static String[] addSourceClassName2TheArguments(String[] initialArgs) {
      String[] args = new String[initialArgs.length + 1];
      System.arraycopy(initialArgs, 0, args, 0, initialArgs.length);
      args[args.length - 1] = TimeRecordingRestApplication.class.getName();
      return args;
   }
}
