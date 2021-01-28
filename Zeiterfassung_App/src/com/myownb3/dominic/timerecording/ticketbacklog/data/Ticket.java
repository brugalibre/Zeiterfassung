package com.myownb3.dominic.timerecording.ticketbacklog.data;

import static com.myownb3.dominic.timerecording.settings.common.Const.USER_NAME_VALUE_KEY;
import static java.util.Objects.nonNull;

import com.myownb3.dominic.timerecording.settings.Settings;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssue;

public class Ticket {

   public static final Ticket SCRUM_ARBEITEN_TICKET = new Ticket("INTA-155", "Buchungsticket: Scrum-Aufwände", "536003", "Scrum Aufwände");
   public static final Ticket SCRUM_TICKET = new Ticket("INTA-151", "Buchungsticket: Stammprojekt - Scrummaster Arbeiten", "950000", "Stammprojekt");
   public static final Ticket MEETING_TICKET = new Ticket("INTA-147", "Buchungsticket: Stammprojekt - Meeting", "950000", "Stammprojekt");

   private String nr;
   private String title;
   private String projektNr;
   private String projektDesc;
   private boolean isCurrentUserAssigned;
   private boolean isSprintTicket;

   private Ticket(String key, String title, String projektNr, String projektDesc) {
      this(key, title, null, projektNr, projektDesc, false);
   }

   private Ticket(String key, String title, String assignee, String projektNr, String projektDesc, boolean isSprintTicket) {
      this.nr = key;
      this.isCurrentUserAssigned = isCurrentUserAssigned(assignee);
      this.title = title;
      this.projektNr = projektNr;
      this.projektDesc = projektDesc;
      this.isSprintTicket = isSprintTicket;
   }

   public static Ticket of(JiraIssue issue) {
      ProjektnummerParser parser = new ProjektnummerParser();
      String projektDesc = parser.getProjektDesc(issue.getProjektNrAndBez());
      String projektNr = parser.getProjektNr(issue.getProjektNrAndBez());
      return new Ticket(issue.getKey(), issue.getTitle(), issue.getAssignee(), projektNr, projektDesc, true);
   }

   private boolean isCurrentUserAssigned(String assignee) {
      String currentUserName = Settings.INSTANCE.getSettingsValue(USER_NAME_VALUE_KEY);
      return nonNull(currentUserName) && currentUserName.equalsIgnoreCase(assignee);
   }

   /**
    * @return the number of the {@link Ticket}
    */
   public String getNr() {
      return nr;
   }

   /**
    * @return the title of the {@link Ticket}
    */
   public String getTitle() {
      return title;
   }

   public String getProjektDesc() {
      return projektDesc;
   }

   public String getProjektNr() {
      return projektNr;
   }

   /**
    * @return <code>true</code> if the Ticket is part of a sprint or <code>false</code> if it's a common ticket (like the INTA's)
    */
   public boolean isSprintTicket() {
      return isSprintTicket;
   }

   public boolean isCurrentUserAssigned() {
      return isCurrentUserAssigned;
   }

   /**
    * Returns a string starting with the ticket-nr and following by ('description-of-this-ticket')
    * 
    * @param ticket
    *        the ticket
    * @return a representation
    */
   public String getTicketRep() {
      return this.getNr() + " (" + this.getTitle() + ")";
   }

   @Override
   public String toString() {
      return "Ticket-Nr = " + nr + " (" + title + "), projekt-nr = " + projektNr + " (" + projektDesc + ")";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((nr == null) ? 0 : nr.hashCode());
      result = prime * result + ((title == null) ? 0 : title.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Ticket other = (Ticket) obj;
      if (nr == null) {
         if (other.nr != null)
            return false;
      } else if (!nr.equals(other.nr))
         return false;
      if (title == null) {
         if (other.title != null)
            return false;
      } else if (!title.equals(other.title))
         return false;
      return true;
   }
}
