/**
 * 
 */
package com.myownb3.dominic.fileimport;

import static com.myownb3.dominic.timerecording.work.businessday.ValueTypes.BEGIN;
import static com.myownb3.dominic.timerecording.work.businessday.ValueTypes.DESCRIPTION;
import static com.myownb3.dominic.util.utils.StringUtil.isNotEmptyOrNull;
import static java.util.Objects.nonNull;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.myownb3.dominic.export.ContentSelector;
import com.myownb3.dominic.fileimport.exception.BusinessDayImportException;
import com.myownb3.dominic.fileimport.exception.FileImportException;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.callback.handler.impl.BusinessDayIncrementImport;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.charge.InvalidChargeTypeRepresentationException;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;
import com.myownb3.dominic.util.parser.DateParser;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * @author Dominic
 *
 */
public class BusinessDayImporter {

    private static final String PLACEHOLDER = "; ;";
    /**
     * The file extension of files to import
     */
    public static final String FILE_EXTENSION = "csv";
    public static final BusinessDayImporter INTANCE = new BusinessDayImporter();
    private static final String TICKET_NR_PREFIX = TextLabel.TICKET + ": ";
    private static final String CONTENT_SEPARATOR = ";";

    private BusinessDayImporter() {
	// private Constructor
    }

    /**
     * Imports a new {@link BusinessDay} from the given {@link List} of imported
     * lines
     * 
     * @param importedLines the imported content
     */
    public BusinessDay importBusinessDay(List<String> importedLines) throws BusinessDayImportException {

	try {
	    if (importedLines.isEmpty()) {
		return null;
	    }
	    return importBusinessDayInternal(importedLines);
	} catch (IOException | ParseException | InvalidChargeTypeRepresentationException e) {
	    e.printStackTrace();
	    throw new BusinessDayImportException(e);
	}
    }

    private BusinessDay importBusinessDayInternal(List<String> importedLines)
	    throws IOException, ParseException, InvalidChargeTypeRepresentationException {
	Date date = parseDate(importedLines.remove(0));
	List<BusinessDayIncrementImport> businessDayIncImports = createBusinessDayIncImports(importedLines, date);
	return createAndReturnBusinessDay(date, businessDayIncImports);
    }

    private BusinessDay createAndReturnBusinessDay(Date date, List<BusinessDayIncrementImport> businessDayIncImports) {
	BusinessDay businessDay = new BusinessDay(date);
	for (BusinessDayIncrementImport businessDayIncrementImport : businessDayIncImports) {
	    businessDay.addBusinessIncrements(businessDayIncrementImport);
	}
	return businessDay;
    }

    private List<BusinessDayIncrementImport> createBusinessDayIncImports(List<String> importedLines, Date date)
	    throws ParseException, InvalidChargeTypeRepresentationException {

	List<BusinessDayIncrementImport> businessDayInc2Import = new ArrayList<>();
	String importLine = importedLines.remove(0);
	String headerLine = importedLines.remove(0);

	while (nonNull(importLine) && !importedLines.isEmpty()) {
	    if (has2ParseLine(importLine)) {
		BusinessDayIncrementImport businessDayIncrementImport = importAndParseLine2BDIncAdd(headerLine,
			importLine, date);
		businessDayInc2Import.add(businessDayIncrementImport);
	    }
	    importLine = readLineAndRemoveEmptySpaces(importedLines.remove(0));
	}
	return businessDayInc2Import;
    }

    private String readLineAndRemoveEmptySpaces(String readLine) {
	// remove all the 'placeholder' elements which are simply semicolon separated
	// spaces
	return readLine.trim().replaceAll(PLACEHOLDER, "");
    }

    private boolean has2ParseLine(String importLine) {
	return StringUtil.isNotEmptyOrNull(importLine) && importLine.startsWith(TextLabel.TICKET + ":");
    }

    private String getElementFromLineAtIndex(String lineAtIndex, int index) {
	String valueAtIndex = lineAtIndex.split(CONTENT_SEPARATOR)[index];
	return valueAtIndex.trim();
    }

    private BusinessDayIncrementImport importAndParseLine2BDIncAdd(String headerLine, String importLine, Date date)
	    throws ParseException, InvalidChargeTypeRepresentationException {
	BusinessDayIncrementImport businessDayIncrementImport = new BusinessDayIncrementImport();

	ValueTypes currentValueTypes = ValueTypes.TICKET_NR;
	int currentElementIndex = 0;
	boolean businessDayIncImported = false;
	while (!businessDayIncImported) {
	    switch (currentValueTypes) {
	    case TICKET_NR:
		parseAndSetTicketNr(importLine, businessDayIncrementImport, currentElementIndex);
		currentElementIndex = 2;
		boolean hasDescription = hasDescription(headerLine);
		currentValueTypes = hasDescription ? DESCRIPTION : BEGIN;
		break;
	    case DESCRIPTION:
		currentElementIndex = parseAndSetDescription(importLine, businessDayIncrementImport,
			currentElementIndex);
		currentValueTypes = BEGIN;
		break;
	    case AMOUNT_OF_TIME:
		// Nothing to do this value will be calculated from all begin/ends
		break;
	    case BEGIN:
		// Fall through since we work on both at the same time
	    case END:
		currentElementIndex = parseAndAddTimeSnippet(importLine, date, businessDayIncrementImport,
			currentElementIndex);
		currentValueTypes = evalNextValue2Import(importLine, currentElementIndex);
		break;

	    case CHARGE_TYPE:
		parseAndSetChargeType(importLine, businessDayIncrementImport, currentElementIndex);
		businessDayIncImported = true;
		break;
	    default:
		break;
	    }
	}
	return businessDayIncrementImport;
    }

    private void parseAndSetTicketNr(String importLine, BusinessDayIncrementImport businessDayIncrementImport,
	    int currentElementIndex) {
	String ticketNr = getTicketNr(importLine, currentElementIndex);
	businessDayIncrementImport.setTicketNo(ticketNr);
    }

    private int parseAndSetDescription(String importLine, BusinessDayIncrementImport businessDayIncrementImport,
	    int currentElementIndex) {
	String description = getElementFromLineAtIndex(importLine, currentElementIndex++);
	businessDayIncrementImport.setDescription(description);
	return currentElementIndex;
    }

    private int parseAndAddTimeSnippet(String importLine, Date date,
	    BusinessDayIncrementImport businessDayIncrementImport, int currentElementIndex) throws ParseException {
	// get begin,- and end value as String
	String beginValue = getElementFromLineAtIndex(importLine, currentElementIndex++);
	String endValue = getElementFromLineAtIndex(importLine, currentElementIndex++);

	// Parse both, begin and end and create a new TimeSnippet
	TimeSnippet timeSnippet = TimeSnippet.createTimeSnippet(date, beginValue, endValue);

	// add the new TimeSnippet
	businessDayIncrementImport.getTimeSnippets().add(timeSnippet);
	return currentElementIndex;
    }

    private void parseAndSetChargeType(String importLine, BusinessDayIncrementImport businessDayIncrementImport,
	    int currentElementIndex) throws InvalidChargeTypeRepresentationException {
	String chargeType = getElementFromLineAtIndex(importLine, currentElementIndex);
	int leistungsartForRep = ChargeType.getLeistungsartForRep(chargeType);
	businessDayIncrementImport.setKindOfService(leistungsartForRep);
    }

    private String getTicketNr(String importLine, int currentElementIndex) {
	return getElementFromLineAtIndex(importLine, currentElementIndex).replace(TICKET_NR_PREFIX, "");
    }

    private boolean hasDescription(String headerLine) {
	return headerLine.contains(TextLabel.DESCRIPTION_LABEL);
    }

    private ValueTypes evalNextValue2Import(String importLine, int currentElementIndex) {
	boolean hasMoreElements = hasNextBeginEndElement(importLine, currentElementIndex);
	return hasMoreElements ? BEGIN : ValueTypes.CHARGE_TYPE;
    }

    private boolean hasNextBeginEndElement(String importLine, int currentElementIndex) {
	return currentElementIndex < importLine.split(CONTENT_SEPARATOR).length - 2;
    }

    private Date parseDate(String readLine) throws IOException, ParseException {
	if (isNotEmptyOrNull(readLine)) {
	    return DateParser.parse2Date(readLine, ContentSelector.DATE_REP_PATTERN);
	}
	throw new FileImportException("Die erste Zeile im Import-file darf nicht null oder leer sein!");
    }
}