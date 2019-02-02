/**
 * 
 */
package com.myownb3.dominic.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

import com.myownb3.dominic.timerecording.work.date.Date;
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
}