/**
 * 
 */
package com.myownb3.dominic.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

import java.util.Date;
import com.myownb3.dominic.util.exception.GlobalExceptionHandler;

/**
 * @author Dominic
 *
 */
public class FileExporter {
    public static void export(List<String> content) {
	String dateDetails = DateFormat.getDateInstance().format(new Date());
	String randomNo = String.valueOf(System.currentTimeMillis());
	String fileExtension = "csv";
	File file = new File(
		System.getProperty("user.home") + "\\Desktop\\" + dateDetails + "_" + randomNo + "." + fileExtension);

	try (FileWriter writer = new FileWriter(file)) {
	    file.createNewFile();
	    writeLines(content, writer);
	} catch (IOException e) {
	    e.printStackTrace();
	    GlobalExceptionHandler.handleGlobalException(Thread.currentThread(), e);
	}
    }

    private static void writeLines(List<String> content, FileWriter writer) throws IOException {
	for (String element : content) {
	    writer.write(element);
	}
    }

    /**
     * Exports the 'book.txt' file which is later used by the TurboBucher-app in
     * order to charge-off all necessary Jira-Tickets After the export, the path of
     * the file is returned
     * 
     * @param content4TurboBucher
     * @return the path of the new created file
     */
    public static File exportAndReturnFile4Charge(List<String> content4TurboBucher) {
	File file = new File(System.getProperty("user.home") + "\\Desktop\\" + "book.txt");
	try (FileWriter writer = new FileWriter(file)) {
	    file.createNewFile();
	    writeLines(content4TurboBucher, writer);
	} catch (IOException e) {
	    e.printStackTrace();
	    GlobalExceptionHandler.handleGlobalException(Thread.currentThread(), e);
	}
	return file;
    }
}