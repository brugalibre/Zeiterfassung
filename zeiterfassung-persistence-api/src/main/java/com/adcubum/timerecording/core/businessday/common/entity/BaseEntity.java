package com.adcubum.timerecording.core.businessday.common.entity;

import com.adcubum.timerecording.core.auditing.BaseAuditingEntityListener;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

import static java.util.Objects.isNull;

/**
 * The {@link BaseEntity} provides the most basic and necessary fields all entities must have
 * One of this fields is the id of a entity. The {@link BaseEntity} provides a auto generated UUID
 * 
 * @author DStalder
 *
 */
@MappedSuperclass
@EntityListeners(BaseAuditingEntityListener.class)
public abstract class BaseEntity implements IEntity<UUID> {

   @Id
   @Column(name = "id", updatable = false, nullable = false)
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   protected UUID id;

   @Column(updatable = false)
   private Timestamp createdDate;

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

   /**
    * Verifies if there are changes on either this {@link BaseEntity} compared to the persistentOther which leads the {@link BaseEntity#lastModifiedDate} to be updated
    *
    * @param persistentOther the persistentOther {@link BaseEntity} to check
    * @param <T>
    * @return <code>true</code> if there are any changes on this {@link BaseEntity} or <code>true</code> if not
    */
   public abstract  <T extends BaseEntity> boolean hasChangesForLastModified(T persistentOther);

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
