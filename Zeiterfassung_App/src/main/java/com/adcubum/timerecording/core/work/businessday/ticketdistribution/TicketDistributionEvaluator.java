package com.adcubum.timerecording.core.work.businessday.ticketdistribution;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import org.apache.commons.text.diff.CommandVisitor;
import org.apache.commons.text.diff.StringsComparator;

import java.util.*;
import java.util.stream.Collectors;

public class TicketDistributionEvaluator {

   public TicketDistribution evaluateTicketDistributionFromIncrements(List<BusinessDayIncrement> businessDayIncrements) {
      return evaluateTicketDistribution(businessDayIncrements.stream()
              .map(businessDayIncrement -> new BookedTicketNrDto(businessDayIncrement.getTicket().getNr(), businessDayIncrement.getTotalDuration()))
              .collect(Collectors.toList()));
   }

   public TicketDistribution evaluateTicketDistribution(List<BookedTicketNrDto> bookedTicketNrDtos) {
      List<String> bookedTicketNrs = bookedTicketNrDtos.stream()
              .map(BookedTicketNrDto::getTicketNr)
              .collect(Collectors.toList());
      List<String> evaluatedTicketNrs = evaluateMatchedTicketNrs(bookedTicketNrs);
      return createTicketDistribution(bookedTicketNrDtos, evaluatedTicketNrs);
   }

   private static TicketDistribution createTicketDistribution(List<BookedTicketNrDto> bookedTicketNrDtos, List<String> evaluatedTicketNrs) {
      Map<String, Double> evaluatedTicketNr2FrequencyMap = new HashMap<>();
      for (String evaluatedTicketNr : evaluatedTicketNrs) {
         double amountOfHours = 0;
         for (BookedTicketNrDto bookedTicketNrDto : bookedTicketNrDtos) {
            CharacterCommandVisitor visitor = new CharacterCommandVisitor();
            StringsComparator ticketNrComparator = new StringsComparator(evaluatedTicketNr, bookedTicketNrDto.getTicketNr());
            ticketNrComparator.getScript().visit(visitor);
            if (!visitor.hasNoKeptCharactersAtBegin()) {
               amountOfHours = amountOfHours + bookedTicketNrDto.getAmountOfHours();
            }
         }
         evaluatedTicketNr2FrequencyMap.put(evaluatedTicketNr, amountOfHours);
      }
      return evaluatedTicketNr2FrequencyMap.entrySet()
              .stream()
              .map(entry -> new TicketDistributionEntryImpl(entry.getKey(), entry.getValue()))
              .collect(Collectors.collectingAndThen(Collectors.toList(), TicketDistributionImpl::new));
   }

   private static List<String> evaluateMatchedTicketNrs(List<String> allBookedTickets) {
      Set<String> prevMatchedStringTickets = new HashSet<>();
      Set<String> currentMatchedStringTickets = new HashSet<>(allBookedTickets);
      boolean isDone = false;
      while (!isDone) {
         currentMatchedStringTickets = evalMatchedStringTickets(new ArrayList<>(currentMatchedStringTickets));
         isDone = prevMatchedStringTickets.size() == currentMatchedStringTickets.size();
         prevMatchedStringTickets = currentMatchedStringTickets;
      }
      return new ArrayList<>(currentMatchedStringTickets);
   }

   private static Set<String> evalMatchedStringTickets(List<String> ticketNrs) {
      Collections.sort(ticketNrs);
      if (ticketNrs.isEmpty()) {
         return Collections.emptySet();
      }
      Iterator<String> iterator = ticketNrs.iterator();
      Set<String> matchedStringTickets = new HashSet<>();
      String currentTicketNr = iterator.next();
      matchedStringTickets.add(currentTicketNr);
      while (iterator.hasNext()) {
         String nextTicketNr = null;
         if (iterator.hasNext()) {
            nextTicketNr = iterator.next();
            CharacterCommandVisitor visitor = new CharacterCommandVisitor();
            StringsComparator ticketNrComparator = new StringsComparator(currentTicketNr, nextTicketNr);
            ticketNrComparator.getScript().visit(visitor);
            if (visitor.hasNoKeptCharactersAtBegin()) {
               matchedStringTickets.add(nextTicketNr);
            } else {
               matchedStringTickets.add(visitor.getKeptCharactersAtBegin());
            }
         }
         currentTicketNr = nextTicketNr;
      }
      return matchedStringTickets;
   }

   private static class CharacterCommandVisitor implements CommandVisitor<Character> {
      private StringBuilder keptCharactersAtBegin;
      private boolean canAddKeptCharacters;

      public CharacterCommandVisitor() {
         this.keptCharactersAtBegin = new StringBuilder();
         this.canAddKeptCharacters = true;
      }

      @Override
      public void visitInsertCommand(Character character) {
         this.canAddKeptCharacters = false;
      }

      @Override
      public void visitKeepCommand(Character character) {
         if (canAddKeptCharacters) {
            keptCharactersAtBegin.append(character);
         }
      }

      @Override
      public void visitDeleteCommand(Character character) {
         this.canAddKeptCharacters = false;
      }

      public String getKeptCharactersAtBegin() {
         return keptCharactersAtBegin.toString();
      }

      public boolean hasNoKeptCharactersAtBegin() {
         return keptCharactersAtBegin.toString().isEmpty();
      }
   }
}
