package com.adcubum.timerecording.core.importexport.out.businessday;

import java.util.List;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.util.parser.DateParser;

/**
 * The {@link BusinessDayExporter} exports a {@link BusinessDay} into a
 * {@link List} of {@link String}.
 * 
 * @author Dominic
 *
 */
public interface BusinessDayExporter {
   /** The pattern for representing a date */
   public static final String DATE_REP_PATTERN = DateParser.DATE_PATTERN;

   /**
    * The singleton instance of a {@link BusinessDayExporter}
    */
   public static final BusinessDayExporter INSTANCE = BusinessDayExporterFactory.createNew();

   /**
    * Collects all the necessary data in the proper format so the turbo-bucher can
    * charge-off the jira tickets
    * 
    * @param businessDay
    * @return the content which is required by the {@link BookerAdapter} in order to book
    */
   List<String> collectContent4Booking(BusinessDay businessDay);

   /**
    * Selects line by line the content to export for the given
    * {@link BusinessDayVO}
    * 
    * @param businessDay
    *        the {@link BusinessDayVO} which has to be exported
    * @return a list of {@link String} to export
    */
   List<String> exportBusinessDay(BusinessDay businessDay);

}
