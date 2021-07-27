package com.adcubum.timerecording.core.businessday.common.entity;

import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * The {@link BaseEntity} provides the most basic and necessary fields all entities must have
 * One of this fields is the id of a entity. The {@link BaseEntity} provides a auto generated UUID
 * 
 * @author DStalder
 *
 */
@MappedSuperclass
public abstract class BaseEntity implements IEntity<UUID> {

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   protected UUID id;

   public BaseEntity(UUID id) {
      this.id = id;
   }

   /**
    * @return the id of this {@link BaseEntity}
    */
   @Override
   public UUID getId() {
      return id;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + id.hashCode();
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      BaseEntity other = (BaseEntity) obj;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      return true;
   }
}
