package com.adcubum.timerecording.core.work.businessday.history.compare;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.history.BusinessDayHistory;
import com.adcubum.timerecording.core.work.businessday.history.BusinessDayHistoryOverview;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeUtil;

public class BusinessDayHistoryOverviewImpl implements BusinessDayHistoryOverview {

   private List<BusinessDayHistory> businessDayHistoryEntries;

   private BusinessDayHistoryOverviewImpl(Time lowerBounds, Time upperBounds, List<BusinessDay> businessDaysHistory) {
      buildBusinessDayHistroyEntries(businessDaysHistory);
      addEmptyBusinessDayHistoryEntryIfNeeded(lowerBounds, upperBounds);
      Collections.sort(businessDayHistoryEntries, new BusinessDayHistoryComparator());
   }

   private void buildBusinessDayHistroyEntries(List<BusinessDay> businessDaysHistory) {
      this.businessDayHistoryEntries = requireNonNull(businessDaysHistory)
            .stream()
            .map(BusinessDayHistoryImpl::of)
            .collect(Collectors.toList());
   }

   private void addEmptyBusinessDayHistoryEntryIfNeeded(Time lowerBounds, Time upperBounds) {
      List<BusinessDayHistory> buildEmptyBusinessDayHistory = buildEmptyBusinessDayHistory(lowerBounds, upperBounds);
      Collections.sort(buildEmptyBusinessDayHistory, new BusinessDayHistoryComparator());
      for (BusinessDayHistory emptyBusinessDayHistory : buildEmptyBusinessDayHistory) {
         Optional<BusinessDayHistory> businessDayHistoryOpt4Date = findBusinessDayHistoryOpt4Date(emptyBusinessDayHistory.getLocalDate());
         if (!businessDayHistoryOpt4Date.isPresent()) {
            businessDayHistoryEntries.add(emptyBusinessDayHistory);
         }
      }
   }

   private Optional<BusinessDayHistory> findBusinessDayHistoryOpt4Date(LocalDate date) {
      return businessDayHistoryEntries
            .stream()
            .filter(isDateEqual(date))
            .findFirst();
   }

   private Predicate<BusinessDayHistory> isDateEqual(LocalDate date) {
      return businessDayHistoryEntry -> {
         LocalDate histDate = businessDayHistoryEntry.getLocalDate();
         return histDate.getDayOfMonth() == date.getDayOfMonth()
               && histDate.getMonth() == date.getMonth()
               && histDate.getYear() == date.getYear();
      };
   }

   private static List<BusinessDayHistory> buildEmptyBusinessDayHistory(Time lowerBounds, Time upperBounds) {
      return TimeUtil.getDatesInBetween(lowerBounds, upperBounds)
            .stream()
            .map(BusinessDayHistoryImpl::of)
            .collect(Collectors.toList());
   }

   @Override
   public List<BusinessDayHistory> getBusinessDayHistoryEntries() {
      return Collections.unmodifiableList(businessDayHistoryEntries);
   }

   public static BusinessDayHistoryOverviewImpl of(Time lowerBounds, Time upperBounds, List<BusinessDay> businessDayHistory) {
      return new BusinessDayHistoryOverviewImpl(lowerBounds, upperBounds, businessDayHistory);
   }
}
