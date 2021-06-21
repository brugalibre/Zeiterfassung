package com.adcubum.timerecording.core.book.abacus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultServiceCodes {

   private DefaultServiceCodes() {
      // private 
   }

   private static final Map<Integer, String> DEFAULT_SERVICE_CODES = createDefaultServiceCodes();

   private static Map<Integer, String> createDefaultServiceCodes() {
      Map<Integer, String> defaultServiceCodes = new HashMap<>();
      defaultServiceCodes.put(100, "100 - Analyse");
      defaultServiceCodes.put(111, "111 - Meeting");
      defaultServiceCodes.put(113, "113 - Umsetzung/Dokumentation");
      defaultServiceCodes.put(122, "122 - Qualtitätssicherung");
      defaultServiceCodes.put(140, "140 - Allg. Verwaltungsarbeiten");
      return defaultServiceCodes;
   }

   /**
    * @return the default service codes
    */
   public static Map<Integer, String> getDefaultServiceCodes() {
      return Collections.unmodifiableMap(DEFAULT_SERVICE_CODES);
   }
}
