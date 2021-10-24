package com.adcubum.timerecording.app.businessday;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.history.BusinessDayHistoryOverview;
import com.adcubum.timerecording.core.work.businessday.history.compare.BusinessDayHistoryOverviewImpl;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.work.date.Time;

/**
 * Lazy access implementation of the {@link BusinessDayHelper}. This means, this class stores an instance of the
 * current {@link BusinessDay} and the only way to update it, is to call {@link BusinessDayHelperImpl#save(BusinessDay)}
 * 
 * @author dstalder
 *
 */
public class BusinessDayHelperImpl implements BusinessDayHelper {

   private BusinessDayRepository businessDayRepository;
   private BookedBusinessDayDeleter bookedBusinessDayDeleter;
   private BusinessDay businessDay;

   /**
    * Creates a new {@link BusinessDayHelperImpl} for the given {@link BusinessDayRepository}
    * 
    * @param businessDayRepository
    *        the {@link BusinessDayRepository} which provides access to the database
    */
   public BusinessDayHelperImpl(BusinessDayRepository businessDayRepository) {
      this(businessDayRepository, BookedBusinessDayDeleterImpl.of(businessDayRepository));
   }

   /**
    * Visible for testing purpose only!
    * 
    * @param businessDayRepository
    *        the {@link BusinessDayRepository}
    * @param bookedBusinessDayDeleter
    *        the {@link BookedBusinessDayDeleter}
    */
   BusinessDayHelperImpl(BusinessDayRepository businessDayRepository, BookedBusinessDayDeleter bookedBusinessDayDeleter) {
      this.businessDayRepository = requireNonNull(businessDayRepository);
      this.bookedBusinessDayDeleter = requireNonNull(bookedBusinessDayDeleter);
   }

   @Override
   public void addBookedBusinessDayIncrements(List<BusinessDayIncrement> increments) {
      BusinessDay bookedBusinessDay = findBookedBusiness4BookedBusinessDayIncs(increments);
      if (needsANewBookedBusinessDayIncrement(bookedBusinessDay, increments)) {
         bookedBusinessDay = businessDayRepository.createNew(true);
      }
      if (nonNull(bookedBusinessDay)) {
         addAllBusinessDayIncrements(bookedBusinessDay, increments);
      }
      bookedBusinessDayDeleter.cleanUpBookedBusinessDays();
   }

   /*
    * A new booked businessDay is only needed if we don't have one for the current date of the booked increments
    * and if there is at least one booked increment
    */
   private boolean needsANewBookedBusinessDayIncrement(BusinessDay bookedBusinessDay, List<BusinessDayIncrement> increments) {
      return isNull(bookedBusinessDay)
            && findFirstBookedBusinessDayInc(increments)
                  .isPresent();
   }

   private BusinessDay findBookedBusiness4BookedBusinessDayIncs(List<BusinessDayIncrement> increments) {
      return findFirstBookedBusinessDayInc(increments)
            .map(BusinessDayIncrement::getDate)
            .map(businessDayRepository::findBookedBusinessDayByDate)
            .orElse(null);
   }

   private static Optional<BusinessDayIncrement> findFirstBookedBusinessDayInc(List<BusinessDayIncrement> increments) {
      return increments.stream()
            .filter(BusinessDayIncrement::isCharged)
            .findFirst();
   }

   private void addAllBusinessDayIncrements(BusinessDay bookedBusinessDay, List<BusinessDayIncrement> increments) {
      increments.stream()
            .filter(BusinessDayIncrement::isCharged)
            .filter(isNotAlreadyContained(bookedBusinessDay))
            .map(BusinessDayHelperImpl::buildBusinessDayIncrementAdd)
            .forEach(bookedBusinessDay::addBusinessIncrement);
      bookedBusinessDay.flagBusinessDayAsCharged();
      businessDayRepository.save(bookedBusinessDay);
   }

   /*
    * Checks if the given booked {@link BusinessDayIncrement}, which belongs to the active and non booked BusinessDay, was not already
    * added to the booked {@link BusinessDay} earlier.
    * This is the case, if we add and book a {@link BusinessDayIncrement} which leads to a new booked {@link BusinessDay} with one {@link BusinessDayIncrement}.
    * Later we add a 2nd {@link BusinessDayIncrement} to the current {@link BusinessDay} and book it as well.
    * Now the current, non booked / active {@link BusinessDay} contains 2 booked {@link BusinessDayIncrement} of which one was already added to the booked BusinessDay
    * and another one, which we still have to add. The first one must not be added again!
    */
   private static Predicate<BusinessDayIncrement> isNotAlreadyContained(BusinessDay bookedBusinessDay) {
      return freshBookedBDayIncrement -> bookedBusinessDay.getIncrements().isEmpty()
            || bookedBusinessDay.getIncrements()
                  .stream()
                  .noneMatch(alreadyPersistentBookedBDayInc -> alreadyPersistentBookedBDayInc.equals(freshBookedBDayIncrement));
   }

   private static BusinessDayIncrementAdd buildBusinessDayIncrementAdd(BusinessDayIncrement businessDayIncrement) {
      return new BusinessDayIncrementAddBuilder()
            .from(businessDayIncrement)
            .build();
   }

   @Override
   public BusinessDay getBusinessDay() {
      return businessDay;
   }

   @Override
   public BusinessDayHistoryOverview getBusinessDayHistoryOverview(Time lowerBounds, Time upperBounds) {
      List<BusinessDay> bookedBussinessDaysWithinRange = businessDayRepository.findBookedBussinessDaysWithinRange(lowerBounds, upperBounds);
      return BusinessDayHistoryOverviewImpl.of(lowerBounds, upperBounds, bookedBussinessDaysWithinRange);
   }

   @Override
   public BusinessDay save(BusinessDay newBusinessDay) {
      return saveAndUpdate(newBusinessDay);
   }

   private BusinessDay saveAndUpdate(BusinessDay newBusinessDay) {
      this.businessDay = businessDayRepository.save(newBusinessDay);
      return businessDay;
   }

   @Override
   public void createNew() {
      loadExistingOrCreateNew();
   }

   @Override
   public BusinessDay loadExistingOrCreateNew() {
      this.businessDay = businessDayRepository.findFirstOrCreateNew();
      return businessDay;
   }

   @Override
   public void deleteAll(boolean isBooked) {
      businessDayRepository.deleteAll(isBooked);
   }
}
