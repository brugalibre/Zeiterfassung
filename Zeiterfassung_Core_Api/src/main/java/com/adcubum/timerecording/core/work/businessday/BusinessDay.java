package com.adcubum.timerecording.core.work.businessday;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.adcubum.timerecording.core.importexport.in.businessday.BusinessDayIncrementImport;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.model.common.DomainModel;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.jira.data.ticket.Ticket;

public interface BusinessDay extends DomainModel {

   /**
    * Resumes the {@link #currentBussinessDayIncremental}
    */
   void resumeLastIncremental();

   /**
    * triggers a manually come or go
    */
   void comeOrGo();

   /**
    * Updates a {@link ComeAndGo} for the given changed values
    * 
    * @param changedComeAndGoValue
    *        the {@link ChangedComeAndGoValue} define the changed values
    * @return a new instance of the {@link ComeAndGoes} of this {@link BusinessDay}
    */
   ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue);

   /**
    * Deletes all {@link ComeAndGo}es which are done
    */
   void clearComeAndGoes();

   /**
    * Flags all {@link ComeAndGo}es of the {@link BusinessDay} as recorded
    */
   void flagComeAndGoesAsRecorded();

   /**
    * Starts a new {@link BusinessDayIncrement}. That means to create a new
    * instance of {@link Time} <br>
    * and forward that to the
    */
   void startNewIncremental();

   /**
    * Stops the current incremental and add the
    * {@link #currentBussinessDayIncremental} to the list with increments. After
    * that, a new incremental is created
    */
   void stopCurrentIncremental();

   /**
    * Removes the {@link BusinessDayIncrement} at the given index. If there is no
    * {@link BusinessDayIncrement} for this index nothing is done
    * 
    * @param index
    *        the given index
    */
   void removeIncrementAtIndex(int index);

   /**
    * Creates and adds a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementAdd}
    * 
    * @param update
    *        the {@link BusinessDayIncrementAdd} which defines the new
    *        {@link BusinessDayIncrement}
    */
   void addBusinessIncrement(BusinessDayIncrementAdd update);

   /**
    * Creates and adds a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementImport}
    * 
    * @param businessDayIncrementImport
    *        the {@link BusinessDayIncrementImport}
    *        which defines the new
    *        {@link BusinessDayIncrement}
    */
   void addBusinessIncrement(BusinessDayIncrementImport businessDayIncrementImport);

   /**
    * According to the given {@link ChangedValue} the corresponding
    * {@link BusinessDayIncrement} evaluated. If there is one then the value is
    * changed
    * 
    * @param changeValue
    *        the param which defines what value is changed
    * @see ValueTypes
    */
   void changeBusinesDayIncrement(ChangedValue changeValue);

   /**
    * Flags this {@link BusinessDay} as charged
    */
   void flagBusinessDayAsCharged();

   /**
    * Deletes all {@link BusinessDayIncrement} which are already finished
    * Also this deletes al {@link ComeAndGo}es which are done
    */
   void clearFinishedIncrements();

   /**
    * All {@link BusinessDayIncrement}s are checked if they have a dummy-{@link Ticket} set
    * If so, this dummy-Ticket is tried to update. This can be necessary, if the jira-api was not reachable
    * during the creating of a {@link BusinessDayIncrement}
    */
   void refreshDummyTickets();

   /**
    * Returns <code>true</code> if this {@link BusinessDay} has at least one
    * element which is not yed charged. Otherwise returns <code>false</code>
    * 
    * @return <code>true</code> if this {@link BusinessDay} has at least one
    *         element which is not yed charged. Otherwise returns
    *         <code>false</code>
    */
   boolean hasNotChargedElements();

   /**
    * @return <code>true</code> if this {@link BusinessDay} has at least one
    *         element with a description. Otherwise returns <code>false</code>
    */
   boolean hasDescription();

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
    * Verifies if there is any {@link BusinessDayIncrement} which was recorded e.g.
    * during a preceding day
    * 
    * @return <code>true</code> if there is at least one
    *         {@link BusinessDayIncrement} which was recorded on a preceding day or
    *         <code>false</code> if not
    */
   boolean hasElementsFromPrecedentDays();

   /**
    * @return <code>true</code> if the {@link BusinessDay#getCurrentBussinessDayIncremental()} has started and yet unfinished
    *         {@link TimeSnippet}. Otherwise return <code>false</code>
    */
   boolean hasUnfinishedBusinessDayIncrement();

   /**
    * @return <code>true</code> if there is at least one {@link ComeAndGo} entry which is not yet recorded. Otherwise return
    *         <code>false</code>
    */
   boolean hasNotRecordedComeAndGoContent();

   float getTotalDuration();

   /**
    * @return the current active {@link BusinessDayIncrement}
    */
   BusinessDayIncrement getCurrentBussinessDayIncremental();

   /**
    * Returns all {@link BusinessDayIncrement} of this {@link BusinessDay} ordered, so that the order of the {@link BusinessDayIncrement}s
    * depends on the {@link TimeSnippet} respectively depends on the time stamp of their {@link TimeSnippet}s
    * 
    * @return all {@link BusinessDayIncrement} of this {@link BusinessDay}
    */
   List<BusinessDayIncrement> getIncrements();

   /**
    * Returns the {@link Date} of this {@link BusinessDay}. If this
    * {@link BusinessDay} has no <br>
    * {@link BusinessDayIncrement}, so the {@link #increments} is empty, a new
    * instance of {@link Date} is returned.
    * 
    * @return the {@link Date} of this BussinessDay.
    */
   Date getDate();

   /**
    * Returns a message since when the capturing is active
    * 
    * @return the message since when the capturing is active
    */
   String getCapturingActiveSinceMsg();

   /**
    * @return a message representing the come and go state
    */
   String getComeAndGoMsg();

   /**
    * @return a message since when the capturing is inactive
    */
   String getCapturingInactiveSinceMsg();

   /**
    * @return the {@link ComeAndGoes} of this {@link BusinessDay}
    */
   ComeAndGoes getComeAndGoes();
}
