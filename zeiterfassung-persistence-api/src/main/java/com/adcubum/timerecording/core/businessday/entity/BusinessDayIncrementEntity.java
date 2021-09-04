package com.adcubum.timerecording.core.businessday.entity;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import com.adcubum.timerecording.core.businessday.common.entity.BaseEntity;

@Entity
@Table(name = "businessday_increment")
public class BusinessDayIncrementEntity extends BaseEntity {

   @ManyToOne
   @JoinColumn(name = "businessday_id", nullable = true)
   private BusinessDayEntity businessDayEntity;

   @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
   @JoinColumn(name = "current_timesnippet_id")
   private TimeSnippetEntity currentTimeSnippetEntity;

   private String description;
   @NonNull
   private String ticketNr;
   private Integer chargeType;
   private boolean isBooked;

   private BusinessDayIncrementEntity() {
      // private constructor for JPA
      super(null);
   }

   /**
    * Creates a new {@link BusinessDayIncrementEntity}
    * 
    * @param id
    *        the id
    * @param businessDayEntity
    *        the {@link BusinessDayEntity}
    * @param description
    *        the description of the {@link BusinessDayIncrementEntity}
    * @param ticketNr
    *        the ticket nr
    * @param chargeType
    *        the charge type
    * @param isBooked
    *        defines if this {@link BusinessDayIncrementEntity} is already isBooked
    */
   public BusinessDayIncrementEntity(UUID id, BusinessDayEntity businessDayEntity, String description, String ticketNr, Integer chargeType,
         boolean isBooked) {
      super(id);
      this.businessDayEntity = businessDayEntity;
      this.description = description;
      this.ticketNr = ticketNr;
      this.chargeType = chargeType;
      this.isBooked = isBooked;
   }

   public TimeSnippetEntity getCurrentTimeSnippetEntity() {
      return currentTimeSnippetEntity;
   }

   public String getDescription() {
      return description;
   }

   public String getTicketNr() {
      return ticketNr;
   }

   public Integer getChargeType() {
      return chargeType;
   }

   public boolean isBooked() {
      return isBooked;
   }

   @Override
   public String toString() {
      return String.format(
            "BusinessDayIncrementEntity[id=%s, description='%s', ticketNr='%s', chargeType='%s', isBooked='%s', currentTimeSnippetEntity='%s']",
            id, description, ticketNr, chargeType, isBooked, currentTimeSnippetEntity);
   }

   /**
    * Defines this {@link BusinessDayIncrementEntity} current {@link TimeSnippetEntity}
    * 
    * @param timeSnippetEntity
    *        the {@link TimeSnippetEntity} to set
    */
   public void setCurrentTimeSnippetEntity(TimeSnippetEntity currentTimeSnippetEntity) {
      this.currentTimeSnippetEntity = currentTimeSnippetEntity;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((businessDayEntity == null) ? 0 : businessDayEntity.hashCode());
      result = prime * result + ((chargeType == null) ? 0 : chargeType.hashCode());
      result = prime * result + ((currentTimeSnippetEntity == null) ? 0 : currentTimeSnippetEntity.hashCode());
      result = prime * result + ((description == null) ? 0 : description.hashCode());
      result = prime * result + (isBooked ? 1231 : 1237);
      result = prime * result + ((ticketNr == null) ? 0 : ticketNr.hashCode());
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
      BusinessDayIncrementEntity other = (BusinessDayIncrementEntity) obj;
      if (businessDayEntity == null) {
         if (other.businessDayEntity != null)
            return false;
      } else if (!businessDayEntity.equals(other.businessDayEntity))
         return false;
      if (chargeType == null) {
         if (other.chargeType != null)
            return false;
      } else if (!chargeType.equals(other.chargeType))
         return false;
      if (currentTimeSnippetEntity == null) {
         if (other.currentTimeSnippetEntity != null)
            return false;
      } else if (!currentTimeSnippetEntity.equals(other.currentTimeSnippetEntity))
         return false;
      if (description == null) {
         if (other.description != null)
            return false;
      } else if (!description.equals(other.description))
         return false;
      if (isBooked != other.isBooked)
         return false;
      if (ticketNr == null) {
         if (other.ticketNr != null)
            return false;
      } else if (!ticketNr.equals(other.ticketNr))
         return false;
      return true;
   }
}
