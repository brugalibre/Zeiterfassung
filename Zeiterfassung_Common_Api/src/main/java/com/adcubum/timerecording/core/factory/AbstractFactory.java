package com.adcubum.timerecording.core.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractFactory {

   private ApplicationContext context;

   protected AbstractFactory(String springXmlConfigFile) {
      this.context = new ClassPathXmlApplicationContext(springXmlConfigFile);
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
}
