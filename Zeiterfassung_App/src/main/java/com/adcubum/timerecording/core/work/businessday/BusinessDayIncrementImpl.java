/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.importexport.in.businessday.BusinessDayIncrementImport;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;
import com.adcubum.timerecording.work.date.TimeType;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;
import com.adcubum.util.parser.NumberFormat;

/**
 * A {@link BusinessDayImpl} consist of one or more {@link BusinessDayIncrement}.
 * Where as a {@link BusinessDayIncrement} consist of one or more
 * {@link TimeSnippet}. Two different {@link BusinessDayIncrement} are <i> not
 * </i> dependent!
 * 
 * @author Dominic
 */
public class BusinessDayIncrementImpl implements BusinessDayIncrement {
   private TimeSnippet currentTimeSnippet;

   private UUID id;
   private String description;
   private Ticket ticket;
   private int chargeType;
   private boolean isCharged;

   /**
    * Constructor used by the factory
    */
   @SuppressWarnings("unused")
   private BusinessDayIncrementImpl(TimeSnippet currentTimeSnippet, UUID id, String description, Ticket ticket, int chargeType, boolean isCharged) {
      this.currentTimeSnippet = currentTimeSnippet;
      this.id = id;
      this.description = description;
      this.ticket = ticket;
      this.chargeType = chargeType;
      this.isCharged = isCharged;
   }

   /**
    * Creates a new and empty {@link BusinessDayIncrement}
    * 
    */
   public BusinessDayIncrementImpl() {
      // nothing to do
   }

   /**
    * @param beginTimeStamp
    */
   @Override
   public void startCurrentTimeSnippet(Time beginTimeStamp) {
      currentTimeSnippet = TimeSnippetFactory.createNew();
      currentTimeSnippet.setBeginTimeStamp(beginTimeStamp);
   }

   /**
    * @param endTimeStamp
    */
   @Override
   public void stopCurrentTimeSnippet(Time endTimeStamp) {
      currentTimeSnippet.setEndTimeStamp(endTimeStamp);
   }

   @Override
   public void resumeLastTimeSnippet() {
      currentTimeSnippet.setEndTimeStamp(null);
   }

   @Override
   public Date getDate() {
      return isNull(currentTimeSnippet) ? new Date() : currentTimeSnippet.getDate();
   }

   @Override
   public float getTotalDuration() {
      return getTotalDuration(TimeType.DEFAULT);
   }

   @Override
   public void flagAsCharged() {
      this.isCharged = true;
   }

   @Override
   public void refreshDummyTicket() {
      if (nonNull(ticket) && ticket.isDummyTicket()) {
         this.ticket = TicketBacklogSPI.getTicketBacklog().getTicket4Nr(ticket.getNr());
      }
   }

   /**
    * Calculates the total amount of working minuts of the current {@link TimeSnippet}
    * 
    * @param type
    *        the {@link TIME_TYPE}
    * @return the total amount of working minuts of the current {@link TimeSnippet}
    */
   @Override
   public float getTotalDuration(TIME_TYPE type) {
      if (isNull(currentTimeSnippet)) {
         return 0f;
      }
      float sum = currentTimeSnippet.getDuration(type);
      return NumberFormat.parseFloat(NumberFormat.format(sum));
   }

   @Override
   public TimeSnippet getCurrentTimeSnippet() {
      return currentTimeSnippet;
   }

   @Override
   public String getDescription() {
      return description;
   }

   @Override
   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Sets the charge-type as int from the given representation.
    * 
    * @param chargeTypeRep
    *        the new representation of a charge type
    */
   @Override
   public void setChargeType(String chargeTypeRep) {
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();
      this.chargeType = serviceCodeAdapter.getServiceCode4Description(chargeTypeRep);
   }

   @Override
   public void setChargeType(int chargeType) {
      this.chargeType = chargeType;
   }

   @Override
   public Ticket getTicket() {
      return ticket;
   }

   @Override
   public void setTicket(Ticket ticket) {
      this.ticket = ticket;
   }

   @Override
   public int getChargeType() {
      return chargeType;
   }

   @Override
   public boolean isCharged() {
      return isCharged;
   }

   @Override
   public boolean isBookable() {
      return nonNull(ticket) && ticket.isBookable();
   }

   @Override
   public void updateBeginTimeSnippetAndCalculate(String newTimeStampValue) {
      currentTimeSnippet.updateAndSetBeginTimeStamp(newTimeStampValue, true);
   }

   @Override
   public void updateEndTimeSnippetAndCalculate(String newTimeStampValue) {
      currentTimeSnippet.updateAndSetEndTimeStamp(newTimeStampValue, true);
   }

   /**
    * Creates a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementAdd}
    * 
    * @param update
    *        the {@link BusinessDayIncrementAdd} with the new values
    * @return a new {@link BusinessDayIncrement}
    */
   public static BusinessDayIncrement of(BusinessDayIncrementAdd update) {

      BusinessDayIncrement businessDayIncremental = new BusinessDayIncrementImpl();
      businessDayIncremental.setDescription(update.getDescription());
      businessDayIncremental.setTicket(update.getTicket());
      businessDayIncremental.setChargeType(update.getKindOfService());
      businessDayIncremental.startCurrentTimeSnippet(update.getTimeSnippet().getBeginTimeStamp());
      businessDayIncremental.stopCurrentTimeSnippet(update.getTimeSnippet().getEndTimeStamp());
      return businessDayIncremental;
   }

   /**
    * Creates a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementImport} with all its {@link TimeSnippet}s
    * 
    * @param businessDayIncrementImport
    * @return a new {@link BusinessDayIncrement}
    */
   public static BusinessDayIncrement of(BusinessDayIncrementImport businessDayIncrementImport) {

      List<TimeSnippet> timeSnippets2Add = businessDayIncrementImport.getTimeSnippets();
      BusinessDayIncrement businessDayIncremental = new BusinessDayIncrementImpl();
      businessDayIncremental.setDescription(businessDayIncrementImport.getDescription());
      TicketBacklog ticketBacklog = TicketBacklogSPI.getTicketBacklog();
      businessDayIncremental.setTicket(ticketBacklog.getTicket4Nr(businessDayIncrementImport.getTicketNo()));
      businessDayIncremental.setChargeType(businessDayIncrementImport.getKindOfService());

      for (TimeSnippet timeSnippet : timeSnippets2Add) {
         businessDayIncremental.startCurrentTimeSnippet(timeSnippet.getBeginTimeStamp());
         businessDayIncremental.stopCurrentTimeSnippet(timeSnippet.getEndTimeStamp());
      }
      return businessDayIncremental;
   }

   @Override
   public boolean isBefore(Time time2Check) {
      Date date = getDate();
      return TimeFactory.createNew(date.getTime()).isBefore(time2Check);
   }

   @Override
   public UUID getId() {
      return id;
   }

   public static BusinessDayIncrement of(BusinessDayIncrement otherBussinessDayIncremental) {
      BusinessDayIncrementImpl businessDayIncrementImpl = new BusinessDayIncrementImpl();
      if (nonNull(otherBussinessDayIncremental)) {
         businessDayIncrementImpl.id = otherBussinessDayIncremental.getId();
      }
      return businessDayIncrementImpl;
   }
}
