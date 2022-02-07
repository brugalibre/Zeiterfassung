package com.adcubum.timerecording.core.importexport.out.businessday;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.importexport.util.BusinessDayImportExportUtil;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.util.BusinessDayUtil;
import com.adcubum.util.parser.DateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.adcubum.util.parser.DateParser.DD_MM_YYYY;
import static java.util.Objects.nonNull;

public class BusinessDayExporterImpl implements BusinessDayExporter {

   private static final String LINE_SEPARATOR = "line.separator";
   private static final Logger LOG = LoggerFactory.getLogger(BusinessDayExporterImpl.class);

   private BusinessDayExporterImpl() {
      // private constructor
   }

   private static final Object CONTENT_SEPPARATOR = "; ";
   private static final Object CONTENT_SEPPARATOR_TURBO_BUCHER = ";";

   @Override
   public List<String> exportBusinessDay(BusinessDay businessDay) {
      LOG.info("Export businessDay \n'{}'", businessDay);
      StringBuilder builder = new StringBuilder();
      List<String> content = new ArrayList<>();

      // First line to mark the date, when the time was recorded
      builder.append(DateParser.parse2String(businessDay.getDateTime(), DATE_REP_PATTERN));
      builder.append(System.getProperty(LINE_SEPARATOR));
      builder.append(System.getProperty(LINE_SEPARATOR));

      appendTitleHeaderCells(builder);

      // = For each 'Ticket' or Increment of an entire Day
      for (BusinessDayIncrement inc : businessDay.getIncrements()) {
         LOG.info("Export BusinessDayIncrement '{}'", inc);
         builder.append(TextLabel.TICKET + ": ");
         builder.append(inc.getTicket().getNr());
         builder.append(CONTENT_SEPPARATOR);
         builder.append(inc.getDescription());
         builder.append(CONTENT_SEPPARATOR);
         builder.append(BusinessDayUtil.getTotalDurationRep(inc));
         builder.append(CONTENT_SEPPARATOR);

         TimeSnippet snippet = inc.getCurrentTimeSnippet();

         builder.append(snippet.getBeginTimeStampRep());
         builder.append(CONTENT_SEPPARATOR);
         builder.append(snippet.getEndTimeStampRep());
         builder.append(CONTENT_SEPPARATOR);
         builder.append(BusinessDayImportExportUtil.getTicketActivityString(inc.getTicketActivity()));
         builder.append(CONTENT_SEPPARATOR);
         builder.append(inc.isBooked() ? TextLabel.YES : TextLabel.NO);

         builder.append(System.getProperty(LINE_SEPARATOR));
         content.add(builder.toString());
         LOG.info("Export BusinessDayIncrement '{}'", builder);
         builder.delete(0, builder.capacity());
      }
      builder.append(System.getProperty(LINE_SEPARATOR));
      builder.append(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL + " " + BusinessDayUtil.getTotalDurationRep(businessDay));
      content.add(builder.toString());
      LOG.info("Created last line '{}'", builder);
      LOG.info("Successfully exported BusinessDay");
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

      builder.append(TextLabel.SERVICE_CODE_LABEL);
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
   public List<String> collectContent4Booking(BusinessDay businessDay) {

      StringBuilder builder = new StringBuilder();
      List<String> content = new ArrayList<>();
      List<BusinessDayIncrement> notChargedIncrements = getNotChargedIncrements(businessDay);
      for (BusinessDayIncrement inc : notChargedIncrements) {
         builder.append(inc.getTicket().getNr());
         builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
         builder.append(inc.getTicketActivity().getActivityCode());
         builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
         builder.append(BusinessDayUtil.getTotalDurationRep(inc));
         builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);

         builder.append(DateParser.parse2String(businessDay.getDateTime(), DD_MM_YYYY));
         if (nonNull(inc.getDescription())) {
            builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
            builder.append(inc.getDescription());
         }
         builder.append(System.getProperty(LINE_SEPARATOR));
         content.add(builder.toString());
         builder.delete(0, builder.capacity());
      }
      return content;
   }

   private List<BusinessDayIncrement> getNotChargedIncrements(BusinessDay businessDay) {
      return businessDay.getIncrements()//
            .stream()//
            .filter(bDayInc -> !bDayInc.isBooked())//
            .collect(Collectors.toList());
   }
}
