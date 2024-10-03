package uk.gov.dwp.uc.pairtest.processors;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.services.TicketSummary;

public class InfantTicketProcessor implements TicketProcessor {
    @Override
    public void process(TicketTypeRequest request, TicketSummary summary) {
        int noOfTickets = request.getNoOfTickets();
        summary.incrementInfantTickets(noOfTickets);
    }
}
