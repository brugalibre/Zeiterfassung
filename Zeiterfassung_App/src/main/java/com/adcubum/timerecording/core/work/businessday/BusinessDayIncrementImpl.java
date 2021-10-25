/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.UUID;

import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.importexport.in.businessday.BusinessDayIncrementImport;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import com.adcubum.timerecording.work.date.TimeType;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;
import com.adcubum.util.parser.NumberFormat;
import com.adcubum.util.utils.StringUtil;

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
   private boolean isBooked;

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
      this.isBooked = isCharged;
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
   public BusinessDayIncrement startCurrentTimeSnippet(DateTime beginTimeStamp) {
      TimeSnippet newCurrentTimeSnippet = TimeSnippetFactory.createNew()
            .setBeginTimeStamp(beginTimeStamp);
      return createNewBusinessDayIncrement(newCurrentTimeSnippet);
   }

   /**
    * @param endTimeStamp
    */
   @Override
   public BusinessDayIncrement stopCurrentTimeSnippet(DateTime endTimeStamp) {
      TimeSnippet changedCurrentTimeSnippet = currentTimeSnippet.setEndTimeStamp(endTimeStamp);
      return createNewBusinessDayIncrement(changedCurrentTimeSnippet);
   }

   @Override
   public BusinessDayIncrement resumeLastTimeSnippet() {
      TimeSnippet changedCurrentTimeSnippet = currentTimeSnippet.setEndTimeStamp(null);
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
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      businessDayIncrementImplCopy.isBooked = true;
      return businessDayIncrementImplCopy;
   }

   @Override
   public BusinessDayIncrement refreshDummyTicket() {
      if (ticket.isDummyTicket()) {
         BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
         businessDayIncrementImplCopy.ticket = TicketBacklogSPI.getTicketBacklog().getTicket4Nr(ticket.getNr());
         return businessDayIncrementImplCopy;
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
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      businessDayIncrementImplCopy.description = description;
      return businessDayIncrementImplCopy;
   }

   @Override
   public boolean hasDescription() {
      return StringUtil.isNotEmptyOrNull(getDescription());
   }

   /**
    * Sets the charge-type as int from the given representation.
    * 
    * @param chargeTypeRep
    *        the new representation of a charge type
    */
   @Override
   public BusinessDayIncrement setServiceCode4Description(String chargeTypeRep) {
      requireNonNull(chargeTypeRep);
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      businessDayIncrementImplCopy.chargeType = serviceCodeAdapter.getServiceCode4Description(chargeTypeRep);
      return businessDayIncrementImplCopy;
   }

   @Override
   public String getServiceCodeDescription() {
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();
      return serviceCodeAdapter.getServiceCodeDescription4ServiceCode(chargeType);
   }

   @Override
   public BusinessDayIncrement setServiceCode(int chargeType) {
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      businessDayIncrementImplCopy.chargeType = chargeType;
      return businessDayIncrementImplCopy;
   }

   @Override
   public Ticket getTicket() {
      return ticket;
   }

   @Override
   public BusinessDayIncrement setTicket(Ticket ticket) {
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      businessDayIncrementImplCopy.ticket = requireNonNull(ticket);
      return businessDayIncrementImplCopy;
   }


   @Override
   public int getChargeType() {
      return chargeType;
   }

   @Override
   public boolean isBooked() {
      return isBooked;
   }

   @Override
   public boolean isBookable() {
      return nonNull(ticket) && ticket.isBookable();
   }

   @Override
   public BusinessDayIncrement updateBeginTimeSnippetAndCalculate(String newTimeStampValue) {
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      TimeSnippet changedCurrentTimeSnippet = currentTimeSnippet.updateAndSetBeginTimeStamp(newTimeStampValue, true);
      businessDayIncrementImplCopy.currentTimeSnippet = changedCurrentTimeSnippet;
      return businessDayIncrementImplCopy;
   }

   @Override
   public BusinessDayIncrement updateEndTimeSnippetAndCalculate(String newTimeStampValue) {
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      TimeSnippet changedCurrentTimeSnippet = currentTimeSnippet.updateAndSetEndTimeStamp(newTimeStampValue, true);
      businessDayIncrementImplCopy.currentTimeSnippet = changedCurrentTimeSnippet;
      return businessDayIncrementImplCopy;
   }

   @Override
   public BusinessDayIncrement addAdditionallyTime(float time2Add) {
      BusinessDayIncrementImpl businessDayIncrementImplCopy = createCopy();
      TimeSnippet changedCurrentTimeSnippet = currentTimeSnippet.addAdditionallyTime(String.valueOf(time2Add));
      businessDayIncrementImplCopy.currentTimeSnippet = changedCurrentTimeSnippet;
      return businessDayIncrementImplCopy;
   }

   @Override
   public UUID getId() {
      return id;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + chargeType;
      result = prime * result + ((currentTimeSnippet == null) ? 0 : currentTimeSnippet.hashCode());
      result = prime * result + ((description == null) ? 0 : description.hashCode());
      result = prime * result + (isBooked ? 1231 : 1237);
      result = prime * result + ((ticket == null) ? 0 : ticket.hashCode());
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
      BusinessDayIncrementImpl other = (BusinessDayIncrementImpl) obj;
      if (chargeType != other.chargeType)
         return false;
      if (currentTimeSnippet == null) {
         if (other.currentTimeSnippet != null)
            return false;
      } else if (!currentTimeSnippet.equals(other.currentTimeSnippet))
         return false;
      if (description == null) {
         if (other.description != null)
            return false;
      } else if (!description.equals(other.description))
         return false;
      if (isBooked != other.isBooked)
         return false;
      if (ticket == null) {
         if (other.ticket != null)
            return false;
      } else if (!ticket.equals(other.ticket))
         return false;
      return true;
   }

   private BusinessDayIncrement createNewBusinessDayIncrement(TimeSnippet changedCurrentTimeSnippet) {
      BusinessDayIncrementImpl copy = createCopy();
      copy.currentTimeSnippet = changedCurrentTimeSnippet;
      return copy;
   }

   private BusinessDayIncrementImpl createCopy() {
      return (BusinessDayIncrementImpl) BusinessDayIncrementImpl.of(this);
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
            .setServiceCode(update.getKindOfService())
            .startCurrentTimeSnippet(update.getTimeSnippet().getBeginTimeStamp())
            .stopCurrentTimeSnippet(update.getTimeSnippet().getEndTimeStamp());
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
      TicketBacklog ticketBacklog = TicketBacklogSPI.getTicketBacklog();
      BusinessDayIncrement businessDayIncremental = new BusinessDayIncrementImpl()
            .setDescription(businessDayIncrementImport.getDescription())
            .setTicket(ticketBacklog.getTicket4Nr(businessDayIncrementImport.getTicketNo()))
            .setServiceCode(businessDayIncrementImport.getKindOfService());

      for (TimeSnippet timeSnippet : timeSnippets2Add) {
         businessDayIncremental = businessDayIncremental
               .startCurrentTimeSnippet(timeSnippet.getBeginTimeStamp())
               .stopCurrentTimeSnippet(timeSnippet.getEndTimeStamp());
      }
      return businessDayIncremental;
   }

   public static BusinessDayIncrement of(BusinessDayIncrement otherBussinessDayIncremental, boolean fullCopy) {
      BusinessDayIncrementImpl businessDayIncrementImpl = new BusinessDayIncrementImpl();
      if (nonNull(otherBussinessDayIncremental) && fullCopy) {
         businessDayIncrementImpl.id = otherBussinessDayIncremental.getId();
         businessDayIncrementImpl.description = otherBussinessDayIncremental.getDescription();
         businessDayIncrementImpl.ticket = otherBussinessDayIncremental.getTicket();
         businessDayIncrementImpl.chargeType = otherBussinessDayIncremental.getChargeType();
         businessDayIncrementImpl.currentTimeSnippet = otherBussinessDayIncremental.getCurrentTimeSnippet();
      }
      return businessDayIncrementImpl;
   }

   public static BusinessDayIncrement of(BusinessDayIncrement otherBussinessDayIncremental) {
      return of(otherBussinessDayIncremental, true);
   }
}
