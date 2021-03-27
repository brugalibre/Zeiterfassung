package com.myownb3.dominic.ui.app.inputfield;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.adcubum.timerecording.jira.data.Ticket;
import com.myownb3.dominic.ui.app.styles.Styles;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * This class is a TextField which implements an "autocomplete" functionality, based on a supplied list of entries.
 * In Anlehnung an Caleb Brinkman (coole Typ!)
 * 
 */
public class AutoCompleteTextField extends TextField {
   private static final int MAX_ENTRIES = 10;
   /** The existing autocomplete entries. */
   private Property<ObservableList<Ticket>> keyWordsProperty;
   /** The popup used to select an entry. */
   private ContextMenu entriesPopup;
   private boolean hasTypedAtLeastOnce; // true if the user has at least typed one time. or false, if the ui showed up and nobody typed at all

   /**
    * Default non-arguments constructor for fxml-initialization
    * Construct a new AutoCompleteTextField.
    */
   public AutoCompleteTextField() {
      super();
      keyWordsProperty = new SimpleListProperty<>(FXCollections.emptyObservableList());
      entriesPopup = new ContextMenu();
      textProperty().addListener(createTextPropertyChangedListener());
      focusedProperty().addListener(createFocusChangedListener());
      setOnKeyTyped(keyEvent -> hasTypedAtLeastOnce = true);
      hasTypedAtLeastOnce = false;
   }

   private ChangeListener<Boolean> createFocusChangedListener() {
      return (observableValue, oldValue, newValue) -> {
         // Don't show the popup when the ui shows up the first time, but the user has nothing typed yet. But show it, if he/she has typed, leave the the text field and re-enter it later 
         if (newValue.booleanValue() && hasTypedAtLeastOnce) {
            showEntriesPopup();
         } else {
            hideAndClearEntriesPopup(false);
         }
      };
   }

   private ChangeListener<String> createTextPropertyChangedListener() {
      return (observableValue, oldStringValue, newStringValue) -> {
         if (getText().length() == 0) {
            hideAndClearEntriesPopup(false);
         } else {
            List<Ticket> searchResult = evalMatchingSearchResults();
            if (!keyWordsProperty.getValue().isEmpty()) {
               populatePopup(searchResult, getText());
               showEntriesPopup();
            } else {
               hideAndClearEntriesPopup(false);
            }
         }
      };
   }

   private List<Ticket> evalMatchingSearchResults() {
      return keyWordsProperty.getValue()
            .stream()
            .filter(startWithText().or(containsText()))
            .collect(Collectors.toList());
   }

   private Predicate<Ticket> containsText() {
      return ticket -> ticket.getTicketRep().toLowerCase().contains(getText().toLowerCase());
   }

   private Predicate<Ticket> startWithText() {
      return ticket -> ticket.getTicketRep().toLowerCase().startsWith(getText().toLowerCase());
   }

   private void showEntriesPopup() {
      if (!entriesPopup.isShowing()) {
         entriesPopup.show(this, Side.BOTTOM, 0, 0);
      }
   }

   private void hideAndClearEntriesPopup(boolean alwaysHideAndClear) {
      entriesPopup.hide();
      if (getText().trim().isEmpty() || alwaysHideAndClear) {
         // the popup disappears and we haven't entered anything -> clear all items. Otherwise we have a full popup when clicking into the textfield
         entriesPopup.getItems().clear();
      }
   }

   /*
    * Populate the entry set with the given search results. Display is limited to desired amount of entries, for performance reason
    * 
    */
   private void populatePopup(List<Ticket> searchResult, String enteredText) {
      List<CustomMenuItem> menuItems = new ArrayList<>();
      int count = Math.min(searchResult.size(), MAX_ENTRIES);
      for (int i = 0; i < count; i++) {
         Ticket ticket = searchResult.get(i);
         TextFlow textFlowPane = buildTextFlow(enteredText, ticket.getTicketRep());
         CustomMenuItem item = new CustomMenuItem(textFlowPane, true);
         item.setOnAction(actionEvent -> {
            setText(ticket.getNr());
            hideAndClearEntriesPopup(false);
         });
         if (inputMatchesTheOneResultExactly(enteredText, i, ticket)) {
            hideAndClearEntriesPopup(true);
            return;
         }
         menuItems.add(item);
      }
      entriesPopup.getItems().clear();
      entriesPopup.getItems().addAll(menuItems);
   }


   // The entered input matches the only result we have exactly (yes, casesensitiv) -> so clear the list and hide the popup. We're done here
   private boolean inputMatchesTheOneResultExactly(String enteredText, int i, Ticket ticket) {
      return i == 0 && ticket.getNr().equals(enteredText);
   }

   private TextFlow buildTextFlow(String enteredText, String ticketRep) {
      return buildTextFlow(buildTexts4TextFlow(enteredText, ticketRep));
   }

   /*
    * Builds a TextFlow which contains multiple 'javafx-Text' elements which displays the ticket-representation.
    * The first text element contains the text from index 0 upto the matched word. The second Text contains 
    * the matching word, highlighted with a different color. The third element containes non matching words.
    * Either until the end of the value or until to the next matching word. This can continue recursively
   
    * e.g we matched the word 'hungry' in the sentence 'I am hungry, Are you hungry?'
    * Then we get a TextFlow containing the four Texts 'I am', 'hungry', '. Are you', 'hungry' & '?'
    */
   private List<Text> buildTexts4TextFlow(String enteredText, String ticketRep) {
      int indexOfMatch = ticketRep.toLowerCase().indexOf(enteredText.toLowerCase());
      Text matchText = extractMatchingTextFromTicketString(enteredText, ticketRep);
      Text ticketTextBeforeEntered = new Text(ticketRep.substring(0, indexOfMatch));
      String textAfterEnteredTextValue = ticketRep.substring(indexOfMatch + matchText.getText().length());
      Text ticketTextAfterEnteredText = new Text(textAfterEnteredTextValue);
      int indexOfNextMatch = textAfterEnteredTextValue.toLowerCase().indexOf(enteredText.toLowerCase());
      if (indexOfNextMatch >= 0) {
         List<Text> subsequentOccurrences = buildTexts4TextFlow(enteredText, textAfterEnteredTextValue);
         LinkedList<Text> texts4TextFlow = new LinkedList<>(asList(ticketTextBeforeEntered, matchText));
         texts4TextFlow.addAll(subsequentOccurrences);
         return texts4TextFlow;
      } else {
         // No further matches: Before the match, the match itself and after the match
         return new LinkedList<>(asList(ticketTextBeforeEntered, matchText, ticketTextAfterEnteredText));
      }
   }

   /*
    * Retrieve the matching text with case insensitiv regex. This way we will find the original sequences (e.g. Bob)
    * even when we looked with 'bob'
    * We also try to be case insensitive -> (?i). 
    * The regex pattern '?i' is is not enough with characters like '�, �' and so on. We also need the unicode flag 'u'
    */
   private static Text extractMatchingTextFromTicketString(String enteredText, String ticketRep) {
      enteredText = escapeRegexSensitiveCharacters(enteredText);
      Pattern pattern = Pattern.compile("(?ui)(" + enteredText + ")");
      Matcher matcher = pattern.matcher(ticketRep);
      String matchedText = matcher.find() ? matcher.group() : "";
      Text match = new Text(matchedText);
      match.getStyleClass().add(Styles.WORD_MATCH);
      return match;
   }

   /*
    * Escapes some characters like '(' or '+' which will mess up with the regex when not treated specially
    * I guess there are a lot more, maybe there is cleverer way to do so
    */
   private static String escapeRegexSensitiveCharacters(String enteredText) {
      return enteredText.replace("(", "[(]")
            .replace(")", "[)]")// '(' or ')' have to be "escaped"
            .replace("+", "[+]");// '+' needs also to be "escaped"
   }

   private static TextFlow buildTextFlow(List<Text> texts) {
      TextFlow textFlowPane = new TextFlow();
      for (Text text : texts) {
         textFlowPane.getChildren().add(text);
      }
      return textFlowPane;
   }

   /**
    * Is called when ever the component, which contains this {@link AutoCompleteTextField}, is hide
    */
   public void onDispose() {
      hasTypedAtLeastOnce = false;
   }

   /**
    * @return the Property of the key words
    */
   public Property<ObservableList<Ticket>> keyWordsProperty() {
      return keyWordsProperty;
   }
}
