package com.myownb3.dominic.ui.app.pages.overview.model.table;

/**
 * Contains the two cell values for the begin and the end time stamp
 * 
 * @author Dominic
 *
 */
public class BeginAndEndCellValue {

   public BeginAndEndCellValue(TimeSnippetCellValue beginCellValue, TimeSnippetCellValue endCellValue) {
      this.beginCellValue = beginCellValue;
      this.endCellValue = endCellValue;
   }

   private TimeSnippetCellValue beginCellValue;
   private TimeSnippetCellValue endCellValue;

   public TimeSnippetCellValue getBeginCellValue() {
      return beginCellValue;
   }

   public TimeSnippetCellValue getEndCellValue() {
      return endCellValue;
   }
}
