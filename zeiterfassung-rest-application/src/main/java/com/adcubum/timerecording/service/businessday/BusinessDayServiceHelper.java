package com.adcubum.timerecording.service.businessday;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.model.businessday.BusinessDayDto;
import com.adcubum.timerecording.model.businessday.BusinessDayIncrementDto;
import com.adcubum.timerecording.model.businessday.history.BusinessDayHistoryOverviewDto;
import org.springframework.lang.NonNull;

import java.util.UUID;

public interface BusinessDayServiceHelper {

   /**
    * @return the {@link BusinessDayDto} from the {@link TimeRecorder}
    */
   @NonNull
   BusinessDayDto getBusinessDayDto();

   /**
    * @return the {@link BusinessDayHistoryOverviewDto}
    */
   @NonNull
   BusinessDayHistoryOverviewDto getBusinessDayHistoryDto();

   /**
    * Delete the {@link BusinessDayIncrementDto} with the given id
    * 
    * @param id
    *        the id
    * @return a integer representing the http status
    */
   int deleteBusinessDayIncrementDto(UUID id);

   /**
    * Adds the given {@link BusinessDayIncrementDto} to the existing {@link BusinessDayDto}
    * 
    * @param businessDayIncrementDto
    *        the increment to add
    * @return the new created {@link BusinessDayIncrementDto}
    */
   @NonNull
   BusinessDayIncrementDto addBusinessDayIncrement(BusinessDayIncrementDto businessDayIncrementDto);

   /**
    * Changes the given {@link BusinessDayIncrementDto}
    * 
    * @param businessDayIncrementDto
    * @return the changed {@link BusinessDayIncrementDto}
    */
   @NonNull
   BusinessDayIncrementDto changeBusinessDayIncrementDto(BusinessDayIncrementDto businessDayIncrementDto);

   /**
    * Deletes all {@link BusinessDayIncrementDto}
    * 
    * @return a integer representing the http status
    */
   int deleteAllBusinessDayIncrements();
}
