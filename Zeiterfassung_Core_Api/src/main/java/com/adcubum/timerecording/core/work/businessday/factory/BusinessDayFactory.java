package com.adcubum.timerecording.core.work.businessday.factory;

import java.util.List;
import java.util.UUID;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;

/**
 * Factory in order to create {@link BusinessDay}s
 * 
 * @author Dominic
 *
 */
public class BusinessDayFactory extends AbstractFactory {
   private static final String BEAN_NAME = "businessday";
   private static final BusinessDayFactory INSTANCE = new BusinessDayFactory();

   private BusinessDayFactory() {
      super("modul-configration.xml");
   }

   /**
    * Creates a new {@link BusinessDay} instance
    * 
    * @param id
    *        the id of the {@link BusinessDay}
    * @param isBooked
    *        <code>true</code> if this {@link BusinessDay} is already booked and therefore is read-only or <code>false</code> if not
    * @param increments
    *        the {@link BusinessDayIncrement}s of this {@link BusinessDay}
    * @param currentTimeSnippet
    *        the current {@link TimeSnippet}
    * @param comeAndGoes
    *        the {@link ComeAndGoes}
    * @return a new {@link BusinessDay} instance
    */
   public static BusinessDay createNew(UUID id, boolean isBooked, List<BusinessDayIncrement> increments, TimeSnippet currentTimeSnippet,
         ComeAndGoes comeAndGoes) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, id, isBooked, increments, currentTimeSnippet, comeAndGoes);
   }
}
