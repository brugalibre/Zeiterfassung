package com.adcubum.timerecording.core.businessday.entity;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
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
   @JoinColumn(name = "current_businessday_increment_id")
   @NonNull
   private BusinessDayIncrementEntity currentBusinessDayIncrementEntity;

   @OneToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "comeandgoes_id")
   private ComeAndGoesEntity comeAndGoesEntity;

   public BusinessDayEntity() {
      this(null);
   }

   /**
    * Creates a new {@link BusinessDayEntity}
    * 
    * @param id
    *        the id
    */
   public BusinessDayEntity(UUID id) {
      super(id);
      this.currentBusinessDayIncrementEntity = new BusinessDayIncrementEntity(null, null, null, null, 0, false);
      this.businessDayIncrementEntities = Collections.emptyList();
      this.comeAndGoesEntity = new ComeAndGoesEntity();
   }

   public BusinessDayIncrementEntity getCurrentBusinessDayIncrementEntity() {
      return currentBusinessDayIncrementEntity;
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

   @Override
   public String toString() {
      String bdIncEntitiesRep = businessDayIncrementEntities.stream()
            .map(bDIncEntity -> String.format("BusinessDayIncrementEntity='%s'", bDIncEntity))
            .reduce("", (a, b) -> a + "," + b);
      return String.format(
            "BusinessDayEntity[id=%s, bdIncEntitiesRep='%s']",
            id, bdIncEntitiesRep);
   }

   public void setBusinessDayIncrementEntities(List<BusinessDayIncrementEntity> businessDayIncrementEntities) {
      this.businessDayIncrementEntities = requireNonNull(businessDayIncrementEntities);
   }

   public void setCurrentBusinessDayIncrementEntity(BusinessDayIncrementEntity currentBusinessDayIncrementEntity) {
      this.currentBusinessDayIncrementEntity = requireNonNull(currentBusinessDayIncrementEntity);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + businessDayIncrementEntities.hashCode();
      result = prime * result + comeAndGoesEntity.hashCode();
      result = prime * result + currentBusinessDayIncrementEntity.hashCode();
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
      return !currentBusinessDayIncrementEntity.equals(other.currentBusinessDayIncrementEntity);
   }

   public void setComeAndGoesEntity(ComeAndGoesEntity comeAndGoesEntity) {
      this.comeAndGoesEntity = requireNonNull(comeAndGoesEntity);
   }
}
