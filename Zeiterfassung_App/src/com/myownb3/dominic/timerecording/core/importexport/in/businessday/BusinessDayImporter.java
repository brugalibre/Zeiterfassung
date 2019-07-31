/**
 * 
 */
package com.myownb3.dominic.timerecording.core.importexport.in.businessday;

import static com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes.BEGIN;
import static com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes.DESCRIPTION;
import static com.myownb3.dominic.util.utils.StringUtil.isNotEmptyOrNull;
import static java.util.Objects.requireNonNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.core.callbackhandler.impl.BusinessDayIncrementImport;
import com.myownb3.dominic.timerecording.core.charge.ChargeType;
import com.myownb3.dominic.timerecording.core.charge.exception.InvalidChargeTypeRepresentationException;
import com.myownb3.dominic.timerecording.core.importexport.in.businessday.exception.BusinessDayImportException;
import com.myownb3.dominic.timerecording.core.importexport.out.businessday.ContentSelector;
import com.myownb3.dominic.timerecording.core.importexport.out.file.FileExporter;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes;
import com.myownb3.dominic.util.parser.DateParser;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * The {@link BusinessDayImporter} is used to import a {@link BusinessDay} from
 * a previously exported one. So the file to import must follow the exact same
 * structure as the file which the {@link FileExporter} exports
 * 
 * @author Dominic
 * @see ContentSelector
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
    private static final String CONTENT_LINE_BEGIN = TextLabel.TICKET + ":";
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
	} catch (ParseException | InvalidChargeTypeRepresentationException e) {
	    throw new BusinessDayImportException(e);
	}
    }

    private BusinessDay importBusinessDayInternal(List<String> importedLines) throws ParseException, InvalidChargeTypeRepresentationException {
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

    private List<BusinessDayIncrementImport> createBusinessDayIncImports(List<String> importedLines, Date date) throws ParseException, InvalidChargeTypeRepresentationException {

	List<BusinessDayIncrementImport> businessDayInc2Import = new ArrayList<>();
	List<String> filteredLines = filterLines(importedLines);
	while (has2ContinueParsing(filteredLines)) {
	    String importLine = filteredLines.remove(0);
	    BusinessDayIncrementImport businessDayIncrementImport = parseLine2BusinessDayIncImport(importLine, date);
	    businessDayInc2Import.add(businessDayIncrementImport);
	}
	return businessDayInc2Import;
    }

    private List<String> filterLines(List<String> importedLines) {
	return importedLines.stream()//
		.filter(importedLine -> has2ParseLine(importedLine))//
		.collect(Collectors.toList());
    }

    private boolean has2ContinueParsing(List<String> importedLines) {
	return !importedLines.isEmpty();
    }

    private boolean has2ParseLine(String importLine) {
	return StringUtil.isNotEmptyOrNull(importLine) && importLine.startsWith(CONTENT_LINE_BEGIN);
    }

    private BusinessDayIncrementImport parseLine2BusinessDayIncImport(String importLine, Date date) throws ParseException, InvalidChargeTypeRepresentationException {
	BusinessDayIncrementImport businessDayIncrementImport = new BusinessDayIncrementImport();

	ValueTypes currentValueType = ValueTypes.TICKET_NR;
	int currentElementIndex = 0;
	boolean businessDayIncImported = false;
	while (!businessDayIncImported) {
	    switch (currentValueType) {
	    case TICKET_NR:
		parseAndSetTicketNr(importLine, businessDayIncrementImport, currentElementIndex);
		currentElementIndex = shiftIndex(currentValueType, currentElementIndex, importLine);
		currentValueType = evalNextValue2Import(currentValueType, importLine, currentElementIndex);
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

    private void parseAndSetTicketNr(String importLine, BusinessDayIncrementImport businessDayIncrementImport, int currentElementIndex) {
	String ticketNrElement = getElementFromLineAtIndex(importLine, currentElementIndex);
	String ticketNr = ticketNrElement.replace(CONTENT_LINE_BEGIN, "");
	businessDayIncrementImport.setTicketNo(ticketNr);
    }

    private void parseAndSetDescription(String importLine, BusinessDayIncrementImport businessDayIncrementImport, int currentElementIndex) {
	String description = getElementFromLineAtIndex(importLine, currentElementIndex);
	businessDayIncrementImport.setDescription(description);
    }

    private void parseAndAddTimeSnippet(String importLine, Date date, BusinessDayIncrementImport businessDayIncrementImport, int currentElementIndex) throws ParseException {
	// get begin,- and end value as String
	String beginValue = getElementFromLineAtIndex(importLine, currentElementIndex++);
	String endValue = getElementFromLineAtIndex(importLine, currentElementIndex++);

	// Parse both, begin and end and create a new TimeSnippet
	TimeSnippet timeSnippet = TimeSnippet.createTimeSnippet(date, beginValue, endValue);

	// add the new TimeSnippet
	businessDayIncrementImport.getTimeSnippets().add(timeSnippet);
    }

    private void parseAndSetChargeType(String importLine, BusinessDayIncrementImport businessDayIncrementImport, int currentElementIndex) throws InvalidChargeTypeRepresentationException {
	String chargeType = getElementFromLineAtIndex(importLine, currentElementIndex);
	int leistungsartForRep = ChargeType.getLeistungsartForRep(chargeType);
	businessDayIncrementImport.setKindOfService(leistungsartForRep);
    }

    private String getElementFromLineAtIndex(String lineAtIndex, int index) {
	String valueAtIndex = lineAtIndex.split(CONTENT_SEPARATOR)[index];
	return valueAtIndex.trim();
    }

    /*
     * Shifts the index which is used to determine the value of the next element to
     * import depending on the current imported element
     */
    private int shiftIndex(ValueTypes currentValueType, int currentElementIndex, String importLine) {
	switch (currentValueType) {
	case TICKET_NR:
	    return currentElementIndex = currentElementIndex + 1;
	case DESCRIPTION:
	    return currentElementIndex = currentElementIndex + 2;
	case BEGIN:
	    // Plus two since we incremented the index two times while adding a
	    // new time stamp. Continue incrementing as long as there are
	    // 'placeholder' elements
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

    /**
     * Evaluates the value type of the next element to import according the
     * current imported element and other informations like if the current line
     * has a description or if there are any more 'begin/end' elements
     */
    private ValueTypes evalNextValue2Import(ValueTypes currentValueType, String importLine, int currentElementIndex) {
	switch (currentValueType) {
	case TICKET_NR:
	    return DESCRIPTION;
	case DESCRIPTION:
	    return BEGIN;
	case BEGIN:
	    boolean hasMoreElements = hasNextBeginEndElement(importLine, currentElementIndex);
	    return hasMoreElements ? BEGIN : ValueTypes.CHARGE_TYPE;
	default:
	    throw new BusinessDayImportException("Unsupported ValueTypes '" + currentValueType + "'");
	}
    }

    private boolean hasNextBeginEndElement(String importLine, int currentElementIndex) {
	boolean hasMoreElements = currentElementIndex < importLine.split(CONTENT_SEPARATOR).length - 2;
	return hasMoreElements && isNexElementNotEmpty(importLine, currentElementIndex);
    }

    private boolean isNexElementNotEmpty(String importLine, int currentElementIndex) {
	return StringUtil.isNotEmptyOrNull(importLine.split(CONTENT_SEPARATOR)[currentElementIndex++]);
    }

    private Date parseDate(String readLine) throws ParseException {
	if (isNotEmptyOrNull(readLine)) {
	    return DateParser.parse2Date(readLine, ContentSelector.DATE_REP_PATTERN);
	}
	throw new BusinessDayImportException(
		"The first line within the import file must not be empty or null! It should rather look like 'Dienstag, 25 Jul 2019 06:45:00'");
    }

    private void checkInput(List<String> importedLines) {
	requireNonNull(importedLines, "The variable 'importedLines' must not be null!");
	if (importedLines.isEmpty()) {
	    throw new BusinessDayImportException("The List 'importedLines' must not be empty!");
	}
    }
}