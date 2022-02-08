package com.adcubum.timerecording.message;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;

/**
 * The {@link BookBusinessDayMessageApiService} decides which {@link BusinessDayIncrement} has to be
 * sent and if there has to be sent anything at all.
 * After the {@link BusinessDayIncrement}s to sent are determined, the {@link BookBusinessDayMessageApiService} is
 * called in order to actually send them
 *
 * @author dstalder
 */
public interface BookBusinessDayMessageApiService {

   BookBusinessDayMessageApiService INSTANCE = BookBusinessDayMessageApiServiceFactory.createNew();

   /**
    * Sends the {@link BusinessDayIncrement}s, which are booked and not sent yet, via the
    * {@link BookBusinessDayMessageApiService} as a message to any receiver listening.
    *
    * @param businessDay the booked {@link BusinessDay}
    * @return a copy of the given {@link BusinessDay} which may contains sent {@link BusinessDayIncrement}s
    */
   BusinessDay sendBookedIncrements(BusinessDay businessDay);

   /**
    * Evaluates if the configuration is properly done and if there are any {@link BusinessDayIncrement} to send
    *
    * @return <code>true</code> if there are any unsent messages which can be sent to a master {@link TimeRecorder}
    * or <code>false</code> if not
    */
   boolean canSendBookedBusinessDayIncrements();
}
