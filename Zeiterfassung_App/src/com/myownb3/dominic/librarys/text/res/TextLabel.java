/**
 * 
 */
package com.myownb3.dominic.librarys.text.res;

/**
 * @author Dominic
 * 
 */
public class TextLabel {
   private TextLabel() {
      // private 
   }

   public static final String SHOW_WORKING_HOURS = "Zeige Arbeitsstunden";
   public static final String APPLICATION_TITLE = "Adcubum Zeiterfassung";
   public static final String EXIT_APP = "App beenden";
   public static final String CLOSE_POPUP_MENU = "Menu schliessen";

   public static final String FINISH_BUTTON_TEXT = "Fertig";
   public static final String CANCEL_BUTTON_TEXT = "Abbrechen";
   public static final String CANCEL_BUTTON_TOOLTIP_TEXT = "Bricht die Eingabe ab und setzt Aufzeichnung fort";
   public static final String ABORT_BUTTON_TEXT = "Alles verwerfen";
   public static final String ABORT_BUTTON_TOOLTIP_TEXT = "Verwirft die gerade gestoppte Aufzeichnung";
   public static final String AMOUNT_OF_HOURS_LABEL = "Anzahl Stunden";
   public static final String TOTAL_AMOUNT_OF_HOURS_LABEL = "Gesamt Anzahl Stunden: ";
   public static final String TICKET_NUMBER_LABEL = "Ticket-Nr.";
   public static final String BOOK_TYPE_LABEL = "Leistungsart";
   public static final String IS_CHARGED_LABEL = "Abgebucht";
   public static final String DESCRIPTION_LABEL = "Beschreibung";
   public static final String VON_LABEL = "Von";
   public static final String BIS_LABEL = "Bis";

   public static final String EXCEPTION_DIALOG_MESSAGE = "Ein unerwarteter Fehler ist aufgetreten:";
   public static final String EXCEPTION_DIALOG_TITLE = "Hoppla!";

   // Change row
   public static final String DELETE_ROW = "Reihe Löschen";
   public static final String CHANGE_DESCRIPTION = "Beschreibung hinzufügen";

   public static final String NUMMER_LABEL = "Nr.";
   public static final String CLEAR_LABEL = "Alles löschen";
   public static final String NEIN = "Nein";
   public static final String JA = "Ja";
   public static final String WARN_TITLE = "Achtung!";
   public static final String CHARGE_LABEL = "Abbuchen";
   public static final String EXPORT_LABEL = "Exportieren";
   public static final String SUCESSFULLY_EXPORTED = "Erfolgreich exportiert";
   public static final String EXPORT_FAILED_TITLE = "Export fehlgeschlagen:";

   // Import of recordings
   public static final String SHOW_IMPORT_DIALOG_MENU_ITEM = "Vorhandene Aufzeichnungen importieren";
   public static final String SHOW_IMPORT_DIALOG_TITLE = ".csv Datei mit bestehenden Aufzeichnungen ausäwhlen";
   public static final String IMPORT_SUCESSFULL = "Import erfolgreich";
   public static final String IMPORT_NOT_SUCESSFULL_TITLE = "Import fehlgeschlagen!";
   public static final String IMPORT_NOT_SUCESSFULL_MSG = "Prüfe das log sowie die zu importierende Datei";

   // Ticket Stuff
   public static final String TICKET = "Ticket";
   public static final String SUCCESSFULLY_BOOKED_TEXT = "Erfolgreich abgebucht";
   public static final Object BOOKING_FAILED_TEXT = "Abbuchen fehlgeschlagen, logs prüfen!";
   public static final String PARTIAL_SUCCESSFULLY_BOOKED_TEXT = "Abbuchung nur teilweise erfolgreich!";

   public static final String NOT_BOOKABLE_TICKETS_FOUND_TEXT = "Projektnummern und Leistungsart von Ticket '%s1' prüfen";
   public static final String NOT_BOOKABLE_TICKET_TOOLTIP = "Ticket nicht abbuchbar, Projektnummer und Leistungsart prüfen";
   public static final String TICKET_DOES_NOT_EXIST_TOOLTIP = "Ticket existiert in Jira nicht";

   public static final String BOOKED = "Abgebucht";
   public static final String NO = "Nein";
   public static final String YES = "Ja";
   public static final String BOOKING_RUNNING = "Abbuchen läuft, lehn zurück und geniess die Show!";
   public static final String REFRESH_TICKET_BACKLOG = "Sprint-Tickets aktualisieren";
   public static final String REFRESH_TICKET_BACKLOG_DONE = "Sprint-Tickets aktualisiert";
   public static final String REFRESH_TICKET_BACKLOG_NOT_POSSIBLE = "Aktualisierung der Sprint-Tickets nicht möglich; Konfiguration unvollständig";
   public static final String REFRESH_TICKET_BACKLOG_FAILED = "Fehler bei der Aktualisierung der Sprint-Ticket";

   public static final String SETTINGS_ROUND_SAVED = "Einstellungen wurden gespeichert";
   public static final String SETTINGS_ROUND = "Rundungseinstellungen";
   public static final String SETTINGS_ROUND_1 = "1 Minute";
   public static final String SETTINGS_ROUND_5 = "5 Minuten";
   public static final String SETTINGS_ROUND_10 = "10 Minuten";
   public static final String SETTINGS_ROUND_15 = "15 Minuten";

   public static final String CAPTURING_INACTIVE = "Zeiterfassung inaktiv";
   public static final String CAPTURING_INCTIVE_SINCE = "Zeiterfassung inaktiv seit:";
   public static final String CAPTURING_ACTIVE_SINCE = "Zeiterfassung aktiv seit:";

   public static final String START_NOT_POSSIBLE_PRECEDENT_ELEMENTS =
         "Es sind noch Elemente von vergangenen Tagen vorhanden! Diese zuerst abbuchen bzw. löschen";
   public static final String START_NOT_POSSIBLE_PRECEDENT_ELEMENTS_TITLE = "Start einer neuen Aufzeichnung nicht möglich!";

   // Login Stuff
   public static final String LOGIN_LABEL = "Login";
   public static final String USERNAME_FIELD_PROMPT = "ADUser";
   public static final String USER_PW_FIELD_PROMPT = "AD-Password";
   public static final String LOGIN_FAILED_LABEL = "Login fehlgeschlagen!";
}
