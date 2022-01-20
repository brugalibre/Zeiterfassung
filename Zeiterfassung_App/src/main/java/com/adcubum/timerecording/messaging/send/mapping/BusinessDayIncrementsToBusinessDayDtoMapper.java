package com.adcubum.timerecording.messaging.send.mapping;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.messaging.api.model.BusinessDayDto;
import com.adcubum.timerecording.messaging.api.model.BusinessDayDto.BusinessDayDtoBuilder;
import com.adcubum.timerecording.messaging.api.model.BusinessDayIncrementDto;
import com.adcubum.timerecording.messaging.api.model.TimeSnippetDto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.adcubum.timerecording.messaging.api.model.BusinessDayIncrementDto.BusinessDayIncrementDtoBuilder;

public class BusinessDayIncrementsToBusinessDayDtoMapper {
   private BusinessDayIncrementsToBusinessDayDtoMapper() {
      // private
   }

   /**
    * Maps the given {@link BusinessDayIncrement}s to a {@link BusinessDayDto}
    * Note, that only booked {@link BusinessDayIncrement}s are considered
    *
    * @param bookedBusinessDayIncrements the {@link BusinessDayIncrement}s to map
    * @return a {@link BusinessDayDto}
    */
   public static BusinessDayDto mapToBusinessDayDto(List<BusinessDayIncrement> bookedBusinessDayIncrements) {
      return bookedBusinessDayIncrements.stream()
              .filter(BusinessDayIncrement::isBooked)
              .map(toBusinessIncrementDto())
              .collect(Collectors.collectingAndThen(Collectors.toList(), buildBusinessDayDto()));
   }

   private static Function<BusinessDayIncrement, BusinessDayIncrementDto> toBusinessIncrementDto() {
      return businessDayIncrement -> BusinessDayIncrementDtoBuilder.of()
              .withDescription(businessDayIncrement.getDescription())
              .withTicketNr(businessDayIncrement.getTicket().getNr())
              .withTicketActivityCode(String.valueOf(businessDayIncrement.getTicketActivity().getActivityCode()))
              .withTimeSnippetDto(TimeSnippetDto.TimeSnippetDtoBuilder.of()
                      .withBeginTimeStampValue(businessDayIncrement.getCurrentTimeSnippet()
                              .getBeginTimeStamp()
                              .getTime())
                      .withEndTimeStampValue(businessDayIncrement.getCurrentTimeSnippet()
                              .getEndTimeStamp()
                              .getTime())
                      .build())
              .build();
   }

   private static Function<List<BusinessDayIncrementDto>, BusinessDayDto> buildBusinessDayDto() {
      return businessDayIncrementDtos -> {
         BusinessDayDtoBuilder businessDayDtoBuilder = BusinessDayDtoBuilder.of();
         for (BusinessDayIncrementDto businessDayIncrementDto : businessDayIncrementDtos) {
            businessDayDtoBuilder.addBusinessDayIncrementDto(businessDayIncrementDto);
         }
         return businessDayDtoBuilder.build();
      };
   }
}
