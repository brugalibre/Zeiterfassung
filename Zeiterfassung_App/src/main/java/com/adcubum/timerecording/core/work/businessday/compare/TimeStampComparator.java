package com.adcubum.timerecording.core.work.businessday.compare;

import java.util.Comparator;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.work.date.DateTime;

public class TimeStampComparator implements Comparator<TimeSnippet> {
   @Override
   public int compare(TimeSnippet timeSnippet, TimeSnippet timeSnippet2) {
      DateTime beginTimeStamp1 = timeSnippet.getBeginTimeStamp();
      DateTime beginTimeStamp2 = timeSnippet2.getBeginTimeStamp();
      return beginTimeStamp1.compareTo(beginTimeStamp2);
   }
}