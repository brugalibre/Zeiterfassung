package com.adcubum.timerecording.proles.ticketbacklog.read;

import com.adcubum.timerecording.jira.data.ticket.Ticket;

import java.util.List;

/**
 * The {@link ProlesTicketReader} reads proles specific {@link Ticket}s
 *
 * @author dstalder
 */
public interface ProlesTicketReader {

    /**
     * Reads a list of {@link Ticket} from the given file path, which stores the proles {@link Ticket}s
     *
     * @param filePath the path to a file
     * @return a list of {@link Ticket}s
     */
    List<Ticket> readProlesTicketFromPath(String filePath);
}
