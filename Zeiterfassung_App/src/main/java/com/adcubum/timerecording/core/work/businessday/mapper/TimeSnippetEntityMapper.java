package com.adcubum.timerecording.core.work.businessday.mapper;

import static java.util.Objects.isNull;

import java.sql.Timestamp;
import java.util.Objects;

import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.core.businessday.entity.TimeSnippetEntity;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetFactory;
import com.adcubum.timerecording.work.date.Time;

/**
 * The {@link TimeSnippetEntityMapper} is used as a Mapper in order to map from a {@link BusinessDay} into a {@link BusinessDayEntity}
 * and vice versa
 * 
 * @author dstalder
 *
 */
public class TimeSnippetEntityMapper {

   /** Singleton instance of the {@link TimeSnippetEntityMapper} */
   public static final TimeSnippetEntityMapper INSTANCE = new TimeSnippetEntityMapper();

   private TimeSnippetEntityMapper() {
      // private
   }

   /**
    * Maps the given {@link TimeSnippet} to a {@link TimeSnippetEntity}
    * 
    * @param timeSnippet
    *        the {@link TimeSnippet} to map
    * @return a new {@link TimeSnippetEntity}
    */
   public TimeSnippetEntity map2TimeSnippetEntity(TimeSnippet timeSnippet) {
      if (isNull(timeSnippet)) {
         return null;
      }
      Time beginTimestamp = timeSnippet.getBeginTimeStamp();
      Time endTimestamp = timeSnippet.getEndTimeStamp();
      return new TimeSnippetEntity(timeSnippet.getId(), isNull(beginTimestamp) ? null : new Timestamp(beginTimestamp.getTime()),
            isNull(endTimestamp) ? null : new Timestamp(endTimestamp.getTime()));
   }

   /**
    * Creates a new {@link TimeSnippet} for the given {@link TimeSnippetEntity}
    * 
    * @param timeSnippetEntity
    *        the {@link TimeSnippetEntity}
    * @return a new {@link TimeSnippet} for the given {@link TimeSnippetEntity}
    */
   public TimeSnippet map2TimeSnippet(TimeSnippetEntity timeSnippetEntity) {
      if (isNull(timeSnippetEntity)) {
         return null;
      }
      Long beginTimestamp = getDateTimeValue(timeSnippetEntity.getBeginTimestamp());
      Long endTimestamp = getDateTimeValue(timeSnippetEntity.getEndTimestamp());
      return TimeSnippetFactory.createNew(timeSnippetEntity.getId(), beginTimestamp, endTimestamp);
   }

   private static Long getDateTimeValue(Timestamp dateTime) {
      return Objects.isNull(dateTime) ? null : dateTime.getTime();
   }
}
