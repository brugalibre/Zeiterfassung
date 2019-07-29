/**
 * 
 */
package com.myownb3.dominic.fileimport;

import static com.myownb3.dominic.timerecording.work.businessday.ValueTypes.BEGIN;
import static com.myownb3.dominic.timerecording.work.businessday.ValueTypes.DESCRIPTION;
import static com.myownb3.dominic.timerecording.work.businessday.ValueTypes.END;
import static com.myownb3.dominic.util.utils.StringUtil.isNotEmptyOrNull;
import static java.util.Objects.nonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.myownb3.dominic.export.ContentSelector;
import com.myownb3.dominic.fileimport.exception.FileImportException;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.callback.handler.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;
import com.myownb3.dominic.util.parser.DateParser;

/**
 * @author Dominic
 *
 */
public class FileImporter {

    /**
     * The file extension of files to import
     */
    public static final String FILE_EXTENSION = "csv";
    public static final FileImporter INTANCE = new FileImporter();

    private FileImporter() {
	// private Constructor
    }

    public void importFile(File selectedFile) {
	
	try (FileReader fileReader = new FileReader(selectedFile)) {
	    
	    BufferedReader bufferedReader = new BufferedReader(fileReader);
	    
	    Date date = importAndParseFirstLine2Date(bufferedReader);
	    BusinessDay businessDay = new BusinessDay(date);
	    
	    importAndWorkOtherLines(businessDay, bufferedReader);
	    
	} catch (IOException | ParseException e) {
	    e.printStackTrace();
	    throw new FileImportException(e);
	}	
    }

    private void importAndWorkOtherLines(BusinessDay businessDay, BufferedReader bufferedReader) throws IOException {
	
	String headerLine = bufferedReader.readLine();
	
	List<String> contentLines = collectContentLines(bufferedReader);
	
	Map<String, String> valueMap = getValueName2ValueMap (headerLine, contentLines); 
	
	while (nonNull(headerLine)) {
	    
	    BusinessDayIncrementAdd businessDayIncrementAdd = importAndParseLine2BDIncAdd(headerLine);
	    businessDay.addBusinessIncrement(businessDayIncrementAdd);
	    
	    headerLine = bufferedReader.readLine();
	}
    }

    private List<String> collectContentLines(BufferedReader bufferedReader) throws IOException {
	List<String> contentLines = new LinkedList<>();
	String importLine = bufferedReader.readLine();
	while (nonNull(importLine)) {
	    contentLines.add(importLine);
	}
	return contentLines;
    }

    private Map<String, String> getValueName2ValueMap(String headerLine, List<String> contentLines) throws IOException {

	Map<String, String> valueName2ValueMap = new HashMap<>();

	String[] headers = headerLine.split(";");
	for (String header : headers) {
	    switch (header) {
	    case TextLabel.TICKET:
		putTicketNrs(valueName2ValueMap, TextLabel.TICKET, contentLines);
		break;
	    case TextLabel.DESCRIPTION_LABEL:
		putDescriptions(valueName2ValueMap, TextLabel.DESCRIPTION_LABEL, contentLines);
		break;
	    case TextLabel.VON_LABEL:
		break;
	    case TextLabel.BIS_LABEL:
		break;
	    case TextLabel.CHARGE_TYPE_LABEL:
		break;
	    case TextLabel.CHARGED:
		// fall through
	    case TextLabel.AMOUNT_OF_HOURS_LABEL:
		// ignore, this value is calculated - not imported
		break;
	    default:
		throw new FileImportException("Unknown header '" + header + "'!");
	    }
	}
	return valueName2ValueMap;
    }

    private void putDescriptions(Map<String, String> valueName2ValueMap, String descriptionLabel,
	    List<String> contentLines) {
	
	for (int i = 0; i < contentLines.size(); i++) {
	    String lineAtIndex = contentLines.get(i);
	    String description = getElementFromLineAtIndex(lineAtIndex, 2);
	    valueName2ValueMap.put(descriptionLabel + i, description);
	}
    }

    private void putTicketNrs(Map<String, String> valueName2ValueMap, String header, List<String> contentLines) {

	for (int i = 0; i < contentLines.size(); i++) {
	    String lineAtIndex = contentLines.get(i);
	    String ticketElement =getElementFromLineAtIndex(lineAtIndex, 0);
	    String ticketNr = ticketElement.replace(TextLabel.TICKET_NUMBER_LABEL + " :", "");
	    
	    valueName2ValueMap.put(header + i, ticketNr);
	}
    }

    private String getElementFromLineAtIndex(String lineAtIndex, int index) {
	return lineAtIndex.split(";")[index];
    }
    
    private BusinessDayIncrementAdd importAndParseLine2BDIncAdd(String readLine) {
	
	BusinessDayIncrementAdd businessDayIncrementAdd = new BusinessDayIncrementAdd();
	
	ValueTypes currentValueTypes = ValueTypes.TICKET_NR;
	String[] lineElements = readLine.split(";");
	
	boolean businessDayIncImported = false;
	while (businessDayIncImported){
	    switch (currentValueTypes) {
	    case TICKET_NR:
//		String ticketNr = getTicketNr(lineElements);
//		businessDayIncrementAdd.setTicketNo(ticketNr);
//		currentValueTypes = DESCRIPTION;
		break;
	    case DESCRIPTION:
		String description = getDescription (lineElements);
		businessDayIncrementAdd.setDescription(description);
		currentValueTypes = BEGIN;
		break;
	    case AMOUNT_OF_TIME:
		// Nothing to do this value will be calculated from all begin/ends
		break;
	    case BEGIN:
		currentValueTypes = END;
		break;
	    case END:
		
		boolean hasMoreElements = false;
		currentValueTypes = hasMoreElements ? BEGIN : ValueTypes.CHARGE_TYPE;
		break;
	    case CHARGE_TYPE:
		
		businessDayIncImported = true;
		break;
	    default:
		break;
	    }
	}
	return businessDayIncrementAdd;
    }

    private String getDescription(String[] lineElements) {
	for (String string : lineElements) {
	    
	}
	// TODO Auto-generated method stub
	return null;
    }

    private Date importAndParseFirstLine2Date(BufferedReader bufferedReader) throws IOException, ParseException {

	String readLine = bufferedReader.readLine();
	if (isNotEmptyOrNull(readLine)){
	    return DateParser.parse2Date(readLine, ContentSelector.DATE_REP_PATTERN);
	}
	throw new FileImportException("Die erste Zeile im Import-file darf nicht null oder leer sein!");
    }
}