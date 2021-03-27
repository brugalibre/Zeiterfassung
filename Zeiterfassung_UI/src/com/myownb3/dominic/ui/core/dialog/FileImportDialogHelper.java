package com.myownb3.dominic.ui.core.dialog;

import java.io.File;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.importexport.in.file.FileImporterImpl;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The {@link FileImportDialogHelper} handles the {@link FileChooser} and its
 * methods in order to show a import dialog
 * 
 * @author Dominic
 *
 */
public class FileImportDialogHelper {

   private FileChooser fileChooser;

   public FileImportDialogHelper() {
      fileChooser = new FileChooser();
   }

   /**
    * Shows a dialog in order to import a file
    * 
    * @param stage
    *        the stage within the dialog is shown
    * @return the selected file or <code>null</code> if none was selected
    */
   public File showImportDialogAndReturnFile(Stage stage) {
      fileChooser.setTitle(TextLabel.SHOW_IMPORT_DIALOG_TITLE);
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
            "Aufzeichnungen (*." + FileImporterImpl.FILE_EXTENSION + ")", "*." + FileImporterImpl.FILE_EXTENSION);
      fileChooser.getExtensionFilters().add(extFilter);
      return fileChooser.showOpenDialog(stage);
   }
}
