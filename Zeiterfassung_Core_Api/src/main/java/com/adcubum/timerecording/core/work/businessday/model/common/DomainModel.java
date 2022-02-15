package com.adcubum.timerecording.core.work.businessday.model.common;

import java.util.UUID;

/**
 * The {@link DomainModel} defines a domain model which contains not only data but also logic
 *
 * @author DStalder
 */
public interface DomainModel {

   /**
    * @return the id of this {@link DomainModel}
    */
   UUID getId();
}
