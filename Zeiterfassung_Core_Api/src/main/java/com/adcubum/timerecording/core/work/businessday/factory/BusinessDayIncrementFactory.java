package com.adcubum.timerecording.core.work.businessday.factory;

import java.util.UUID;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.jira.data.ticket.Ticket;

/**
 * Factory in order to create {@link BusinessDayIncrement}s
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncrementFactory extends AbstractFactory {
   private static final String BEAN_NAME = "businessday-increment";
   private static final BusinessDayIncrementFactory INSTANCE = new BusinessDayIncrementFactory();

   private BusinessDayIncrementFactory() {
      super("modul-configration.xml");
   }

   /**
    * Creates a new {@link BusinessDayIncrement} instance
    * 
    * @param currentTimeSnippet
    *        the current {@link TimeSnippet} of the {@link BusinessDayIncrement} to create. May be null
    * @param description
    *        the (ticket) description of this {@link BusinessDayIncrement}
    * @param ticket
    *        the ticket itself
    * @param chargeType
    *        the charge type
    * @param isCharged
    *        <code>true</code> if this {@link BusinessDayIncrement} is charged or <code>false</code> if not
    * @param the
    *        id of the {@link BusinessDayIncrement}
    * @return a new {@link BusinessDayIncrement} instance
    */
   public static BusinessDayIncrement createNew(TimeSnippet currentTimeSnippet, UUID id, String description, Ticket ticket, int chargeType,
         boolean isCharged) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, currentTimeSnippet, id, description, ticket, chargeType, isCharged);
   }
}
