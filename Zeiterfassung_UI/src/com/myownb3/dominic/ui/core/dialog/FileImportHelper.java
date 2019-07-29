package com.myownb3.dominic.ui.core.dialog;

import java.io.File;

import com.myownb3.dominic.fileimport.FileImporter;
import com.myownb3.dominic.librarys.text.res.TextLabel;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileImportHelper {

    private FileChooser fileChooser;

    public FileImportHelper() {
	fileChooser = new FileChooser();
    }

    public void showImportDialog(Stage stage) {
	fileChooser.setTitle(TextLabel.SHOW_IMPORT_DIALOG_TITLE);
	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
		"TEXT files (*." + FileImporter.FILE_EXTENSION + ")", "*." + FileImporter.FILE_EXTENSION);
	fileChooser.getExtensionFilters().add(extFilter);
	File selectedFile = fileChooser.showOpenDialog(stage);
	if (selectedFile != null){
	    FileImporter.INTANCE.importFile(selectedFile);
	}
    }
}
