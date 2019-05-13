/**
 * 
 */
package com.myownb3.dominic.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import com.myownb3.dominic.export.exception.FileExportException;

/**
 * @author Dominic
 *
 */
public class FileExporter {
    
    public static final FileExporter INTANCE = new FileExporter();
    
    private FileExporter () {
	// private Constructor
    }
    
    /**
     * Exports the given list of {@link String} to the Desktop
     * @param content the content to export
     */
    public void export(List<String> content) {
	String dateDetails = DateFormat.getDateInstance().format(new Date());
	float randomNo = System.currentTimeMillis();
	String fileExtension = "csv";
	File file = new File(
		System.getProperty("user.home") + "\\Desktop\\" + dateDetails + "_" + randomNo + "." + fileExtension);

	try (FileWriter writer = new FileWriter(file)) {
	    file.createNewFile();
	    writeLines(content, writer);
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new FileExportException(e);
	}
    }

    private void writeLines(List<String> content, FileWriter writer) throws IOException {
	for (String element : content) {
	    writer.write(element);
	}
    }
}