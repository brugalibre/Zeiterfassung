package com.adcubum.timerecording.core.businessday.common.repository;

import org.springframework.lang.NonNull;

import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.core.repository.ObjectNotFoundException;
import com.adcubum.timerecording.work.date.DateTime;

/**
 * Common repository definition for all repositories dealing with any kind of business-day object
 * 
 * @author dstalder
 *
 * @param <T>
 *        the specific type of a business-day object
 * @param <I>
 *        the type of the id
 */
public interface CommonBusinessDayRepository<T, I> {

   /**
    * Evaluates a specific type of a business-day object for the given id which is managed by this {@link CommonBusinessDayRepository}
    * Note that this method throws an {@link ObjectNotFoundException} if there is entity associated with the given id
    * 
    * @param objectId
    *        the id of the business-day object to load
    * @return the business-day object
    * @throws ObjectNotFoundException
    *         if there is no latest
    */
   @NonNull
   T findById(@NonNull I objectId) throws ObjectNotFoundException;

   /**
    * @return the first found object or create a new one, if there is none.
    */
   @NonNull
   T findFirstOrCreateNew();

   /**
    * Creates and returns a new object
    * 
    * @param isBooked
    *        <code>true</code> if the new created {@link BusinessDayEntity} is a booked one or <code>false</code> if not
    * 
    * @return returns a new object
    */
   @NonNull
   T createNew(boolean isBooked);

   /**
    * Saves the given type of business-day object and return the changed entity
    * 
    * @param businessDayObject
    *        the business-day object to store
    * @return the changed entity
    */
   @NonNull
   T save(@NonNull T businessDayObject);

   /**
    * Removes all persisted businessDayObjects managed by this {@link CommonBusinessDayRepository} which are either booked or not booked
    * 
    * @param isBooked
    *        <code>true</code> if only booked {@link BusinessDayEntity} should be considered or <code>false</code> if not
    */
   void deleteAll(boolean isBooked);

   /**
    * Deletes all BusinessDays which are booked and which took place within or equal the given {@link DateTime} bounds
    * 
    * @param lowerBounds
    *        the lower bound
    * @param upperBounds
    *        the upper bound
    */
   void deleteBookedBusinessDaysWithinRange(@NonNull DateTime lowerBounds, @NonNull DateTime upperBounds);
}
