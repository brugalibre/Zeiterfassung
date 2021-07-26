package com.adcubum.timerecording.security.login.auth.configuration;

/**
 * A {@link Configuration} stores the configuration used by the authentication classes
 * 
 * @author dstalder
 *
 */
public interface Configuration {

   /**
    * Returns the value stored for the given key
    * 
    * @param key
    *        the key
    * @return the value
    */
   String getValue(String key);

}
