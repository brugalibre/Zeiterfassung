package com.adcubum.timerecording.core.work.businessday;

import static java.util.Objects.nonNull;

import java.text.ParseException;
import java.util.Date;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.work.date.Time;
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
    * Creates a new {@link TimeSnippet} instance
    * 
    * @param begin
    *        the begin {@link Time} of the created {@link TimeSnippet}
    */
   public static TimeSnippet createNew(Time begin) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, begin);
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
    * Creates a new {@link TimeSnippet} instance
    * 
    * @param date
    *        the begin {@link Date} of the created {@link TimeSnippet}
    */
   public static TimeSnippet createNew(Date date) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, date);
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
      TimeSnippet timeSnippet = TimeSnippetFactory.createNew(date);
      timeSnippet.setBeginTimeStamp(DateParser.getTime(beginValue, date));
      timeSnippet.setEndTimeStamp(DateParser.getTime(endValue, date));
      return timeSnippet;
   }

}
