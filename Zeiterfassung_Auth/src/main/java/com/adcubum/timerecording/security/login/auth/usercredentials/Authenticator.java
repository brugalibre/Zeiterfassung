package com.adcubum.timerecording.security.login.auth.usercredentials;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker annotation in order to identify possible {@link UserCredentialsAuthenticator}
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticator {

   String[] authenticatorNames() default "";
}
