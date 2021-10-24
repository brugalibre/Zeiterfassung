package com.adcubum.timerecording.core.work.businessday.comeandgo;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.model.common.DomainModel;
import com.adcubum.timerecording.work.date.DateTime;

/**
 * The class {@link ComeAndGo} keeps track of when a user comes and goes during the business day
 * 
 * @author DStalder
 *
 */
public interface ComeAndGo extends DomainModel {

   /**
    * @return the {@link TimeSnippet} of this {@link ComeAndGo}
    */
   TimeSnippet getComeAndGoTimeStamp();

   /**
    * @return <code>false</code> if this {@link ComeAndGo} has a {@link ComeAndGo#come} as well as a {@link ComeAndGo#go}. Otherwise returns
    *         <code>true</code>
    */
   boolean isNotDone();

   /**
    * @return <code>false</code> if for this {@link ComeAndGo} exists a {@link BusinessDayIncrement} otherwise return <code>true</code>
    */
   boolean isNotRecorded();

   /**
    * Flags this {@link ComeAndGo} as recorded and returns a new, recorded instance
    * This instance remains unchanged!
    * 
    * @return a new instance
    */
   ComeAndGo flagAsRecorded();

   /**
    * Resumes this {@link ComeAndGo} and returns a new, resumed instance
    * This instance remains unchanged!
    * 
    * @return a new instance
    */
   ComeAndGo resume();

   /**
    * Either triggers a come or a go for this {@link ComeAndGoDto} with the given date and returns a new, changed instance
    * This instance remains unchanged!
    * 
    * @return a new instance
    */
   ComeAndGo comeOrGo(DateTime time);
}
