/**
 * 
 */
package com.adcubum.timerecording.model.businessday;

import static com.adcubum.timerecording.core.work.businessday.ValueTypes.BEGIN;
import static com.adcubum.timerecording.core.work.businessday.ValueTypes.END;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;

/**
 * The {@link TimeSnippetDto} is used as a dto for a {@link TimeSnippet}
 * 
 * @author dstalder
 *
 */
public class TimeSnippetDto {
   private UUID id;
   private String beginTimeStampRepresentation;
   private String endTimeStampRepresentation;
   private String durationRep;

   public TimeSnippetDto() {
      // empty
   }

   protected TimeSnippetDto(TimeSnippet timeSnippet) {
      this.id = requireNonNull(timeSnippet.getId());
      this.beginTimeStampRepresentation = requireNonNull(timeSnippet.getBeginTimeStampRep());
      if (nonNull(timeSnippet.getEndTimeStamp())) {
         this.endTimeStampRepresentation = timeSnippet.getEndTimeStampRep();
      }
      this.durationRep = timeSnippet.getDurationRep();
   }

   public String getDurationRep() {
      return durationRep;
   }

   public String getEndTimeStampRepresentation() {
      return endTimeStampRepresentation;
   }

   public String getBeginTimeStampRepresentation() {
      return beginTimeStampRepresentation;
   }

   public UUID getId() {
      return id;
   }

   @Override
   public String toString() {
      return "TimeSnippetDto [id=" + id + ", beginTimeStampRepresentation=" + beginTimeStampRepresentation + ", endTimeStampRepresentation="
            + endTimeStampRepresentation + ", durationRep=" + durationRep + "]";
   }

   /**
    * Compares both {@link TimeSnippetDto} and returns a list of {@link ValueTypes} which has changed
    * 
    * @param changedBusinessDayIncrementDto
    */
   public List<ValueTypes> compareAndEvalChangedValues(TimeSnippetDto otherimeSnippetDto) {
      List<ValueTypes> changedAttrs = new ArrayList<>();
      if (!beginTimeStampRepresentation.equals(otherimeSnippetDto.beginTimeStampRepresentation)) {
         changedAttrs.add(BEGIN);
      }
      if (!endTimeStampRepresentation.equals(otherimeSnippetDto.endTimeStampRepresentation)) {
         changedAttrs.add(END);
      }
      if (!durationRep.equals(otherimeSnippetDto.durationRep)) {
         changedAttrs.add(ValueTypes.AMOUNT_OF_TIME);
      }
      return changedAttrs;
   }

   /**
    * @param valueType
    *        the given {@link ValueTypes}
    * @return the value of this {@link TimeSnippetDto} according to the given {@link ValueTypes} or <code>null</code> if there is no value
    *         for the given ValueTypes
    */
   public Object getChangedValue(ValueTypes valueType) {
      switch (valueType) {
         case BEGIN:
            return beginTimeStampRepresentation;
         case END:
            return endTimeStampRepresentation;
         case AMOUNT_OF_TIME:
            return durationRep;
         default:
            break;
      }
      return null;
   }
}
