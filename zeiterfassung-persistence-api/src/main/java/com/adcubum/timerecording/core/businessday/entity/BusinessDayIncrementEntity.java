package com.adcubum.timerecording.core.businessday.entity;

import com.adcubum.timerecording.core.businessday.common.entity.BaseEntity;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

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

   @NonNull
   private Integer serviceCode;

   @NonNull
   private boolean isBooked;

   @NonNull
   private boolean isSent;

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
         boolean isBooked, boolean isSent) {
      super(id);
      this.businessDayEntity = businessDayEntity;
      this.description = description;
      this.ticketNr = ticketNr;
      this.serviceCode = chargeType;
      this.isBooked = isBooked;
      this.isSent = isSent;
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

   public Integer getServiceCode() {
      return serviceCode;
   }

   public boolean isBooked() {
      return isBooked;
   }

   public boolean isSent() {
      return isSent;
   }

   @Override
   public String toString() {
      return String.format(
            "BusinessDayIncrementEntity[id=%s, " +
                    "description='%s'," +
                    " ticketNr='%s'," +
                    " chargeType='%s'," +
                    " isBooked='%s'," +
                    " isSent='%s'," +
                    " currentTimeSnippetEntity='%s']",
            id, description, ticketNr, serviceCode, isBooked, isSent, currentTimeSnippetEntity);
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), businessDayEntity, currentTimeSnippetEntity, description, ticketNr, serviceCode, isBooked, isSent);
   }

   /**
    * Defines this {@link BusinessDayIncrementEntity} current {@link TimeSnippetEntity}
    * 
    * @param currentTimeSnippetEntity
    *        the {@link TimeSnippetEntity} to set
    */
   public void setCurrentTimeSnippetEntity(TimeSnippetEntity currentTimeSnippetEntity) {
      this.currentTimeSnippetEntity = currentTimeSnippetEntity;
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
      if (serviceCode == null) {
         if (other.serviceCode != null)
            return false;
      } else if (!serviceCode.equals(other.serviceCode))
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
