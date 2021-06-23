package com.adcubum.timerecording.settings;

import java.io.IOException;

public class SettingsInitException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public SettingsInitException(IOException e) {
      super(e);
   }

   public SettingsInitException(String msg) {
      super(msg);
   }

}
