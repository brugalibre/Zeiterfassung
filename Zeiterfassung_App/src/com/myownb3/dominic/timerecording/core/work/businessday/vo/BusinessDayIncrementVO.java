/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.businessday.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.util.parser.NumberFormat;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * The {@link BusinessDayIncrementVO} is used whenever a we need
 * {@link BusinessDayIncrement} for displaying or exporting. The {@link BusinessDayIncrementVO} is read only
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncrementVO {

   private List<TimeSnippetVO> timeSnippets;
   private List<TimeSnippetVOPlaceHolder> timeSnippetPlaceHolders;

   private TimeSnippet currentTimeSnippet;

   private float totalDuration;
   private String description;
   private String ticketNumber;
   private int chargeType;
   private boolean isCharged;

   private BusinessDayIncrementVO(BusinessDayIncrement businessDayIncremental) {

      this.currentTimeSnippet = TimeSnippet.of(businessDayIncremental.getCurrentTimeSnippet());
      this.description = businessDayIncremental.getDescription();
      this.ticketNumber = businessDayIncremental.getTicketNumber();
      this.chargeType = businessDayIncremental.getChargeType();
      this.totalDuration = businessDayIncremental.getTotalDuration();
      this.isCharged = businessDayIncremental.isCharged();

      timeSnippets = businessDayIncremental.getTimeSnippets()//
            .stream()//
            .map(TimeSnippetVO::new)//
            .collect(Collectors.toList());
      Collections.sort(timeSnippets, new TimeSnippetVO.TimeStampComparator());
      timeSnippetPlaceHolders = Collections.emptyList();
   }

   /**
    * Returns <code>true</code> if this {@link BusinessDayIncrement} has a valid
    * description or <code>false</code> if not
    * 
    * @return<code>true</code> if this {@link BusinessDayIncrement} has a valid
    *                          description or <code>false</code> if not
    */
   public boolean hasDescription() {
      return StringUtil.isNotEmptyOrNull(description);
   }

   /**
    * All rows must fit with it content to the title header. Thats why we have to
    * add some placeholders if this row has less TimeSnipets then the maximum
    * amount of TimeSnippet-Cells
    * 
    * @param businessDayExportStruct
    *        the {@link BusinessDayVO} this increment
    *        belongs to
    * 
    */
   public void addPlaceHolderForMissingCell(BusinessDayVO businessDayExportStruct) {

      int amountOfEmptyTimeSnippets = businessDayExportStruct.getAmountOfVonBisElements() - timeSnippets.size();
      List<TimeSnippetVOPlaceHolder> timeSnippetPlaceHolders = new ArrayList<>();
      for (int i = 0; i < amountOfEmptyTimeSnippets; i++) {
         timeSnippetPlaceHolders.add(new TimeSnippetVOPlaceHolder());
         timeSnippetPlaceHolders.add(new TimeSnippetVOPlaceHolder());
      }
      this.timeSnippetPlaceHolders = timeSnippetPlaceHolders;
   }

   public final String getTotalDurationRep() {
      return NumberFormat.format(this.totalDuration);
   }

   public final List<TimeSnippetVOPlaceHolder> getTimeSnippetPlaceHolders() {
      return this.timeSnippetPlaceHolders;
   }

   public final String getDescription() {
      return this.description != null ? description : "";
   }

   public final String getTicketNumber() {
      return this.ticketNumber;
   }

   public final int getChargeType() {
      return this.chargeType;
   }

   public final List<TimeSnippetVO> getTimeSnippets() {
      return this.timeSnippets;
   }

   public boolean isCharged() {
      return isCharged;
   }

   public final TimeSnippet getCurrentTimeSnippet() {
      return this.currentTimeSnippet;
   }

   /**
    * Returns a new {@link BusinessDayIncrementVO} for the given
    * {@link BusinessDayIncrement}
    * 
    * @param currentBussinessDayIncremental
    * @return a new {@link BusinessDayIncrementVO} for the given
    *         {@link BusinessDayIncrement}
    */
   public static BusinessDayIncrementVO of(BusinessDayIncrement currentBussinessDayIncremental) {
      BusinessDayIncrementVO businessDayIncrementVO = new BusinessDayIncrementVO(currentBussinessDayIncremental);
      return businessDayIncrementVO;
   }
}
