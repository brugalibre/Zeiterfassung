package com.adcubum.timerecording.core.work.businessday;

import com.adcubum.timerecording.core.domainmodel.BaseDomainModel;
import com.adcubum.timerecording.core.importexport.in.businessday.BusinessDayIncrementImport;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import com.adcubum.timerecording.work.date.TimeType;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;
import com.adcubum.util.parser.NumberFormat;
import com.adcubum.util.utils.StringUtil;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.Objects.*;

/**
 * A {@link BusinessDayImpl} consist of one or more {@link BusinessDayIncrement}.
 * Where as a {@link BusinessDayIncrement} consist of one or more
 * {@link TimeSnippet}. Two different {@link BusinessDayIncrement} are <i> not
 * </i> dependent!
 * 
 * @author Dominic
 */
public class BusinessDayIncrementImpl extends BaseDomainModel implements BusinessDayIncrement {
   private TimeSnippet currentTimeSnippet;

   private String description;
   private Ticket ticket;
   private boolean isBooked;
   private boolean isSent;
   private TicketActivity ticketActivity;

   /**
    * Constructor used by the factory
    */
   @SuppressWarnings("unused")
   private BusinessDayIncrementImpl(TimeSnippet currentTimeSnippet, UUID id, String description, Ticket ticket,
                                    TicketActivity ticketActivity, boolean isBooked, boolean isSent) {
      super(id);
      this.currentTimeSnippet = currentTimeSnippet;
      this.description = description;
      this.ticket = ticket;
      this.ticketActivity = ticketActivity;
      this.isBooked = isBooked;
      this.isSent = isSent;
   }

   /**
    * Creates a new and empty {@link BusinessDayIncrement}
    * 
    */
   public BusinessDayIncrementImpl() {
      super(null);
   }

   @Override
   public BusinessDayIncrement startCurrentTimeSnippet(DateTime beginTimeStamp) {
      if (isBooked){
         // there is obviously something wrong if someone tries this..
         throw new IllegalStateException("BusinessDayIncrement is already booked!");
      }
      TimeSnippet newCurrentTimeSnippet = TimeSnippetFactory.createNew()
            .setBeginTimeStamp(beginTimeStamp);
      return createNewBusinessDayIncrement(newCurrentTimeSnippet);
   }

   @Override
   public BusinessDayIncrement stopCurrentTimeSnippet(DateTime endTimeStamp) {
      if (isBooked){
         // there is obviously something wrong if someone tries this..
         throw new IllegalStateException("BusinessDayIncrement is already booked!");
      }
      TimeSnippet changedCurrentTimeSnippet = currentTimeSnippet.setEndTimeStamp(endTimeStamp);
      return createNewBusinessDayIncrement(changedCurrentTimeSnippet);
   }

   @Override
   public DateTime getDateTime() {
      return isNull(currentTimeSnippet) ? DateTimeFactory.createNew() : currentTimeSnippet.getDateTime();
   }

   @Override
   public float getTotalDuration() {
      return getTotalDuration(TimeType.DEFAULT);
   }

   @Override
   public BusinessDayIncrement flagAsBooked() {
      return changeBusinessDayIncrementInternal(businessDayIncrementImplCopy -> businessDayIncrementImplCopy.isBooked = true);
   }

   @Override
   public BusinessDayIncrement flagAsSent() {
      return changeBusinessDayIncrementInternal(businessDayIncrementImplCopy -> businessDayIncrementImplCopy.isSent = true);
   }

   @Override
   public BusinessDayIncrement refreshDummyTicket() {
      if (ticket.isDummyTicket()) {
         return changeBusinessDayIncrementInternal(businessDayIncrement ->
                 businessDayIncrement.ticket = TicketBacklogSPI.getTicketBacklog().getTicket4Nr(ticket.getNr()));
      }
      return this;
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
   public BusinessDayIncrement setDescription(String description) {
      if (isBooked){
         return this;
      }
      return changeBusinessDayIncrementInternal(businessDayIncrement -> businessDayIncrement.description = description);
   }

   @Override
   public boolean hasDescription() {
      return StringUtil.isNotEmptyOrNull(getDescription());
   }

   /**
    * Sets the charge-type as int from the given representation.
    * 
    * @param ticketActivity
    *        the new {@link TicketActivity}
    */
   @Override
   public BusinessDayIncrement setTicketActivity(TicketActivity ticketActivity) {
      // Don't change to an invalid TicketActivity
      if (ticketActivity.isDummy() && isCurrentTicketActivityNotDummy() || isBooked){
         return this;
      }
      return changeBusinessDayIncrementInternal(businessDayIncrementCopy -> businessDayIncrementCopy.ticketActivity = requireNonNull(ticketActivity));
   }

   private boolean isCurrentTicketActivityNotDummy() {
      return nonNull(this.ticketActivity) && !this.ticketActivity.isDummy();
   }

   @Override
   public Ticket getTicket() {
      return ticket;
   }

   @Override
   public BusinessDayIncrement setTicket(Ticket ticket) {
      if (isBooked){
         return this;
      }
      return changeBusinessDayIncrementInternal(businessDayIncrementImplCopy -> businessDayIncrementImplCopy.ticket = requireNonNull(ticket));
   }

   @Override
   public TicketActivity getTicketActivity() {
      return ticketActivity;
   }

   @Override
   public boolean isBooked() {
      return isBooked;
   }

   @Override
   public boolean isSent() {
      return isSent;
   }

   @Override
   public boolean isBookable() {
      return nonNull(ticket) && ticket.isBookable() && !ticketActivity.isDummy();
   }

   @Override
   public BusinessDayIncrement updateBeginTimeSnippetAndCalculate(String newTimeStampValue) {
      if (isBooked){
         return this;
      }
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      TimeSnippet changedCurrentTimeSnippet = currentTimeSnippet.updateAndSetBeginTimeStamp(newTimeStampValue, true);
      businessDayIncrementImplCopy.currentTimeSnippet = changedCurrentTimeSnippet;
      return businessDayIncrementImplCopy;
   }

   @Override
   public BusinessDayIncrement updateEndTimeSnippetAndCalculate(String newTimeStampValue) {
      if (isBooked){
         return this;
      }
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      TimeSnippet changedCurrentTimeSnippet = currentTimeSnippet.updateAndSetEndTimeStamp(newTimeStampValue, true);
      businessDayIncrementImplCopy.currentTimeSnippet = changedCurrentTimeSnippet;
      return businessDayIncrementImplCopy;
   }

   @Override
   public BusinessDayIncrement addAdditionallyTime(float time2Add) {
      if (isBooked){
         return this;
      }
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      TimeSnippet changedCurrentTimeSnippet = currentTimeSnippet.addAdditionallyTime(String.valueOf(time2Add));
      businessDayIncrementImplCopy.currentTimeSnippet = changedCurrentTimeSnippet;
      return businessDayIncrementImplCopy;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      BusinessDayIncrementImpl that = (BusinessDayIncrementImpl) o;
      return isBooked == that.isBooked && Objects.equals(currentTimeSnippet, that.currentTimeSnippet) && Objects.equals(description, that.description) && Objects.equals(ticket, that.ticket) && Objects.equals(ticketActivity, that.ticketActivity);
   }

   @Override
   public int hashCode() {
      return hash(currentTimeSnippet, description, ticket, isBooked, ticketActivity);
   }

   private BusinessDayIncrement changeBusinessDayIncrementInternal(Consumer<BusinessDayIncrementImpl> businessDayIncrementUpdater) {
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      businessDayIncrementUpdater.accept(businessDayIncrementImplCopy);
      return businessDayIncrementImplCopy;
   }

   private BusinessDayIncrement createNewBusinessDayIncrement(TimeSnippet changedCurrentTimeSnippet) {
      BusinessDayIncrementImpl copy = createCopy();
      copy.currentTimeSnippet = changedCurrentTimeSnippet;
      return copy;
   }

   private BusinessDayIncrementImpl createCopy() {
      return (BusinessDayIncrementImpl) BusinessDayIncrementImpl.of(this);
   }

   @Override
   public String toString() {
      return "BusinessDayIncrementImpl {" +
              "\n\tcurrentTimeSnippet=" + currentTimeSnippet + "," +
              "\n\tid=" + id + "," +
              "\n\tdescription='" + description + "'," +
              "\n\tticket=" + ticket + "," +
              "\n\tisBooked=" + isBooked + "," +
              "\n\tticketActivity=" + ticketActivity + "," +
              "\n}";
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

      BusinessDayIncrementImpl businessDayIncremental = new BusinessDayIncrementImpl();
      businessDayIncremental.id = update.getId();
      return businessDayIncremental.setDescription(update.getDescription())
            .setTicket(update.getTicket())
            .setTicketActivity(update.getTicketActivity())
            .startCurrentTimeSnippet(update.getTimeSnippet().getBeginTimeStamp())
            .stopCurrentTimeSnippet(update.getTimeSnippet().getEndTimeStamp());
   }

   /**
    * Creates a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementImport} with all its {@link TimeSnippet}s
    * 
    * @param businessDayIncrementImport the {@link BusinessDayIncrementImport} to copy from
    * @return a new {@link BusinessDayIncrement}
    */
   public static BusinessDayIncrement of(BusinessDayIncrementImport businessDayIncrementImport) {

      List<TimeSnippet> timeSnippets2Add = businessDayIncrementImport.getTimeSnippets();
      TicketBacklog ticketBacklog = TicketBacklogSPI.getTicketBacklog();
      BusinessDayIncrement businessDayIncremental = new BusinessDayIncrementImpl()
            .setDescription(businessDayIncrementImport.getDescription())
            .setTicket(ticketBacklog.getTicket4Nr(businessDayIncrementImport.getTicketNo()))
            .setTicketActivity(businessDayIncrementImport.getTicketActivity());

      for (TimeSnippet timeSnippet : timeSnippets2Add) {
         businessDayIncremental = businessDayIncremental
               .startCurrentTimeSnippet(timeSnippet.getBeginTimeStamp())
               .stopCurrentTimeSnippet(timeSnippet.getEndTimeStamp());
      }
      return businessDayIncremental;
   }

   public static BusinessDayIncrement of(BusinessDayIncrement otherBusinessDayIncremental, boolean fullCopy) {
      BusinessDayIncrementImpl businessDayIncrementImpl = new BusinessDayIncrementImpl();
      if (nonNull(otherBusinessDayIncremental) && fullCopy) {
         businessDayIncrementImpl.id = otherBusinessDayIncremental.getId();
         businessDayIncrementImpl.description = otherBusinessDayIncremental.getDescription();
         businessDayIncrementImpl.ticket = otherBusinessDayIncremental.getTicket();
         businessDayIncrementImpl.ticketActivity = otherBusinessDayIncremental.getTicketActivity();
         businessDayIncrementImpl.isSent = otherBusinessDayIncremental.isSent();
         businessDayIncrementImpl.isBooked = otherBusinessDayIncremental.isBooked();
         businessDayIncrementImpl.currentTimeSnippet = otherBusinessDayIncremental.getCurrentTimeSnippet();
      }
      return businessDayIncrementImpl;
   }

   public static BusinessDayIncrement of(BusinessDayIncrement otherBusinessDayIncremental) {
      return of(otherBusinessDayIncremental, true);
   }
}
