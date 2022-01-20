package com.adcubum.timerecording.messaging.util;

import com.adcubum.timerecording.messaging.api.receive.BookBusinessDayMessageReceiver;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReflectionUtil {

   public static <T> List<T> lookupClasses4AnnotationAndCreateInstances(String annotationName) {
      ClassInfoList classInfos = scanClasses4Annotation(annotationName);
      List<Class<T>> classesWithAnnotation = getCustomizedAuthenticatorClass(classInfos);
      return classesWithAnnotation.stream()
              .map(createInstance())
              .collect(Collectors.toList());
   }

   private static <T> Function<Class<? extends T>, T> createInstance() {
      return customizedAuthenticatorClass -> {
         try {
            customizedAuthenticatorClass.getDeclaredConstructor().setAccessible(true);
            return customizedAuthenticatorClass.getDeclaredConstructor().newInstance();
         } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
         }
      };
   }

   private static ClassInfoList scanClasses4Annotation(String annotationName) {
      ScanResult result = new ClassGraph().enableClassInfo()
              .enableAnnotationInfo()
              .enableClassInfo()
              .scan();
      return result.getClassesWithAnnotation(annotationName);
   }

   private static <T> List<Class<T>> getCustomizedAuthenticatorClass(ClassInfoList classInfos) {
      return classInfos.stream()
              .map(toClassName())
              .filter(BookBusinessDayMessageReceiver.class::isAssignableFrom)
              .map(aClass -> (Class<T>) aClass)
              .collect(Collectors.toList());
   }

   private static Function<ClassInfo, Class<?>> toClassName() {
      return classInfo -> {
         try {
            return Class.forName(classInfo.getName());
         } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
         }
      };
   }
}
