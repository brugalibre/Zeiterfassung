/**
 * 
 */
package com.myownb3.dominic.timerecording.core.importexport.out.businessday;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayVO;

/**
 * The {@link BusinessDayExporter} exports a {@link BusinessDay} into a
 * {@link List} of {@link String}.
 * 
 * @author Dominic
 *
 */
public class BusinessDayExporter {

   public static final String DATE_REP_PATTERN = "EEEEE, d MMM yyyy HH:mm:ss";
   public static final BusinessDayExporter INSTANCE = new BusinessDayExporter();
   private static final String LINE_SEPARATOR = "line.separator";

   private BusinessDayExporter() {
      // private constructor
   }

   private static final Object CONTENT_SEPPARATOR = "; ";
   private static final Object CONTENT_SEPPARATOR_TURBO_BUCHER = ";";

   /**
    * Selects line by line the content to export for the given
    * {@link BusinessDayVO}
    * 
    * @param bussinessDay
    *        the {@link BusinessDayVO} which has to be exported
    * @return a list of {@link String} to export
    */
   public List<String> exportBusinessDay(BusinessDayVO bussinessDay) {
      StringBuilder builder = new StringBuilder();
      List<String> content = new ArrayList<>();

      // First line to mark the date, when the time was recorded
      builder.append(bussinessDay.getDateRep(DATE_REP_PATTERN));
      builder.append(System.getProperty(LINE_SEPARATOR));
      builder.append(System.getProperty(LINE_SEPARATOR));

      appendTitleHeaderCells(builder);

      // = For each 'Ticket' or Increment of an entire Day
      for (BusinessDayIncrementVO inc : bussinessDay.getBusinessDayIncrements()) {
         builder.append(TextLabel.TICKET + ": ");
         builder.append(inc.getTicketNumber());
         builder.append(CONTENT_SEPPARATOR);
         builder.append(inc.getDescription());
         builder.append(CONTENT_SEPPARATOR);
         builder.append(inc.getTotalDurationRep());
         builder.append(CONTENT_SEPPARATOR);

         TimeSnippet snippet = inc.getCurrentTimeSnippet();

         builder.append(snippet.getBeginTimeStampRep());
         builder.append(CONTENT_SEPPARATOR);
         builder.append(snippet.getEndTimeStampRep());
         builder.append(CONTENT_SEPPARATOR);
         ServiceCodeAdapter serviceCodeAdapter = TimeRecorder.INSTANCE.getServiceCodeAdapter();
         builder.append(serviceCodeAdapter.getServiceCodeDescription4ServiceCode(inc.getChargeType()));
         builder.append(CONTENT_SEPPARATOR);
         builder.append(inc.isBooked() ? TextLabel.YES : TextLabel.NO);

         builder.append(System.getProperty(LINE_SEPARATOR));
         content.add(builder.toString());
         builder.delete(0, builder.capacity());
      }
      builder.append(System.getProperty(LINE_SEPARATOR));
      builder.append(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL + " " + bussinessDay.getTotalDurationRep());
      content.add(builder.toString());
      return content;
   }

   private void appendTitleHeaderCells(StringBuilder builder) {

      builder.append(TextLabel.TICKET);
      builder.append(CONTENT_SEPPARATOR);
      builder.append(TextLabel.DESCRIPTION_LABEL);
      builder.append(CONTENT_SEPPARATOR);
      builder.append(TextLabel.AMOUNT_OF_HOURS_LABEL);
      builder.append(CONTENT_SEPPARATOR);

      appendBeginEndHeader(builder);

      builder.append(TextLabel.BOOK_TYPE_LABEL);
      builder.append(CONTENT_SEPPARATOR);
      builder.append(TextLabel.BOOKED);
      builder.append(System.getProperty(LINE_SEPARATOR));
   }

   private void appendBeginEndHeader(StringBuilder builder) {
      builder.append(TextLabel.VON_LABEL);
      builder.append(CONTENT_SEPPARATOR);
      builder.append(TextLabel.BIS_LABEL);
      builder.append(CONTENT_SEPPARATOR);
   }

   /**
    * Collects all the necessary data in the proper format so the turbo-bucher can
    * charge-off the jira tickets
    * 
    * @param bussinessDay
    * @return the content which is required by the {@link Booker} in order to book
    */
   public List<String> collectContent4TurboBucher(BusinessDayVO bussinessDay) {

      StringBuilder builder = new StringBuilder();
      List<String> content = new ArrayList<>();

      List<BusinessDayIncrementVO> notChargedIncrements = getNotChargedIncrements(bussinessDay);
      for (BusinessDayIncrementVO inc : notChargedIncrements) {
         builder.append(inc.getTicketNumber());
         builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
         builder.append(inc.getChargeType());
         builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
         builder.append(inc.getTotalDurationRep());
         builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);

         builder.append(bussinessDay.getDateRep());
         if (inc.hasDescription()) {
            builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
            builder.append(inc.getDescription());
         }
         builder.append(System.getProperty(LINE_SEPARATOR));
         content.add(builder.toString());
         builder.delete(0, builder.capacity());
      }
      return content;
   }

   private List<BusinessDayIncrementVO> getNotChargedIncrements(BusinessDayVO bussinessDay) {
      return bussinessDay.getBusinessDayIncrements()//
            .stream()//
            .filter(bDayInc -> !bDayInc.isBooked())//
            .collect(Collectors.toList());
   }
}
