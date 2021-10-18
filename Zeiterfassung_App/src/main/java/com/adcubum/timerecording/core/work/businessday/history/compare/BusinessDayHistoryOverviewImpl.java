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

   private BusinessDayHistoryOverviewImpl(Time historyBegin, Time historyEnd, List<BusinessDay> businessDaysHistory) {
      buildBusinessDayHistroyEntries(businessDaysHistory);
      addEmptyBusinessDayHistoryEntryIfNeeded(historyBegin, historyEnd);
      Collections.sort(businessDayHistoryEntries, new BusinessDayHistoryComparator());
   }

   private void buildBusinessDayHistroyEntries(List<BusinessDay> businessDaysHistory) {
      this.businessDayHistoryEntries = requireNonNull(businessDaysHistory)
            .stream()
            .map(BusinessDayHistoryImpl::of)
            .collect(Collectors.toList());
   }

   private void addEmptyBusinessDayHistoryEntryIfNeeded(Time historyBegin, Time historyEnd) {
      List<BusinessDayHistory> buildEmptyBusinessDayHistory = buildEmptyBusinessDayHistory(historyBegin, historyEnd);
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

   private static List<BusinessDayHistory> buildEmptyBusinessDayHistory(Time historyBegin, Time historyEnd) {
      return TimeUtil.getDatesInBetween(historyBegin, historyEnd)
            .stream()
            .map(BusinessDayHistoryImpl::of)
            .collect(Collectors.toList());
   }

   @Override
   public List<BusinessDayHistory> getBusinessDayHistoryEntries() {
      return Collections.unmodifiableList(businessDayHistoryEntries);
   }

   /**
    * Creates a new {@link BusinessDayHistoryOverviewImpl} for the given period and for the given booked business-days
    * For each day between the given period (incl. begin & end) there is a {@link BusinessDayHistory}.
    * If there is a booked {@link BusinessDay} for a {@link BusinessDayHistory} the total duration of this {@link BusinessDay} is used
    * for the {@link BusinessDayHistory#getBookedHours()}. If there is no {@link BusinessDay} the amount of booked hours is <code>0</code>
    * 
    * 
    * @param historyBegin
    *        the begin of the history
    * @param historyEnd
    *        the end of the history
    * @param businessDayHistory
    *        the booked {@link BusinessDay}s
    * @return a {@link BusinessDayHistoryOverviewImpl}
    */
   public static BusinessDayHistoryOverviewImpl of(Time historyBegin, Time historyEnd, List<BusinessDay> businessDayHistory) {
      return new BusinessDayHistoryOverviewImpl(historyBegin, historyEnd, businessDayHistory);
   }
}
