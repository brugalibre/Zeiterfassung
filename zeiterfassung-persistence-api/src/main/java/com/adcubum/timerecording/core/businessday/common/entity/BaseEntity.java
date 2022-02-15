package com.adcubum.timerecording.core.businessday.common.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * The {@link BaseEntity} provides the most basic and necessary fields all entities must have
 * One of this fields is the id of a entity. The {@link BaseEntity} provides a auto generated UUID
 * 
 * @author DStalder
 *
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements IEntity<UUID> {

   @Id
   @Column(name = "id", updatable = false, nullable = false)
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   protected UUID id;

   @Column(updatable = false)
   @CreatedDate
   private Timestamp createdDate;

   @LastModifiedDate
   private Timestamp lastModifiedDate;

   public BaseEntity(UUID id) {
      this.id = id;
   }

   @Override
   public UUID getId() {
      return id;
   }

   @Override
   public Timestamp getCreatedDate() {
      return createdDate;
   }

   @Override
   public Timestamp getLastModifiedDate() {
      return lastModifiedDate;
   }

   public void setCreatedDate(Timestamp createdDate) {
      this.createdDate = createdDate;
   }

   public void setLastModifiedDate(Timestamp lastModifiedDate) {
      this.lastModifiedDate = lastModifiedDate;
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
