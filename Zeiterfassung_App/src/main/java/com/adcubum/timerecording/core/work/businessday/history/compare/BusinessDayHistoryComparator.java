package com.adcubum.timerecording.core.work.businessday.history.compare;

import java.time.LocalDate;
import java.util.Comparator;

import com.adcubum.timerecording.core.work.businessday.history.BusinessDayHistory;

public class BusinessDayHistoryComparator implements Comparator<BusinessDayHistory> {

   @Override
   public int compare(BusinessDayHistory businessDayHistory1, BusinessDayHistory businessDayHistory2) {
      LocalDate localDate1 = businessDayHistory1.getLocalDate();
      LocalDate localDate2 = businessDayHistory2.getLocalDate();
      return localDate1.compareTo(localDate2);
   }

}
