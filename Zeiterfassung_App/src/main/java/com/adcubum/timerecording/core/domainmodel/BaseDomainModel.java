package com.adcubum.timerecording.core.domainmodel;

import com.adcubum.timerecording.core.work.businessday.model.common.DomainModel;

import java.time.LocalDate;
import java.util.UUID;

public abstract class BaseDomainModel implements DomainModel {
   protected UUID id;

   public BaseDomainModel(UUID id) {
      this.id = id;
   }

   @Override
   public String toString() {
      return "id=" + id ;
   }

   @Override
   public UUID getId() {
      return id;
   }
}
