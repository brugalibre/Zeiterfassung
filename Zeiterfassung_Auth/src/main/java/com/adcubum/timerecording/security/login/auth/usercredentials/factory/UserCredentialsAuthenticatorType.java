package com.adcubum.timerecording.security.login.auth.usercredentials.factory;

import com.adcubum.timerecording.security.login.auth.usercredentials.Authenticator;
import com.adcubum.timerecording.security.login.auth.usercredentials.UserCredentialsAuthenticator;
import io.github.classgraph.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static java.util.Objects.requireNonNull;

public enum UserCredentialsAuthenticatorType {

   /**
    * A authenticator based on jira
    */
   ADC_JIRA_WEB("ADC_JIRA_WEB"),

   /**
    * A authenticator based on jira
    */
   JIRA_API("JIRA_API"),

   /**
    * A authenticator based on proles
    */
   PROLES_WEB("PROLES_WEB");

   private static final Logger LOG = LoggerFactory.getLogger(UserCredentialsAuthenticatorType.class);
   private final String authenticatorName;

   UserCredentialsAuthenticatorType(String authenticatorName) {
      this.authenticatorName = requireNonNull(authenticatorName);
   }

   /**
    * Creates a new {@link UserCredentialsAuthenticator} for this specific type
    *
    * @return a new {@link UserCredentialsAuthenticator} for this specific type
    */
   public UserCredentialsAuthenticator createUserCredentialAuthenticator() {

      ClassInfoList classInfos = scanClasses4Annotation();
      try {
         Class<? extends UserCredentialsAuthenticator> customizedAuthenticatorClass = getCustomizedAuthenticatorClass(authenticatorName, classInfos);
         return customizedAuthenticatorClass.getDeclaredConstructor().newInstance();
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
         throw new IllegalStateException("No UserCredentialsAuthenticator registered for name '" + authenticatorName + "'", e);
      }
   }

   private static ClassInfoList scanClasses4Annotation() {
      ScanResult result = new ClassGraph().enableClassInfo()
              .enableAnnotationInfo()
              .enableClassInfo()
              .scan();
      return result.getClassesWithAnnotation(Authenticator.class.getName());
   }

   private static Class<? extends UserCredentialsAuthenticator> getCustomizedAuthenticatorClass(String authenticatorName, ClassInfoList classInfos) throws ClassNotFoundException {
      for (ClassInfo classInfo : classInfos) {
         for (AnnotationParameterValue parameterValue : classInfo.getAnnotationInfo(Authenticator.class.getName()).getParameterValues()) {
            String[] values = (String[]) parameterValue.getValue();
            if (parameterValue.getName().equals("authenticatorNames") && Arrays.asList(values).contains(authenticatorName)) {
               return (Class<? extends UserCredentialsAuthenticator>) Class.forName(classInfo.getName());
            }
         }
      }
      throw new NoSuchElementException("No UserCredentialsAuthenticator registered for name '" + authenticatorName + "'");
   }

   public static UserCredentialsAuthenticatorType getForName(String ticketSystemName) {
      for (UserCredentialsAuthenticatorType userCredentialsAuthenticatorType : UserCredentialsAuthenticatorType.values()) {
         if (userCredentialsAuthenticatorType.authenticatorName.equals(ticketSystemName)) {
            return userCredentialsAuthenticatorType;
         }
      }
      LOG.warn("No UserCredentialsAuthenticatorType found for name '{}'. Fallback to the default '{}'", ticketSystemName, UserCredentialsAuthenticatorType.ADC_JIRA_WEB.authenticatorName);
      // Fallback due to backwards compatibility
      return UserCredentialsAuthenticatorType.ADC_JIRA_WEB;
   }
}
