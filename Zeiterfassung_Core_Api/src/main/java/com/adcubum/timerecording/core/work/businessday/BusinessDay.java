package com.adcubum.timerecording.core.work.businessday;

import com.adcubum.timerecording.core.importexport.in.businessday.BusinessDayIncrementImport;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.model.common.DomainModel;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.work.date.DateTime;

import java.util.List;
import java.util.UUID;

public interface BusinessDay extends DomainModel {

   /**
    * Resumes the current recording of a {@link TimeSnippet}
    * 
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay resumeLastIncremental();

   /**
    * triggers a manually come or go
    * 
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay comeOrGo();

   /**
    * Updates a {@link ComeAndGo} for the given changed values
    * 
    * @param changedComeAndGoValue
    *        the {@link ChangedComeAndGoValue} define the changed values
    * @return a new instance of the changed {@link BusinessDay} which contains the changed {@link ComeAndGoes}
    */
   BusinessDay changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue);

   /**
    * Deletes all {@link ComeAndGo}es which are done
    * 
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay clearComeAndGoes();

   /**
    * Flags all {@link ComeAndGo}es of the {@link BusinessDay} as recorded
    * 
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay flagComeAndGoesAsRecorded();

   /**
    * Starts a new {@link BusinessDayIncrement}. That means to create a new
    * instance of {@link DateTime} <br>
    * and forward that to the
    * 
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay startNewIncremental();

   /**
    * Stops the current incremental and creates and add a new {@link BusinessDayIncrement} to the list with increments. After
    * that, a new incremental is created
    * 
    * @return a new and changed instance of this {@link BusinessDay}
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay stopCurrentIncremental();

   /**
    * Removes the {@link BusinessDayIncrement} for the given id. If there is no
    * {@link BusinessDayIncrement} for this id, nothing is done
    * 
    * @param id
    *        the given id
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay removeIncrement4Id(UUID id);

   /**
    * Creates and adds a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementAdd}
    * 
    * @param update
    *        the {@link BusinessDayIncrementAdd} which defines the new
    *        {@link BusinessDayIncrement}
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay addBusinessIncrement(BusinessDayIncrementAdd update);

   /**
    * Creates and adds a new {@link BusinessDayIncrement} for the given
    * {@link BusinessDayIncrementImport}
    * 
    * @param businessDayIncrementImport
    *        the {@link BusinessDayIncrementImport}
    *        which defines a new {@link BusinessDayIncrement} to add
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay addBusinessIncrement(BusinessDayIncrementImport businessDayIncrementImport);

   /**
    * According to the given {@link ChangedValue} the corresponding
    * {@link BusinessDayIncrement} evaluated. If there is one then the value is
    * changed
    * 
    * @param changeValue
    *        the param which defines what value is changed
    * @see ValueTypes
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay changeBusinesDayIncrement(ChangedValue changeValue);

   /**
    * Flags this {@link BusinessDay} and all its {@link BusinessDayIncrement }as booked
    * 
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay flagBusinessDayAsBooked();

   /**
    * Flags a specific {@link BusinessDayIncrement} as booked
    *
    * @param id the Id of the {@link BusinessDayIncrement} to flag as charged
    * @return a changed copy of this {@link BusinessDay}
    */
   BusinessDay flagBusinessDayIncrementAsBooked(UUID id);

   /**
    * Flags a specific {@link BusinessDayIncrement} as sent
    *
    * @param id the Id of the {@link BusinessDayIncrement} to flag as charged
    * @return a changed copy of this {@link BusinessDay}
    */
   BusinessDay flagBusinessDayIncrementAsSent(UUID id);

   /**
    * Deletes all {@link BusinessDayIncrement} which are already finished
    * Also this deletes al {@link ComeAndGo}es which are done
    * 
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay clearFinishedIncrements();

   /**
    * All {@link BusinessDayIncrement}s are checked if they have a dummy-{@link Ticket} set
    * If so, this dummy-Ticket is tried to update. This can be necessary, if the jira-api was not reachable
    * during the creating of a {@link BusinessDayIncrement}
    * 
    * @return a new and changed instance of this {@link BusinessDay}
    */
   BusinessDay refreshDummyTickets();

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
    * @return <code>true</code> if the {@link BusinessDay#getCurrentTimeSnippet()} has started and yet unfinished
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
    * @return the current active and not yet finished {@link TimeSnippet}
    */
   TimeSnippet getCurrentTimeSnippet();

   /**
    * Evaluates a {@link BusinessDayIncrement} for the given id
    *
    * @param id the id to search
    * @return a found {@link BusinessDayIncrement} or throws a {@link com.adcubum.timerecording.core.work.businessday.exception.NoSuchBusinessDayIncrementException}
    * throws {@link com.adcubum.timerecording.core.work.businessday.exception.NoSuchBusinessDayIncrementException}
    */
   BusinessDayIncrement getBusinessDayIncrementById(UUID id);

   /**
    * Returns all {@link BusinessDayIncrement} of this {@link BusinessDay} ordered, so that the order of the {@link BusinessDayIncrement}s
    * depends on the {@link TimeSnippet} respectively depends on the time stamp of their {@link TimeSnippet}s
    * 
    * @return all {@link BusinessDayIncrement} of this {@link BusinessDay}
    */
   List<BusinessDayIncrement> getIncrements();

   /**
    * Returns the {@link DateTime} of this {@link BusinessDay}. If this
    * {@link BusinessDay} has no <br>
    * {@link BusinessDayIncrement}, so the {@link #getIncrements()} is empty, a new
    * instance of {@link DateTime} is returned.
    * 
    * @return the {@link DateTime} of this BusinessDay.
    */
   DateTime getDateTime();

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

   /**
    * Return <code>true</code> if this {@link BusinessDay} is already booked and therefore is read-only or <code>false</code> if not
    * 
    * @return <code>true</code> if this {@link BusinessDay} is already booked and therefore is read-only or <code>false</code> if not
    */
   boolean isBooked();
}
