/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.businessday.vo;

import java.util.Comparator;

import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.date.Time;

/**
 * The {@link TimeSnippetVO} is used whenever a we need
 * {@link TimeSnippet} for displaying or exporting. The {@link TimeSnippetVO} is read only
 * 
 * @author Dominic
 *
 */
public class TimeSnippetVO {

   public TimeSnippetVO(TimeSnippet timeSnippet) {
      this.beginTimeStamp = timeSnippet.getBeginTimeStamp();
      this.endTimeStamp = timeSnippet.getEndTimeStamp();
   }

   private Time beginTimeStamp;
   private Time endTimeStamp;

   public String getBeginTimeStampRep() {
      return String.valueOf(beginTimeStamp);
   }

   public final String getEndTimeStamp() {
      return String.valueOf(endTimeStamp);
   }

   public String getEndTimeStampRep() {
      return String.valueOf(endTimeStamp);
   }

   public static class TimeStampComparator implements Comparator<TimeSnippetVO> {
      @Override
      public int compare(TimeSnippetVO timeSnippet, TimeSnippetVO timeSnippet2) {
         Time beginTimeStamp1 = timeSnippet.beginTimeStamp;
         Time beginTimeStamp2 = timeSnippet2.beginTimeStamp;
         return beginTimeStamp1.compareTo(beginTimeStamp2);
      }
   }
}
