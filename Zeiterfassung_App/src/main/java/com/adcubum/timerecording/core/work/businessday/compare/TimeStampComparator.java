package com.adcubum.timerecording.core.work.businessday.compare;

import java.util.Comparator;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.work.date.Time;

public class TimeStampComparator implements Comparator<TimeSnippet> {
   @Override
   public int compare(TimeSnippet timeSnippet, TimeSnippet timeSnippet2) {
      Time beginTimeStamp1 = timeSnippet.getBeginTimeStamp();
      Time beginTimeStamp2 = timeSnippet2.getBeginTimeStamp();
      return beginTimeStamp1.compareTo(beginTimeStamp2);
   }
}