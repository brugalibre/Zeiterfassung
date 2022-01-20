package com.adcubum.timerecording.messaging.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractFactory {

   private final ApplicationContext context;

   protected AbstractFactory(String springXmlConfigFile) {
      this.context = new ClassPathXmlApplicationContext(springXmlConfigFile);
   }

   @SuppressWarnings("unchecked")
   public <T> T createNew(String name) {
      try {
         return (T) context.getBean(name);
      } catch (BeansException e) {
         throw new IllegalStateException(e);
      }
   }
}
