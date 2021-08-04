package com.adcubum.timerecording.core.businessday.comeandgo.entity;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.adcubum.timerecording.core.businessday.common.entity.BaseEntity;
import com.adcubum.timerecording.core.businessday.entity.TimeSnippetEntity;

@Entity
@Table(name = "comeandgo")
public class ComeAndGoEntity extends BaseEntity {

   @ManyToOne
   @JoinColumn(name = "comeandgoes_id")
   private ComeAndGoesEntity comeAndGoesEntity;

   @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
   @JoinColumn(name = "timesnippet_id")
   private TimeSnippetEntity timeSnippetEntity;

   private boolean isRecorded;

   private ComeAndGoEntity() {
      super(null);
   }

   /**
    * Creates a new {@link ComeAndGoEntity}
    * 
    * @param id
    *        the id
    * @param comeAndGoesEntity
    *        the {@link ComeAndGoesEntity}
    * @param isRecorded
    *        <code>true</code> if this {@link ComeAndGoEntity} is already recorded or <code>false</code> if not
    */
   public ComeAndGoEntity(UUID id, ComeAndGoesEntity comeAndGoesEntity, boolean isRecorded) {
      super(id);
      this.comeAndGoesEntity = requireNonNull(comeAndGoesEntity);
      this.isRecorded = isRecorded;
   }

   public TimeSnippetEntity getTimeSnippetEntity() {
      return timeSnippetEntity;
   }

   @Override
   public String toString() {
      return String.format(
            "ComeAndGoEntity[id=%s, isRecorded='%s', timeSnippetEntity='%s']",
            id, isRecorded, timeSnippetEntity);
   }

   public boolean isRecorded() {
      return isRecorded;
   }

   /**
    * Defines this {@link ComeAndGoEntity} current {@link TimeSnippetEntity}
    * 
    * @param timeSnippetEntity
    *        the {@link TimeSnippetEntity} to set
    */
   public void setTimeSnippetEntity(TimeSnippetEntity timeSnippetEntity) {
      this.timeSnippetEntity = timeSnippetEntity;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((comeAndGoesEntity == null) ? 0 : comeAndGoesEntity.hashCode());
      result = prime * result + (isRecorded ? 1231 : 1237);
      result = prime * result + ((timeSnippetEntity == null) ? 0 : timeSnippetEntity.hashCode());
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
      ComeAndGoEntity other = (ComeAndGoEntity) obj;
      if (comeAndGoesEntity == null) {
         if (other.comeAndGoesEntity != null)
            return false;
      } else if (!comeAndGoesEntity.equals(other.comeAndGoesEntity))
         return false;
      if (isRecorded != other.isRecorded)
         return false;
      if (timeSnippetEntity == null) {
         if (other.timeSnippetEntity != null)
            return false;
      } else if (!timeSnippetEntity.equals(other.timeSnippetEntity))
         return false;
      return true;
   }
}
