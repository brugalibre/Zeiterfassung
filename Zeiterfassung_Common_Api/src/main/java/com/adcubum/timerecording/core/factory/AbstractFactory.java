package com.adcubum.timerecording.core.factory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractFactory {

   private static Map<String, ApplicationContext> CONTEXT_MAP = new HashMap<>();
   private ApplicationContext context;

   protected AbstractFactory(String springXmlConfigFile) {
      if (CONTEXT_MAP.containsKey(springXmlConfigFile)) {
         this.context = CONTEXT_MAP.get(springXmlConfigFile);
      } else {
         this.context = new ClassPathXmlApplicationContext(springXmlConfigFile);
         CONTEXT_MAP.put(springXmlConfigFile, context);
      }
   }

   @SuppressWarnings("unchecked")
   public <T> T createNewWithAgruments(String name, Object... args) {
      try {
         return (T) context.getBean(name, args);
      } catch (BeansException e) {
         throw new ObjectCreateException(e);
      }
   }

   @SuppressWarnings("unchecked")
   public <T> T createNew(String name) {
      try {
         return (T) context.getBean(name);
      } catch (BeansException e) {
         throw new ObjectCreateException(e);
      }
   }

   public void setContext(ApplicationContext applicationContext) {
      this.context = applicationContext;
   }
}
