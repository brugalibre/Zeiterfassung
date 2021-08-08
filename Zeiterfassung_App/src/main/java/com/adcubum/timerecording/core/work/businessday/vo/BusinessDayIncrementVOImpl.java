/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday.vo;

import java.util.UUID;
import java.util.function.Function;

import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.jira.constants.TicketConst;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.util.parser.NumberFormat;
import com.adcubum.util.utils.StringUtil;

/**
 * The {@link BusinessDayIncrementVO} is used whenever a we need
 * {@link BusinessDayIncrement} for displaying or exporting. The {@link BusinessDayIncrementVO} is read only
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncrementVOImpl implements BusinessDayIncrementVO {

   private UUID id;
   private TimeSnippet currentTimeSnippet;

   private float totalDuration;
   private String description;
   private Ticket ticket;
   private int chargeType;
   private boolean isBooked;
   private Function<Integer, String> serviceCodeDescProvider;

   private BusinessDayIncrementVOImpl(BusinessDayIncrement businessDayIncremental) {
      this.id = businessDayIncremental.getId();
      this.currentTimeSnippet = businessDayIncremental.getCurrentTimeSnippet();
      this.description = businessDayIncremental.getDescription();
      this.ticket = businessDayIncremental.getTicket();
      this.chargeType = businessDayIncremental.getChargeType();
      this.totalDuration = businessDayIncremental.getTotalDuration();
      this.isBooked = businessDayIncremental.isCharged();
      this.serviceCodeDescProvider = getSserviceCodeDescProvider();
   }

   private static Function<Integer, String> getSserviceCodeDescProvider() {
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();
      return serviceCodeAdapter::getServiceCodeDescription4ServiceCode;
   }

   @Override
   public UUID getId() {
      return id;
   }

   @Override
   public boolean hasDescription() {
      return StringUtil.isNotEmptyOrNull(description);
   }

   @Override
   public final String getTotalDurationRep() {
      return NumberFormat.format(this.totalDuration);
   }

   @Override
   public final String getDescription() {
      return this.description != null ? description : "";
   }

   @Override
   public Ticket getTicket() {
      return ticket;
   }

   @Override
   public String getTicketNumber() {
      return ticket != null ? ticket.getNr() : TicketConst.DEFAULT_TICKET_NAME;
   }

   @Override
   public final int getChargeType() {
      return this.chargeType;
   }

   @Override
   public boolean isBooked() {
      return isBooked;
   }

   @Override
   public final TimeSnippet getCurrentTimeSnippet() {
      return this.currentTimeSnippet;
   }

   @Override
   public String getServiceCodeDescription4ServiceCode() {
      return serviceCodeDescProvider.apply(chargeType);
   }

   /**
    * Returns a new {@link BusinessDayIncrementVO} for the given
    * {@link BusinessDayIncrement}
    * 
    * @param currentBussinessDayIncremental
    * @return a new {@link BusinessDayIncrementVO} for the given
    *         {@link BusinessDayIncrement}
    */
   public static BusinessDayIncrementVO of(BusinessDayIncrement currentBussinessDayIncremental) {
      return new BusinessDayIncrementVOImpl(currentBussinessDayIncremental);
   }
}
