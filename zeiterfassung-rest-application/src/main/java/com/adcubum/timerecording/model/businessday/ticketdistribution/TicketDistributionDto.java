package com.adcubum.timerecording.model.businessday.ticketdistribution;

import com.adcubum.timerecording.core.work.businessday.ticketdistribution.TicketDistribution;
import com.adcubum.timerecording.core.work.businessday.ticketdistribution.TicketDistributionEntry;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TicketDistributionDto {
   private List<TicketDistributionEntryDto> ticketDistributionDtoEntries;

   /**
    * Creates a new {@link TicketDistributionDto} from the given {@link TicketDistribution} object
    *
    * @param ticketDistribution
    */
   public TicketDistributionDto(TicketDistribution ticketDistribution) {
      this.ticketDistributionDtoEntries = ticketDistribution.getTicketDistributionsEntries()
              .stream()
              .map(toTicketDistributionDto(ticketDistribution))
              .collect(Collectors.toList());
   }

   private static Function<TicketDistributionEntry, TicketDistributionEntryDto> toTicketDistributionDto(TicketDistribution ticketDistribution) {
      return ticketDistributionEntry -> {
         double percentageByTicketNr = ticketDistribution.getPercentageByTicketNr(ticketDistributionEntry.getTicketNr());
         return new TicketDistributionEntryDto(ticketDistributionEntry.getTicketNr(), percentageByTicketNr);
      };
   }

   public List<TicketDistributionEntryDto> getTicketDistributionDtoEntries() {
      return ticketDistributionDtoEntries;
   }
}
