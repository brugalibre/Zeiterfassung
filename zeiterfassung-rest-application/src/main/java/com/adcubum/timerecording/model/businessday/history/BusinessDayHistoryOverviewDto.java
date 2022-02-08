package com.adcubum.timerecording.model.businessday.history;

import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.timerecording.core.work.businessday.history.BusinessDayHistoryOverview;
import com.adcubum.timerecording.model.businessday.ticketdistribution.TicketDistributionDto;

/**
 * The {@link BusinessDayHistoryOverviewDto} contains a {@link BusinessDayHistoryOverview}s as an overview over a
 * longer period of time
 * 
 * @author dstalder
 *
 */
public class BusinessDayHistoryOverviewDto {

   private List<BusinessDayHistoryDto> businessDayHistoryDtos;
   private TicketDistributionDto ticketDistributionDto;

   private BusinessDayHistoryOverviewDto(BusinessDayHistoryOverview businessDayHistoryOverview) {
      this.ticketDistributionDto = new TicketDistributionDto(businessDayHistoryOverview.getTicketDistribution());
      this.businessDayHistoryDtos = businessDayHistoryOverview.getBusinessDayHistoryEntries()
            .stream()
            .map(BusinessDayHistoryDto::of)
            .collect(Collectors.toList());
   }

   public List<BusinessDayHistoryDto> getBusinessDayHistoryDtos() {
      return businessDayHistoryDtos;
   }

   public TicketDistributionDto getTicketDistributionDto() {
      return ticketDistributionDto;
   }

   public static BusinessDayHistoryOverviewDto of(BusinessDayHistoryOverview businessDayHistoryOverview) {
      return new BusinessDayHistoryOverviewDto(businessDayHistoryOverview);
   }
}
