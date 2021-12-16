package com.adcubum.util.utils;

import java.util.Collection;
import java.util.stream.Collectors;

public class LogUtil {

   private LogUtil() {
      // privé
   }

   public static String toLogString(Collection<? extends Object> objects){
      return objects.stream()
              .map(Object::toString)
              .collect(Collectors.collectingAndThen(Collectors.toList(), StringUtil::concat2StringNl));
   }
}
