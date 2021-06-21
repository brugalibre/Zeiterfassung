/**
 * 
 */
package com.adcubum.timerecording.core.importexport.out.businessday;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayVOImpl;

public class BusinessDayExporterImpl implements BusinessDayExporter {

   private static final String LINE_SEPARATOR = "line.separator";

   private BusinessDayExporterImpl() {
      // private constructor
   }

   private static final Object CONTENT_SEPPARATOR = "; ";
   private static final Object CONTENT_SEPPARATOR_TURBO_BUCHER = ";";

   @Override
   public List<String> exportBusinessDay(BusinessDay bussinessDay) {
      StringBuilder builder = new StringBuilder();
      List<String> content = new ArrayList<>();
      BusinessDayVO bussinessDayVO = BusinessDayVOImpl.of(bussinessDay);

      // First line to mark the date, when the time was recorded
      builder.append(bussinessDayVO.getDateRep(DATE_REP_PATTERN));
      builder.append(System.getProperty(LINE_SEPARATOR));
      builder.append(System.getProperty(LINE_SEPARATOR));

      appendTitleHeaderCells(builder);

      // = For each 'Ticket' or Increment of an entire Day
      for (BusinessDayIncrementVO inc : bussinessDayVO.getBusinessDayIncrements()) {
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
         builder.append(inc.getServiceCodeDescription4ServiceCode());
         builder.append(CONTENT_SEPPARATOR);
         builder.append(inc.isBooked() ? TextLabel.YES : TextLabel.NO);

         builder.append(System.getProperty(LINE_SEPARATOR));
         content.add(builder.toString());
         builder.delete(0, builder.capacity());
      }
      builder.append(System.getProperty(LINE_SEPARATOR));
      builder.append(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL + " " + bussinessDayVO.getTotalDurationRep());
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

   @Override
   public List<String> collectContent4Booking(BusinessDay bussinessDay) {

      StringBuilder builder = new StringBuilder();
      List<String> content = new ArrayList<>();
      BusinessDayVO bussinessDayVO = BusinessDayVOImpl.of(bussinessDay);
      List<BusinessDayIncrementVO> notChargedIncrements = getNotChargedIncrements(bussinessDayVO);
      for (BusinessDayIncrementVO inc : notChargedIncrements) {
         builder.append(inc.getTicketNumber());
         builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
         builder.append(inc.getChargeType());
         builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
         builder.append(inc.getTotalDurationRep());
         builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);

         builder.append(bussinessDayVO.getDateRep());
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
