package com.adcubum.timerecording.core.work.businessday;

import static java.util.Objects.nonNull;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.util.parser.DateParser;

/**
 * Factory in order to create {@link TimeSnippet}s
 * 
 * @author Dominic
 *
 */
public class TimeSnippetFactory extends AbstractFactory {
   private static final String BEAN_NAME = "timesnippet";
   private static final TimeSnippetFactory INSTANCE = new TimeSnippetFactory();

   private TimeSnippetFactory() {
      super("modul-configration.xml");
   }

   /**
    * Creates a new / empty {@link TimeSnippet} instance
    */
   public static TimeSnippet createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }

   /**
    * Creates a new {@link TimeSnippet} instance from a given instance as a copy
    * 
    * @param timeSnippet
    *        the bother {@link TimeSnippet}
    */
   public static TimeSnippet createNew(TimeSnippet timeSnippet) {
      if (nonNull(timeSnippet)) {
         return INSTANCE.createNewWithAgruments(BEAN_NAME, timeSnippet);
      }
      return null;
   }

   /**
    * Create new {@link TimeSnippet} for the given {@link Date} and begin,- an end
    * time stamp as String values
    * 
    * @param date
    *        the {@link Date}
    * @param beginValue
    *        the begin time stamp as String
    * @param endValue
    *        the end time stamp as String
    * @return a new {@link TimeSnippet}
    * @throws ParseException
    *         if the {@code #beginValue} or {@code #endValue}
    *         could'nt be parsed
    */
   public static TimeSnippet createNew(Date date, String beginValue, String endValue) throws ParseException {
      return createNew()
            .setBeginTimeStamp(DateParser.getTime(beginValue, date))
            .setEndTimeStamp(DateParser.getTime(endValue, date));
   }

   /**
    * Creates a new {@link TimeSnippet} instance for the given values
    * 
    * @param id
    *        the id of the {@link TimeSnippet} to create
    * @param beginTimeStampValue
    *        the long value of {@link TimeSnippet#getBeginTimeStamp()}
    * @param endTimeStampValue
    *        the long value of {@link TimeSnippet#getEndTimeStamp()}
    * @return a new {@link TimeSnippet} instance
    */
   public static TimeSnippet createNew(UUID id, Long beginTimeStampValue, Long endTimeStampValue) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, id, beginTimeStampValue, endTimeStampValue);
   }

}
