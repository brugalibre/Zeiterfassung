package com.adcubum.timerecording.core.work.businessday.ticketdistribution;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class TicketDistributionEvaluatorTest {
   private static final String PKS_PREFIX = "PKS-";
   private static final String SYRIUS_PREFIX = "SYRIUS-";
   private static final String IGAA_PREFIX = "IGAA-";
   private static final String NAG_PREFIX = "NAG-";

   @Test
   void testEvaluateTicketNrs() {
      // Given
      double expectedPksTicketNrAmountOfHours = 4.0;
      double expectedSyriusTicketNrAmountOfHours = 1.0;
      double expectedIgaaTicketNrAmountOfHours = 1.0;
      double sumTicketNrAmountOfHours = expectedPksTicketNrAmountOfHours + expectedSyriusTicketNrAmountOfHours + expectedSyriusTicketNrAmountOfHours;
      double expectedPksPercentage = ((double) expectedPksTicketNrAmountOfHours / sumTicketNrAmountOfHours) * 100;
      double expectedSyriusPercentage = ((double) expectedSyriusTicketNrAmountOfHours / sumTicketNrAmountOfHours) * 100;
      double expectedIgaaPercentage = ((double) expectedIgaaTicketNrAmountOfHours / sumTicketNrAmountOfHours) * 100;
      List<String> allBookedTickets = List.of(SYRIUS_PREFIX + "123", PKS_PREFIX + "123", PKS_PREFIX + "124", PKS_PREFIX + "1", PKS_PREFIX + "754", IGAA_PREFIX + "98751");
      List<BookedTicketNrDto> bookedTicketNrDtos = createDummyBookedTicketNrDtos(allBookedTickets);
      TicketDistributionEvaluator ticketDistributionEvaluator = new TicketDistributionEvaluator();

      // When
      TicketDistribution ticketDistribution = ticketDistributionEvaluator.evaluateTicketDistribution(bookedTicketNrDtos);

      // Then
      assertThat(ticketDistribution.getPercentageByTicketNr(PKS_PREFIX), is(expectedPksPercentage));
      assertThat(ticketDistribution.getAmountOfHoursByTicketNr(PKS_PREFIX), is(expectedPksTicketNrAmountOfHours));
      assertThat(ticketDistribution.getPercentageByTicketNr(SYRIUS_PREFIX + "123"), is(expectedSyriusPercentage));
      assertThat(ticketDistribution.getAmountOfHoursByTicketNr(SYRIUS_PREFIX + "123"), is(expectedSyriusTicketNrAmountOfHours));
      assertThat(ticketDistribution.getAmountOfHoursByTicketNr(IGAA_PREFIX + "98751"), is(expectedIgaaTicketNrAmountOfHours));
      assertThat(ticketDistribution.getPercentageByTicketNr(IGAA_PREFIX + "98751"), is(expectedIgaaPercentage));
   }

   @Test
   void testEvaluateTicketNrsWithoutNumbers() {

      // Given
      double expectedNagTicketNrAmountOfHours = 5.0;
      double expectedNagTicketNrPercentage = 100.0;
      String test = "test-";
      List<String> allBookedTickets = List.of(NAG_PREFIX + test + "1", NAG_PREFIX + test + "2", NAG_PREFIX + test + "2", NAG_PREFIX + test + "2", NAG_PREFIX + test + "3");
      List<BookedTicketNrDto> bookedTicketNrDtos = createDummyBookedTicketNrDtos(allBookedTickets);
      TicketDistributionEvaluator ticketDistributionEvaluator = new TicketDistributionEvaluator();

      // When
      TicketDistribution ticketDistribution = ticketDistributionEvaluator.evaluateTicketDistribution(bookedTicketNrDtos);

      // Then
      assertThat(ticketDistribution.getAmountOfHoursByTicketNr(NAG_PREFIX + test), is(expectedNagTicketNrAmountOfHours));
      assertThat(ticketDistribution.getPercentageByTicketNr(NAG_PREFIX + test), is(expectedNagTicketNrPercentage));
      assertThat(ticketDistribution.getTicketDistributionsEntries().size(), is(1));
   }

   @Test
   void testEvaluateTicketNrs2() {
      // Given
      double pksTicketNrAmountOfHours = 1.0;
      double syriusTicketNrAmountOfHours = 1.0;
      double igaaTicketNrAmountOfHours = 1.0;
      List<String> allBookedTickets = List.of(SYRIUS_PREFIX + "123", PKS_PREFIX + "754", IGAA_PREFIX + "98751");
      List<BookedTicketNrDto> bookedTicketNrDtos = createDummyBookedTicketNrDtos(allBookedTickets);
      TicketDistributionEvaluator ticketDistributionEvaluator = new TicketDistributionEvaluator();

      // When
      TicketDistribution ticketDistribution = ticketDistributionEvaluator.evaluateTicketDistribution(bookedTicketNrDtos);

      // Then
      assertThat(ticketDistribution.getAmountOfHoursByTicketNr(PKS_PREFIX + "754"), is(pksTicketNrAmountOfHours));
      assertThat(ticketDistribution.getAmountOfHoursByTicketNr(SYRIUS_PREFIX + "123"), is(syriusTicketNrAmountOfHours));
      assertThat(ticketDistribution.getAmountOfHoursByTicketNr(IGAA_PREFIX + "98751"), is(igaaTicketNrAmountOfHours));
   }

   private static List<BookedTicketNrDto> createDummyBookedTicketNrDtos(List<String> allBookedTickets) {
      return allBookedTickets
              .stream()
              .map(ticketNr -> new BookedTicketNrDto(ticketNr, 1))// one hour, so the calculation for percentage is straight forward
              .collect(Collectors.toList());
   }
}