package com.adcubum.timerecording.core.businessday.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import com.adcubum.timerecording.core.businessday.entity.BusinessDayEntity;
import com.adcubum.timerecording.work.date.DateTime;

/**
 * The {@link BusinessDayDao} as data-access-object in order to create, retrieve and change {@link BusinessDayEntity}
 *
 * @author dstalder
 *
 */
public interface BusinessDayDao extends CrudRepository<BusinessDayEntity, UUID> {
   static final String SELECT_ALL_FROM_BUSINESS_DAY = "SELECT DISTINCT bd "
         + "FROM BusinessDayEntity bd "
         + "JOIN bd.businessDayIncrementEntities bdIncrements ";

   /**
    * Selects all booked {@link BusinessDayEntity} which are within or equal the given {@link DateTime} bounds
    *
    * @param lowerBounds
    *        the lower bound
    * @param upperBounds
    *        the upper bound
    * @return a list of all selected {@link BusinessDayEntity}
    */
   @Query(SELECT_ALL_FROM_BUSINESS_DAY
         + "WHERE bd.isBooked = true "
         + "AND (bdIncrements.currentTimeSnippetEntity.beginTimestamp >= ?1 "
         + "AND bdIncrements.currentTimeSnippetEntity.beginTimestamp <= ?2)")
   @NonNull
   List<BusinessDayEntity> findAllBookedBusinessDayEntitiesWithinRange(@NonNull Timestamp lowerBounds, @NonNull Timestamp upperBounds);

   /**
    * Selects the id's of all {@link BusinessDayEntity} which are either booked or not booked - depending on the given
    * parameter
    *
    * @param isBooked
    *        <code>true</code> if only booked {@link BusinessDayEntity} should be considered or <code>false</code> if not
    * @return a list of all id's of all {@link BusinessDayEntity} which are either booked or not
    */
   @Query("SELECT bd.id FROM BusinessDayEntity bd "
         + "WHERE bd.isBooked = ?1")
   @NonNull
   List<UUID> findAllBusinessDayIds4BookingStatus(boolean isBooked);
}
