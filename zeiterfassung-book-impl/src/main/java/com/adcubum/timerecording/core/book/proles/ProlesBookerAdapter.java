package com.adcubum.timerecording.core.book.proles;

import com.adcubum.timerecording.core.book.common.CommonBookerAdapter;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.util.parser.DateParser;
import com.adcubum.util.parser.NumberFormat;
import com.zeiterfassung.web.common.book.record.BookRecord;
import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import com.zeiterfassung.web.proles.book.ProlesBooker;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.adcubum.util.parser.DateParser.DD_MM_YYYY;
import static com.zeiterfassung.web.common.book.record.impl.BookRecordImpl.BookRecordBuilder;
import static com.zeiterfassung.web.proles.book.record.ProlesBookRecordEntry.ProlesBookRecordEntryBuilder;

public class ProlesBookerAdapter extends CommonBookerAdapter<ProlesServiceCodeAdapter> {

    private Supplier<ProlesBooker> prolesBookerSupplier;

    /**
     * Constructor for testing purpose only
     *
     * @param prolesBooker                  the mocked {@link ProlesBooker}
     */
    ProlesBookerAdapter(ProlesBooker prolesBooker) {
        super(ProlesServiceCodeAdapter.class);
        this.prolesBookerSupplier = () -> prolesBooker;
    }

    public ProlesBookerAdapter() {
        super(ProlesServiceCodeAdapter.class);
        this.prolesBookerSupplier = () -> ProlesBooker.createAndInitBooker(username, String.valueOf(userPwdSupplier.get()));
    }

    /**
     * Collects from each {@link BusinessDayIncrement} the content to book and calls
     * finally the {@link ProlesBooker#bookRecords(BookRecord)}.
     * Additionally, all booked {@link BusinessDayIncrement} are flagged as charged
     *
     * @see ProlesBooker#bookRecords(BookRecord)
     */
    @Override
    public BookerResult book(BusinessDay businessDay) {
        BookRecord bookRecord = createBookRecord(businessDay);
        ProlesBooker prolesBooker = prolesBookerSupplier.get();
        BookRecord bookedBookRecord = prolesBooker.bookRecords(bookRecord);
        BusinessDay bookedBusinessDay = flagBookedBDIncrements(businessDay, bookedBookRecord);
        return createAndReturnBookResult(bookedBusinessDay, businessDay);
    }

    private static BusinessDay flagBookedBDIncrements(BusinessDay businessDay, BookRecord bookedBookRecord) {
        for (BookRecordEntry bookedRecordEntry : bookedBookRecord.getBookerRecordEntries()) {
            if (bookedRecordEntry.getIsBooked()) {
                UUID bdIncrementId = UUID.fromString(bookedRecordEntry.getExternalId());
                businessDay = businessDay.flagBusinessDayIncrementAsBooked(bdIncrementId);
            }
        }
        return businessDay;
    }

    private static BookRecord createBookRecord(BusinessDay businessDay) {
        return BookRecordBuilder.of()
                .withBookRecordEntries(map2BookRecordEntries(businessDay))
                .build();
    }

    private static List<BookRecordEntry> map2BookRecordEntries(BusinessDay businessDay) {
        return businessDay.getIncrements().stream()
                .filter(BusinessDayIncrement::isBookable)
                .filter(businessDayIncrement -> !businessDayIncrement.isBooked())
                .map(businessDayIncrement -> ProlesBookRecordEntryBuilder.of()
                        .withDate(DateParser.parse2String(businessDay.getDateTime(), DD_MM_YYYY))
                        .withActivity(businessDayIncrement.getTicketActivity().getActivityName())
                        .withCustomer(businessDayIncrement.getTicket().getTicketAttrs().getThema())
                        .withProject(businessDayIncrement.getTicket().getTicketAttrs().getProjectDesc())
                        .withDescription(businessDayIncrement.getDescription())
                        .withExternalId(businessDayIncrement.getId().toString())
                        .withAmountOfHours(NumberFormat.format(businessDayIncrement.getTotalDuration()))
                        .build())
                .collect(Collectors.toList());
    }
}
