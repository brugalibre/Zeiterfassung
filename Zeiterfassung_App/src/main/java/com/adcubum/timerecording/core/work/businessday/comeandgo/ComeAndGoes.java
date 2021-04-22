package com.adcubum.timerecording.core.work.businessday.comeandgo;

import java.util.List;
import java.util.Optional;

import com.adcubum.timerecording.work.date.Time;

/**
 * The class {@link ComeAndGoes} keeps track of each time a user comes and goes during the business day.
 * 
 * @author DStalder
 *
 */
public interface ComeAndGoes {

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
    * @return an {@link Optional} of the the current and therefore unfinished {@link ComeAndGo} if there is any or {@link Optional#empty()}
    *         if tehre is none
    */
   Optional<ComeAndGo> getCurrentComeAndGo();

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
    * Either triggers a manually come or a go for this {@link ComeAndGoes} with the given {@link Time}
    * This instance remains unchanged!
    * 
    * @return a new instance with a new started or ended come or go
    */
   ComeAndGoes comeOrGo(Time time);
}
