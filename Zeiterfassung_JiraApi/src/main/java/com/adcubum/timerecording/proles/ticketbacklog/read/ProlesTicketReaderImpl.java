package com.adcubum.timerecording.proles.ticketbacklog.read;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.proles.ticketbacklog.read.data.ProlesTicketsJsonImport;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class ProlesTicketReaderImpl implements ProlesTicketReader {

    @Override
    public List<Ticket> readProlesTicketFromPath(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        ProlesTicketsJsonImport prolesTicketsJsonImport = new ProlesTicketsJsonImport();
        try {
            InputStream input = getInputStreamFromFilePath(filePath);
            prolesTicketsJsonImport = mapper.readValue(input, ProlesTicketsJsonImport.class);
        } catch (IOException e) {
            throw new ProlesTicketsJsonImportException(e);
        }
        return prolesTicketsJsonImport.mapToProlesTickets();
    }

    private static InputStream getInputStreamFromFilePath(String filePath) {
        InputStream inputStream = getFileInputStream(filePath);
        if (isNull(inputStream)) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            inputStream = classloader.getResourceAsStream(filePath);
        }
        return requireNonNull(inputStream, "proles ticket input could not be read!");
    }

    private static InputStream getFileInputStream(String filePath) {
        try {
            File file = Paths.get(filePath).toFile();
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // ignore
        }
        return null;
    }
}
