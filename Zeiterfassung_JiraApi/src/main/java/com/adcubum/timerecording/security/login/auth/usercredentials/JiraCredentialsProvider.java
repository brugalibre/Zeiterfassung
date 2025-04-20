package com.adcubum.timerecording.security.login.auth.usercredentials;

import com.adcubum.timerecording.security.login.auth.init.UserAuthenticatedObservable;

/**
 * The {@link JiraCredentialsProvider} is responsible for retrieving the credentials, required for any authenticated
 * interaction with jira
 */
public interface JiraCredentialsProvider extends UserAuthenticatedObservable {
   String getCredentials();
}
