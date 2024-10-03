package uk.gov.dwp.uc.pairtest.processors;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.services.TicketSummary;

public interface TicketProcessor {
    void process(TicketTypeRequest request, TicketSummary summary);
}

