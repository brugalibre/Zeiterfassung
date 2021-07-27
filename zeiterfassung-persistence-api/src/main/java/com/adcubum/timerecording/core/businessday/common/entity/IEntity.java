package com.adcubum.timerecording.core.businessday.common.entity;

/**
 * Super interface for all kind of entities
 * 
 * @author DStalder
 *
 */
public interface IEntity<T> {

   /**
    * @return the id of this {@link IEntity}
    */
   T getId();
}
