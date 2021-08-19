package com.adcubum.timerecording.core.work.businessday.compare;

import java.util.Comparator;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;

public class BusinessDayIncrementComparator implements Comparator<BusinessDayIncrement> {
   @Override
   public int compare(BusinessDayIncrement businessDayIncrement, BusinessDayIncrement businessDayIncrement2) {
      TimeSnippet timeSnippet1 = businessDayIncrement.getCurrentTimeSnippet();
      TimeSnippet timeSnippet2 = businessDayIncrement2.getCurrentTimeSnippet();
      return new TimeStampComparator().compare(timeSnippet1, timeSnippet2);
   }
}
