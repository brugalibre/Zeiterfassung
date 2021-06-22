/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

   private Date date;
   private String description;
   private Ticket ticket;
   private int chargeType;
   private boolean isCharged;

   /**
    * Creates a new {@link BusinessDayIncrement} for the given {@link Date}
    * 
    * @param date
    *        the {@link Date}
    */
   public BusinessDayIncrementImpl(Date date) {
      this.date = requireNonNull(date);
   }

   /**
    * @param beginTimeStamp
    */
   @Override
   public void startCurrentTimeSnippet(Time beginTimeStamp) {
      createNewTimeSnippet();
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

   private void createNewTimeSnippet() {
      currentTimeSnippet = TimeSnippetFactory.createNew(date);
   }

   @Override
   public Date getDate() {
      return date;
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

   public static class TimeStampComparator implements Comparator<TimeSnippet> {
      @Override
      public int compare(TimeSnippet timeSnippet, TimeSnippet timeSnippet2) {
         Time beginTimeStamp1 = timeSnippet.getBeginTimeStamp();
         Time beginTimeStamp2 = timeSnippet2.getBeginTimeStamp();
         return beginTimeStamp1.compareTo(beginTimeStamp2);
      }
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

      BusinessDayIncrement businessDayIncremental = new BusinessDayIncrementImpl(update.getTimeSnippet().getDate());
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
      Date date = new Date();
      if (!timeSnippets2Add.isEmpty()) {
         date = timeSnippets2Add.get(0).getDate();
      }
      BusinessDayIncrement businessDayIncremental = new BusinessDayIncrementImpl(date);
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
      return TimeFactory.createNew(date.getTime()).isBefore(time2Check);
   }
}
