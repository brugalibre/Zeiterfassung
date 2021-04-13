package com.adcubum.timerecording.ui.app.pages.overview.model.table;

import static java.util.Objects.nonNull;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.librarys.pictures.PictureLibrary;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.ui.core.view.table.EditableCell;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

/**
 * 
 * It's basically an editable javafx.scene.control.TableCell which contains a {@link Ticket} as a value
 * 
 * @author Dominic
 */
public class EditableTicketCell extends EditableCell<BusinessDayIncTableRowValue, Ticket> {

   private EditableTicketCell(StringConverter<Ticket> converter, TextField textField) {
      super(converter, textField);
      enableReadOnlyMode();
      setContentDisplay(ContentDisplay.LEFT);// Defines on which side the warning-icon is displayed
   }

   @Override
   protected ChangeListener<Ticket> getItemChangedListener(StringConverter<Ticket> converter) {
      return (obsValue, oldItem, newItem) -> setText(converter.toString(newItem));
   }

   @Override
   protected void enableReadOnlyMode() {
      setGraphic(null);
   }

   @Override
   public void updateItem(Ticket ticket, boolean isEmpty) {
      super.updateItem(ticket, isEmpty);
      if (nonNull(ticket)) {
         setTextAndWarnIconIfNecessary(ticket);
      }
   }

   @Override
   public void startEdit() {
      super.startEdit();
      setGraphic(textField);
   }

   private void setTextAndWarnIconIfNecessary(Ticket ticket) {
      setText(ticket.getNr());
      ImageView imageView = null;
      Tooltip tooltip = null;
      if (!ticket.isBookable()) {
         imageView = buildImageView();
         tooltip = new Tooltip(getTooltipText(ticket));
      }
      setTooltip(tooltip);
      setGraphic(imageView);
   }

   private static ImageView buildImageView() {
      ImageView imageView;
      imageView = new ImageView(PictureLibrary.getWarningIcon());
      imageView.setFitHeight(20);
      imageView.setSmooth(true);
      imageView.setPreserveRatio(true);
      return imageView;
   }

   private static String getTooltipText(Ticket ticket) {
      if (ticket.isDummyTicket()) {
         return TextLabel.TICKET_DOES_NOT_EXIST_TOOLTIP;
      } else {
         return TextLabel.NOT_BOOKABLE_TICKET_TOOLTIP;
      }
   }

   /**
    * Creates a new {@link EditableTicketCell} with a default TextField
    * 
    * @return a new {@link EditableTicketCell} with a default TextField
    */
   public static EditableTicketCell createTicketEditCell() {
      return new EditableTicketCell(new Ticket2StringConverter(), new TextField());
   }

   private static class Ticket2StringConverter extends StringConverter<Ticket> {

      @Override
      public String toString(Ticket ticket) {
         return nonNull(ticket) ? ticket.getNr() : "";
      }

      @Override
      public Ticket fromString(String newTicketNr) {
         return TicketBacklogSPI.getTicketBacklog().getTicket4Nr(newTicketNr);
      }
   }
}
