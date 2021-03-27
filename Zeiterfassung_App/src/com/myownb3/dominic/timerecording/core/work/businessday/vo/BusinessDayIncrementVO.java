/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.businessday.vo;

import com.adcubum.timerecording.jira.data.Ticket;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.util.parser.NumberFormat;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * The {@link BusinessDayIncrementVO} is used whenever a we need
 * {@link BusinessDayIncrement} for displaying or exporting. The {@link BusinessDayIncrementVO} is read only
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncrementVO {

   private TimeSnippet currentTimeSnippet;

   private float totalDuration;
   private String description;
   private Ticket ticket;
   private int chargeType;
   private boolean isBooked;

   private BusinessDayIncrementVO(BusinessDayIncrement businessDayIncremental) {

      this.currentTimeSnippet = businessDayIncremental.getCurrentTimeSnippet();
      this.description = businessDayIncremental.getDescription();
      this.ticket = businessDayIncremental.getTicket();
      this.chargeType = businessDayIncremental.getChargeType();
      this.totalDuration = businessDayIncremental.getTotalDuration();
      this.isBooked = businessDayIncremental.isCharged();
   }

   /**
    * Returns <code>true</code> if this {@link BusinessDayIncrement} has a valid
    * description or <code>false</code> if not
    * 
    * @return<code>true</code> if this {@link BusinessDayIncrement} has a valid
    * description or <code>false</code> if not
    */
   public boolean hasDescription() {
      return StringUtil.isNotEmptyOrNull(description);
   }

   public final String getTotalDurationRep() {
      return NumberFormat.format(this.totalDuration);
   }

   public final String getDescription() {
      return this.description != null ? description : "";
   }

   public Ticket getTicket() {
      return ticket;
   }

   public String getTicketNumber() {
      return ticket != null ? ticket.getNr() : "SYRIUS";
   }

   public final int getChargeType() {
      return this.chargeType;
   }

   public boolean isBooked() {
      return isBooked;
   }

   public final TimeSnippet getCurrentTimeSnippet() {
      return this.currentTimeSnippet;
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
      return new BusinessDayIncrementVO(currentBussinessDayIncremental);
   }
}
