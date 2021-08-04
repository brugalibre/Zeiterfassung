package com.adcubum.timerecording.core.work.businessday.factory;

import java.util.List;
import java.util.UUID;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
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
    * @param increments
    *        the {@link BusinessDayIncrement}s of this {@link BusinessDay}
    * @param currentBDIncrement
    *        the current {@link BusinessDayIncrement}
    * @param comeAndGoes
    *        the {@link ComeAndGoes}
    * @return a new {@link BusinessDay} instance
    */
   public static BusinessDay createNew(UUID id, List<BusinessDayIncrement> increments, BusinessDayIncrement currentBDIncrement,
         ComeAndGoes comeAndGoes) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, id, increments, currentBDIncrement, comeAndGoes);
   }
}
