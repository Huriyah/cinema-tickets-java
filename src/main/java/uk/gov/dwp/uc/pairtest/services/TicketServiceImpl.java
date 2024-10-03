package uk.gov.dwp.uc.pairtest.services;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.processors.AdultTicketProcessor;
import uk.gov.dwp.uc.pairtest.processors.ChildTicketProcessor;
import uk.gov.dwp.uc.pairtest.processors.InfantTicketProcessor;
import uk.gov.dwp.uc.pairtest.processors.TicketProcessor;

import java.util.HashMap;
import java.util.Map;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;

    private static final int MAX_TICKETS_ALLOWED = 25;

    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService seatReservationService) {
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validateAccountId(accountId);

        TicketSummary summary = calculateTicketSummary(ticketTypeRequests);

        validateTicketLimits(summary);
        processPayment(accountId, summary.getTotalPrice());
        reserveSeats(accountId, summary.getAdultTickets() + summary.getChildTickets());
    }

    private void validateAccountId(Long accountId) throws InvalidPurchaseException {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID.");
        }
    }

    private void validateTicketLimits(TicketSummary summary) throws InvalidPurchaseException {
        if (summary.getTotalTickets() > MAX_TICKETS_ALLOWED) {
            throw new InvalidPurchaseException("Cannot purchase more than 25 tickets.");
        }

        if (summary.getAdultTickets() == 0 && (summary.getChildTickets() > 0 || summary.getInfantTickets() > 0)) {
            throw new InvalidPurchaseException("Child or Infant tickets cannot be purchased without an Adult ticket.");
        }
    }

    private void processPayment(Long accountId, int totalPrice) {
        paymentService.makePayment(accountId, totalPrice);
    }

    private void reserveSeats(Long accountId, int seatsToReserve) {
        seatReservationService.reserveSeat(accountId, seatsToReserve);
    }

    private TicketSummary calculateTicketSummary(TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        TicketSummary summary = new TicketSummary(0, 0, 0, 0, 0);

        Map<TicketTypeRequest.Type, TicketProcessor> ticketProcessorMap = new HashMap<>();
        ticketProcessorMap.put(TicketTypeRequest.Type.ADULT, new AdultTicketProcessor());
        ticketProcessorMap.put(TicketTypeRequest.Type.CHILD, new ChildTicketProcessor());
        ticketProcessorMap.put(TicketTypeRequest.Type.INFANT, new InfantTicketProcessor());

        for (TicketTypeRequest request : ticketTypeRequests) {
            TicketProcessor processor = ticketProcessorMap.get(request.getTicketType());

            if (processor == null) {
                throw new InvalidPurchaseException("Unknown ticket type.");
            }

            processor.process(request, summary);
        }

        return summary;
    }


}


