/**
 * 
 */
package com.adcubum.timerecording.model.businessday;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;

/**
 * The {@link BusinessDayIncrementVO} is used whenever a we need
 * {@link BusinessDayIncrement} for displaying or exporting. The {@link BusinessDayIncrementVO} is read only
 * 
 * @author Dominic
 *
 */
public class AddNewBusinessDayIncrementDto {

   private BusinessDayIncrementDto businessDayIncrementDto;
   private boolean isSubmitButtonDisabled;

   /**
    * Creates a new {@link AddNewBusinessDayIncrementDto} for the given {@link BusinessDayIncrementD}
    * 
    * @param businessDayIncrement
    */
   public AddNewBusinessDayIncrementDto(BusinessDayIncrementDto businessDayIncrementDto) {
      this.businessDayIncrementDto = businessDayIncrementDto;
      this.isSubmitButtonDisabled = evalsSubmitButtonDisabled(businessDayIncrementDto);
   }

   public AddNewBusinessDayIncrementDto() {
      this(new BusinessDayIncrementDto());
   }

   public BusinessDayIncrementDto getBusinessDayIncrementDto() {
      return businessDayIncrementDto;
   }

   public boolean isSubmitButtonDisabled() {
      return isSubmitButtonDisabled;
   }

   public boolean getIsStartedAndStopped() {
      return nonNull(businessDayIncrementDto.getTimeSnippetDto().getBeginTimeStampRepresentation())
            && nonNull(businessDayIncrementDto.getTimeSnippetDto().getEndTimeStampRepresentation());
   }

   private static boolean evalsSubmitButtonDisabled(BusinessDayIncrementDto businessDayIncrementDto) {
      return isNull(businessDayIncrementDto.getTicketDto().getTicketNr())
            || isNull(businessDayIncrementDto.getTimeSnippetDto().getBeginTimeStampRepresentation())
            || isNull(businessDayIncrementDto.getServiceCodeDto())
            || isNull(businessDayIncrementDto.getTimeSnippetDto().getEndTimeStampRepresentation());
   }
}
