package com.adcubum.timerecording.core.businessday.common.entity;

import java.sql.Timestamp;

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

   /**
    * @return the {@link Timestamp} when this {@link IEntity} was created
    */
   Timestamp getCreatedDate();

   /**
    * @return the {@link Timestamp} when this {@link IEntity} was modified the last time
    */
   Timestamp getLastModifiedDate();
}
