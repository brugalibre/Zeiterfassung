package com.adcubum.timerecording.messaging.receive.mapping.impl;

import com.adcubum.timerecording.messaging.receive.mapping.TicketMapper;

/**
 * THe {@link NOOPMapper} is a no-operation mapper
 * and maps therefore nothing. He passes the given values 1:1 to the caller
 * @author dstalder
 */
public class NOOPMapper implements TicketMapper {
   @Override
   public String mapTicketNr(String ticketNr) {
      return ticketNr;
   }

   @Override
   public String mapTicketActivityCode(String ticketActivityName) {
      return ticketActivityName;
   }
}
