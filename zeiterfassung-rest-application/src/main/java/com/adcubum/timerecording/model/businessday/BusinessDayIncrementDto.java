/**
 * 
 */
package com.adcubum.timerecording.model.businessday;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.data.ticket.ticketactivity.factor.TicketActivityFactory;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.model.ticketbacklog.ServiceCodeDto;
import com.adcubum.timerecording.model.ticketbacklog.TicketDto;
import com.adcubum.util.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * The {@link BusinessDayIncrement} is used whenever a we need
 * {@link BusinessDayIncrement} for displaying or exporting. The {@link BusinessDayIncrement} is read only
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncrementDto {

   private UUID id;

   private String description;
   private ServiceCodeDto serviceCodeDto;

   private boolean isBooked;
   private boolean isDurationRepEditable;
   private boolean isServiceCodeEditable;
   private boolean isTicketNrEditable;
   private boolean isDescriptionEditable;
   private boolean isBeginTimeStampEditable;
   private boolean isEndTimeStampEditable;

   private TicketDto ticketDto;
   private TimeSnippetDto timeSnippetDto;

   public BusinessDayIncrementDto() {
      this.ticketDto = new TicketDto("", "", false, -1);
      this.timeSnippetDto = new TimeSnippetDto();
      this.serviceCodeDto = new ServiceCodeDto();
   }

   private BusinessDayIncrementDto(BusinessDayIncrement businessDayIncremental) {
      this.id = businessDayIncremental.getId();
      this.timeSnippetDto = new TimeSnippetDto(businessDayIncremental.getCurrentTimeSnippet());
      this.description = evalDescription(businessDayIncremental.getDescription());
      this.ticketDto = map2TicketDto(businessDayIncremental.getTicket());
      TicketActivity ticketActivity = businessDayIncremental.getTicketActivity();
      this.serviceCodeDto = new ServiceCodeDto(ticketActivity.getActivityCode(), ticketActivity.getActivityName());
      this.isBooked = businessDayIncremental.isBooked();
   }

   private static String evalDescription(String givenDescription) {
      String description;
      if (StringUtil.isEmptyOrNull(givenDescription)) {
         description = "                ";
      } else {
         description = givenDescription;
      }
      return description;
   }

   private static TicketDto map2TicketDto(Ticket ticket) {
      return new TicketDto(ticket.getNr(), ticket.getTicketRep(), ticket.isBookable(), ticket.getTicketAttrs().getProjectNr());
   }

   public final String getDescription() {
      return this.description;
   }

   public final ServiceCodeDto getServiceCodeDto() {
      return this.serviceCodeDto;
   }

   public boolean getIsBooked() {
      return isBooked;
   }

   public boolean isDurationRepEditable() {
      return isDurationRepEditable && !isBooked;
   }

   public boolean isTicketNrEditable() {
      return isTicketNrEditable && !isBooked;
   }

   public boolean isDescriptionEditable() {
      return isDescriptionEditable && !isBooked;
   }

   public boolean isBeginTimeStampEditable() {
      return isBeginTimeStampEditable && !isBooked;
   }

   public boolean isEndTimeStampEditable() {
      return isEndTimeStampEditable && !isBooked;
   }

   public boolean isServiceCodeEditable() {
      return isServiceCodeEditable && !isBooked;
   }

   public TimeSnippetDto getTimeSnippetDto() {
      return timeSnippetDto;
   }

   public TicketDto getTicketDto() {
      return ticketDto;
   }

   public UUID getId() {
      return id;
   }

   @Override
   public String toString() {
      return "BusinessDayIncrementDto [id=" + id + ", description=" + description + ", serviceCode=" + serviceCodeDto + ", isBooked=" + isBooked
            + ", ticketDto=" + ticketDto + ", timeSnippetDto=" + timeSnippetDto + "]";
   }

   /**
    * @param valueType
    *        the given {@link ValueTypes}
    * @return the value of this {@link BusinessDayIncrementDto} according to the given {@link ValueTypes} or <code>null</code> if there is
    *         no value for the given ValueTypes
    */
   public Object getChangedValue(ValueTypes valueType) {
      switch (valueType) {
         case TICKET_ACTIVITY:
            return TicketActivityFactory.INSTANCE.createNew(serviceCodeDto.getRepresentation(), serviceCodeDto.getValue());
         case DESCRIPTION:
            return description;
         default:
            break;
      }
      String changedValue = ticketDto.getChangedValue(valueType);
      if (nonNull(changedValue)) {
         return changedValue;
      }
      return timeSnippetDto.getChangedValue(valueType);
   }

   /**
    * Compares both {@link BusinessDayIncrementDto} and returns a list of {@link ValueTypes} which has changed
    * 
    * @param otherBusinessDayIncrementDto
    */
   public List<ValueTypes> compareAndEvalChangedValues(BusinessDayIncrementDto otherBusinessDayIncrementDto) {
      List<ValueTypes> changedAttrs = new ArrayList<>();
      if (serviceCodeDto.getValue() != otherBusinessDayIncrementDto.serviceCodeDto.getValue()) {
         changedAttrs.add(ValueTypes.TICKET_ACTIVITY);
      }
      if (!description.equals(otherBusinessDayIncrementDto.description)) {
         changedAttrs.add(ValueTypes.DESCRIPTION);
      }
      changedAttrs.addAll(ticketDto.compareAndEvalChangedValues(otherBusinessDayIncrementDto.getTicketDto()));
      changedAttrs.addAll(timeSnippetDto.compareAndEvalChangedValues(otherBusinessDayIncrementDto.getTimeSnippetDto()));
      return changedAttrs;
   }

   /**
    * Returns a new {@link BusinessDayIncrementDto} for the given
    * {@link BusinessDayIncrement}
    * 
    * @param businessDayIncrement
    * @return a new {@link BusinessDayIncrementDto} for the given
    *         {@link BusinessDayIncrement}
    */
   public static BusinessDayIncrementDto of(BusinessDayIncrement businessDayIncrement) {
      requireNonNull(businessDayIncrement);
      requireNonNull(businessDayIncrement.getTicket());
      return new BusinessDayIncrementDto(businessDayIncrement);
   }

   public static BusinessDayIncrementDto of(TimeSnippet currentTimeSnippet, Ticket ticket) {
      BusinessDayIncrementDto businessDayIncrementDto = new BusinessDayIncrementDto();
      businessDayIncrementDto.ticketDto = map2TicketDto(ticket);
      businessDayIncrementDto.timeSnippetDto = new TimeSnippetDto(currentTimeSnippet);
      businessDayIncrementDto.serviceCodeDto = new ServiceCodeDto();
      businessDayIncrementDto.description = "";
      return businessDayIncrementDto;
   }
}
