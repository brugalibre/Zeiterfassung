package com.adcubum.timerecording.core.work.businessday.comeandgo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.model.common.DomainModel;
import com.adcubum.timerecording.work.date.DateTime;

/**
 * The class {@link ComeAndGoes} keeps track of each time a user comes and goes during the business day.
 * 
 * @author DStalder
 *
 */
public interface ComeAndGoes extends DomainModel {

   /**
    * @return a unmodifiable list of it's come's and goes
    */
   List<ComeAndGo> getComeAndGoEntries();

   /**
    * Verifies if there is any {@link ComeAndGo} which was recorded e.g.
    * during a preceding day
    * 
    * @return <code>true</code> if there is at least one
    *         {@link ComeAndGo} which was recorded on a preceding day or
    *         <code>false</code> if not
    */
   boolean hasComeAndGoesFromPrecedentDays();

   /**
    * Return <code>true</code> if there is a {@link ComeAndGo} which is unfinished, that means which has a begin time stamp but no end time
    * stamp. Otherwise return <code>false</code>
    * 
    * @return <code>true</code> if there is a {@link ComeAndGo} which is unfinished or <code>false</code> if not
    */
   boolean hasUnfinishedComeAndGo();

   /**
    * @return <code>true</code> if there is at least one finished {@link ComeAndGo} entry which is not yet recorded. Otherwise return
    *         <code>false</code>
    */
   boolean hasNotRecordedComeAndGoContent();

   /**
    * @return an {@link Optional} of the current and therefore unfinished {@link ComeAndGo} if there is any or {@link Optional#empty()}
    *         if there is none
    */
   Optional<ComeAndGo> getCurrentComeAndGo();

   /**
    * Return an {@link Optional} of a {@link ComeAndGo} with the given id or a {@link Optional#empty()} if there doesn't exist any
    * with the given id
    * 
    * @param id
    *        the id of the desired {@link ComeAndGo}
    * @return the {@link ComeAndGo} for the given Id
    */
   Optional<ComeAndGo> getComeAndGo4Id(UUID id);

   /**
    * Removes all {@link ComeAndGoes} and return a new instance
    * This instance remains unchanged!
    * 
    * @return a new {@link ComeAndGoes} instance for which all done elements are removed
    */
   ComeAndGoes clearDoneComeAndGoes();

   /**
    * Flags all {@link ComeAndGo}es of this {@link ComeAndGoes} as recorded and return a new instance
    * This instance remains unchanged!
    * 
    * @return a new {@link ComeAndGoes} instance for which all done elements are flaged as recorded
    */
   ComeAndGoes flagComeAndGoesAsRecorded();

   /**
    * Triggers a new come or go and return a new instance
    * This instance remains unchanged!
    * 
    * @return a new instance with a new started or ended come or go
    */
   ComeAndGoes comeOrGo();

   /**
    * Either triggers a manually come or a go for this {@link ComeAndGoes} with the given {@link DateTime}
    * This instance remains unchanged!
    * 
    * @return a new instance with a new started or ended come or go
    */
   ComeAndGoes comeOrGo(DateTime time);

   /**
    * Updates a given {@link ComeAndGo} for the given changed values
    * Since we can change either the come or the go we will always find a corresponding {@link ComeAndGo} for the given
    * {@link ChangedComeAndGoValue}. If not, nothing will be changed
    * 
    * @param changedComeAndGoValue
    *        the {@link ChangedComeAndGoValue} define the changed values
    */
   ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue);
}
