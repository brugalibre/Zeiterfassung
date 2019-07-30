/**
 * 
 */
package com.myownb3.dominic.fileimport;

import static com.myownb3.dominic.timerecording.work.businessday.ValueTypes.BEGIN;
import static com.myownb3.dominic.timerecording.work.businessday.ValueTypes.DESCRIPTION;
import static com.myownb3.dominic.util.utils.StringUtil.isNotEmptyOrNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

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
import com.sun.xml.internal.txw2.IllegalSignatureException;

/**
 * @author Dominic
 *
 */
public class BusinessDayImporter {

    /**
     * The file extension of files to import
     */
    public static final String FILE_EXTENSION = "csv";

    /**
     * The singleton instance of the {@link BusinessDayImporter}
     */
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
     * @param importedLines
     *            the imported content
     */
    public BusinessDay importBusinessDay(List<String> importedLines) throws BusinessDayImportException {

	try {
	    checkInput(importedLines);
	    return importBusinessDayInternal(new ArrayList<>(importedLines));
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
	boolean hasDescription = hasDescription(importedLines);

	while (has2ContinueParsing(importedLines, importLine)) {
	    if (has2ParseLine(importLine)) {
		BusinessDayIncrementImport businessDayIncrementImport = parseLine2BusinessDayIncImport(hasDescription,
			importLine, date);
		businessDayInc2Import.add(businessDayIncrementImport);
	    }
	    importLine = importedLines.remove(0);
	}
	return businessDayInc2Import;
    }

    private boolean has2ContinueParsing(List<String> importedLines, String importLine) {
	return nonNull(importLine) && !importedLines.isEmpty();
    }

    private boolean has2ParseLine(String importLine) {
	return StringUtil.isNotEmptyOrNull(importLine) && importLine.startsWith(TextLabel.TICKET + ":");
    }

    private String getElementFromLineAtIndex(String lineAtIndex, int index) {
	String valueAtIndex = lineAtIndex.split(CONTENT_SEPARATOR)[index];
	return valueAtIndex.trim();
    }

    private BusinessDayIncrementImport parseLine2BusinessDayIncImport(boolean hasDescription, String importLine,
	    Date date) throws ParseException, InvalidChargeTypeRepresentationException {
	BusinessDayIncrementImport businessDayIncrementImport = new BusinessDayIncrementImport();

	ValueTypes currentValueType = ValueTypes.TICKET_NR;
	int currentElementIndex = 0;
	boolean businessDayIncImported = false;
	while (!businessDayIncImported) {
	    switch (currentValueType) {
	    case TICKET_NR:
		parseAndSetTicketNr(importLine, businessDayIncrementImport, currentElementIndex++);
		currentElementIndex = shiftIndex(currentValueType, currentElementIndex, hasDescription, importLine);
		currentValueType = evalNextValue2Import(currentValueType, currentElementIndex, hasDescription);
		break;

	    case DESCRIPTION:
		parseAndSetDescription(importLine, businessDayIncrementImport, currentElementIndex);
		currentElementIndex = shiftIndex(currentValueType, currentElementIndex, importLine);
		currentValueType = evalNextValue2Import(currentValueType, importLine, currentElementIndex);
		break;

	    case BEGIN:
		// Fall through since we work on both at the same time
	    case END:
		parseAndAddTimeSnippet(importLine, date, businessDayIncrementImport, currentElementIndex);
		currentElementIndex = shiftIndex(currentValueType, currentElementIndex, importLine);
		currentValueType = evalNextValue2Import(currentValueType, importLine, currentElementIndex);
		break;

	    case CHARGE_TYPE:
		parseAndSetChargeType(importLine, businessDayIncrementImport, currentElementIndex);
		businessDayIncImported = true;
		break;

	    default:
		// All other cases like AMOUNT_OF_TIME or NONE are ignored
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

    private String getTicketNr(String importLine, int currentElementIndex) {
	return getElementFromLineAtIndex(importLine, currentElementIndex).replace(TICKET_NR_PREFIX, "");
    }

    private void parseAndSetDescription(String importLine, BusinessDayIncrementImport businessDayIncrementImport,
	    int currentElementIndex) {
	String description = getElementFromLineAtIndex(importLine, currentElementIndex);
	businessDayIncrementImport.setDescription(description);
    }

    private void parseAndAddTimeSnippet(String importLine, Date date,
	    BusinessDayIncrementImport businessDayIncrementImport, int currentElementIndex) throws ParseException {
	// get begin,- and end value as String
	String beginValue = getElementFromLineAtIndex(importLine, currentElementIndex++);
	String endValue = getElementFromLineAtIndex(importLine, currentElementIndex++);

	// Parse both, begin and end and create a new TimeSnippet
	TimeSnippet timeSnippet = TimeSnippet.createTimeSnippet(date, beginValue, endValue);

	// add the new TimeSnippet
	businessDayIncrementImport.getTimeSnippets().add(timeSnippet);
    }

    private void parseAndSetChargeType(String importLine, BusinessDayIncrementImport businessDayIncrementImport,
	    int currentElementIndex) throws InvalidChargeTypeRepresentationException {
	String chargeType = getElementFromLineAtIndex(importLine, currentElementIndex);
	int leistungsartForRep = ChargeType.getLeistungsartForRep(chargeType);
	businessDayIncrementImport.setKindOfService(leistungsartForRep);
    }

    private int shiftIndex(ValueTypes currentValueTypes, int currentElementIndex, String importLine) {
	return shiftIndex(currentValueTypes, currentElementIndex, false, importLine);
    }

    /*
     * Shifts the index which is used to determine the value of the next element
     * to import depending on the current imported element
     */
    private int shiftIndex(ValueTypes currentValueTypes, int currentElementIndex, boolean hasDescription,
	    String importLine) {
	switch (currentValueTypes) {
	case TICKET_NR:
	    return currentElementIndex = hasDescription ? currentElementIndex : currentElementIndex + 1;
	case DESCRIPTION:
	    return currentElementIndex = currentElementIndex + 2;
	case BEGIN:
	    // Plus two since we incremented the index two times while adding a
	    // new time stamp. Continue incrementing as long as there are 'placeholder' elements
	    currentElementIndex = currentElementIndex + 2;
	    while (!isNexElementNotEmpty(importLine, currentElementIndex)) {
		currentElementIndex++;
	    }
	    return currentElementIndex;
	default:
	    // All other cases are ignored
	    return currentElementIndex;
	}
    }

    private ValueTypes evalNextValue2Import(ValueTypes currentValueType, String importLine, int currentElementIndex) {
	return evalNextValue2Import(currentValueType, importLine, currentElementIndex, false);
    }

    private ValueTypes evalNextValue2Import(ValueTypes currentValueType, int currentElementIndex,
	    boolean hasDescription) {
	return evalNextValue2Import(currentValueType, null, currentElementIndex, hasDescription);
    }

    /**
     * Evaluates the value type of the next element to import according the
     * current imported element and other informations like if the current line
     * has a description or if there are any more 'begin/end' elements
     */
    private ValueTypes evalNextValue2Import(ValueTypes currentValueType, String importLine, int currentElementIndex,
	    boolean hasDescription) {
	switch (currentValueType) {
	case TICKET_NR:
	    return hasDescription ? DESCRIPTION : BEGIN;
	case DESCRIPTION:
	    return BEGIN;
	case BEGIN:
	    boolean hasMoreElements = hasNextBeginEndElement(importLine, currentElementIndex);
	    return hasMoreElements ? BEGIN : ValueTypes.CHARGE_TYPE;
	default:
	    throw new IllegalSignatureException("Unsupported ValueTypes '" + currentValueType + "'");
	}
    }

    private boolean hasDescription(List<String> importedLines) {
	return importedLines.stream().anyMatch(importedLine -> importedLine.contains(TextLabel.DESCRIPTION_LABEL));
    }

    private boolean hasNextBeginEndElement(String importLine, int currentElementIndex) {
	boolean hasMoreElements = currentElementIndex < importLine.split(CONTENT_SEPARATOR).length - 2;
	return hasMoreElements && isNexElementNotEmpty(importLine, currentElementIndex);
    }

    private boolean isNexElementNotEmpty(String importLine, int currentElementIndex) {
	return StringUtil.isNotEmptyOrNull(importLine.split(CONTENT_SEPARATOR)[currentElementIndex++]);
    }

    private Date parseDate(String readLine) throws IOException, ParseException {
	if (isNotEmptyOrNull(readLine)) {
	    return DateParser.parse2Date(readLine, ContentSelector.DATE_REP_PATTERN);
	}
	throw new FileImportException(
		"The first line within the import file must not be empty or null! It should rather look like 'Dienstag, 25 Jul 2019 06:45:00'");
    }

    private void checkInput(List<String> importedLines) {
	requireNonNull(importedLines, "The variable 'importedLines' must not be null!");
	if (importedLines.isEmpty()) {
	    throw new IllegalStateException("The List 'importedLines' must not be empty!");
	}
    }
}