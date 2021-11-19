package com.adcubum.timerecording.core.businessday.entity;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoesEntity;
import com.adcubum.timerecording.core.businessday.common.entity.BaseEntity;

@Entity
@Table(name = "businessday")
public class BusinessDayEntity extends BaseEntity {

   /*
    * yes eager, since we access them in the maper, where the session is already closed.
    * Besides this should'nt be a performance issue, since there are not that much active increments.
    */
   @OneToMany(targetEntity = BusinessDayIncrementEntity.class,
         mappedBy = "businessDayEntity",
         cascade = CascadeType.ALL,
         fetch = FetchType.EAGER,
         orphanRemoval = true)
   @NonNull
   private List<BusinessDayIncrementEntity> businessDayIncrementEntities;

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "current_timesnippet_id")
   private TimeSnippetEntity currentTimeSnippetEntity;

   @OneToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "comeandgoes_id")
   private ComeAndGoesEntity comeAndGoesEntity;

   private boolean isBooked;

   public BusinessDayEntity() {
      this(null, false);
   }

   public BusinessDayEntity(boolean isBooked) {
      this(null, isBooked);
   }

   /**
    * Creates a new {@link BusinessDayEntity}
    * 
    * @param id
    *        the id
    */
   public BusinessDayEntity(UUID id, boolean isBooked) {
      super(id);
      this.isBooked = isBooked;
      this.currentTimeSnippetEntity = new TimeSnippetEntity(id, null, null);
      this.businessDayIncrementEntities = new ArrayList<>();
      this.comeAndGoesEntity = new ComeAndGoesEntity();
   }

   public TimeSnippetEntity getCurrentBusinessDayIncrementEntity() {
      return currentTimeSnippetEntity;
   }

   public List<BusinessDayIncrementEntity> getBusinessDayIncrementEntities() {
      return businessDayIncrementEntities;
   }

   public ComeAndGoesEntity getComeAndGoesEntity() {
      return comeAndGoesEntity;
   }

   public UUID getComeAndGoesEntityId() {
      return comeAndGoesEntity.getId();
   }

   public boolean isBooked() {
      return isBooked;
   }

   @Override
   public String toString() {
      String bdIncEntitiesRep = businessDayIncrementEntities.stream()
            .map(bDIncEntity -> String.format("BusinessDayIncrementEntity='%s'", bDIncEntity))
            .reduce("", (a, b) -> a + "," + b);
      return String.format(
            "BusinessDayEntity[id=%s, isBooked=%s, bdIncEntitiesRep='%s']",
            id, isBooked, bdIncEntitiesRep);
   }

   public void setBusinessDayIncrementEntities(List<BusinessDayIncrementEntity> businessDayIncrementEntities) {
      this.businessDayIncrementEntities = requireNonNull(businessDayIncrementEntities);
   }

   public void setCurrentTimeSnippetEntity(TimeSnippetEntity currentTimeSnippetEntity) {
      this.currentTimeSnippetEntity = requireNonNull(currentTimeSnippetEntity);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((businessDayIncrementEntities == null) ? 0 : businessDayIncrementEntities.hashCode());
      result = prime * result + ((comeAndGoesEntity == null) ? 0 : comeAndGoesEntity.hashCode());
      result = prime * result + ((currentTimeSnippetEntity == null) ? 0 : currentTimeSnippetEntity.hashCode());
      result = prime * result + (isBooked ? 1231 : 1237);
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      BusinessDayEntity other = (BusinessDayEntity) obj;
      if (!businessDayIncrementEntities.equals(other.businessDayIncrementEntities))
         return false;
      if (!comeAndGoesEntity.equals(other.comeAndGoesEntity))
         return false;
      if (!currentTimeSnippetEntity.equals(other.currentTimeSnippetEntity)) {
         return false;
      }
      return isBooked == other.isBooked;
   }

   public void setComeAndGoesEntity(ComeAndGoesEntity comeAndGoesEntity) {
      this.comeAndGoesEntity = /*requireNonNull(*/comeAndGoesEntity/*)*/;
   }

   public void setIsBooked(boolean isBooked) {
      this.isBooked = isBooked;
   }
}
