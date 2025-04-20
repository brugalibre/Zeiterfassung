package com.adcubum.timerecording.settings.key;

/**
 * A {@link ValueKey} is use to map any kind of values to a specific key. The key defines a String value which represents the name of the
 * key as well as
 * a class, which defines the type of the value which is stored with a Key
 *
 * @author DStalder
 * @param <T>
 *
 */
public interface ValueKey<T> {
   /**
    * Return the type of the value this key is mapping
    *
    * @return the type of the value this key is mapping
    */
   Class<T> getType();

   /**
    * Return the (internal) name of this key
    *
    * @return the (internal) name of this key
    */
    String getName();

   /**
    * @return default value of this {@link ValueKey}
    */
   T getDefault();

   /**
    * Return the name of the resourceName, which contains the stored value
    *
    * @return the name of the resourceName, which contains the stored value
    */
    String getResourceName();
}
