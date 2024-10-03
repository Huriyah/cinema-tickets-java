import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.services.TicketServiceImpl;

import static org.mockito.Mockito.verify;

public class TicketServiceImplTest {

    private TicketPaymentService paymentService;
    private SeatReservationService seatReservationService;
    private TicketServiceImpl ticketService;

    @BeforeEach
    public void setUp() {
        paymentService = Mockito.mock(TicketPaymentService.class);
        seatReservationService = Mockito.mock(SeatReservationService.class);

        ticketService = new TicketServiceImpl(paymentService, seatReservationService);
    }

    @Test
    public void testPurchaseTickets_Success() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        ticketService.purchaseTickets(1L, adultRequest, childRequest);

        verify(paymentService).makePayment(1L, 65);
        verify(seatReservationService).reserveSeat(1L, 3);
    }

    @Test
    public void testPurchaseTickets_TooManyTickets() {
        TicketTypeRequest[] requests = new TicketTypeRequest[26];
        for (int i = 0; i < 26; i++) {
            requests[i] = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        }

        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, requests);
        });
    }

    @Test
    public void testPurchaseTickets_NoAdultTicket() {
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, childRequest);
        });
    }

    @Test
    public void testPurchaseTickets_InvalidAccountId() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(-1L, adultRequest);
        });
    }

    @Test
    public void testPurchaseTickets_OnlyInfant() {
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, infantRequest);
        });
    }

    @Test
    public void testPurchaseTickets_MixedWithoutAdult() {
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, childRequest, infantRequest);
        });
    }

    @Test
    public void testPurchaseTickets_MaximumTickets() throws InvalidPurchaseException {
        TicketTypeRequest[] requests = new TicketTypeRequest[25];
        for (int i = 0; i < 25; i++) {
            requests[i] = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        }

        ticketService.purchaseTickets(1L, requests);

        verify(paymentService).makePayment(1L, 625);
        verify(seatReservationService).reserveSeat(1L, 25);
    }

    @Test
    public void testPurchaseTickets_SingleInfant() {
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, infantRequest);
        });
    }


    @Test
    public void testPurchaseTickets_UnknownTicketType() {
        TicketTypeRequest unknownRequest = new TicketTypeRequest(null, 1);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, unknownRequest);
        });
    }

    @Test
    public void testPurchaseTickets_TwoAdultsAndOneInfant() throws InvalidPurchaseException {

        TicketTypeRequest adultRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest adultRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        ticketService.purchaseTickets(1L, adultRequest1, adultRequest2, infantRequest);

        verify(paymentService).makePayment(1L, 50);
        verify(seatReservationService).reserveSeat(1L, 2);
    }

    @Test
    public void testPurchaseMultipleTickets_Success() throws InvalidPurchaseException {
        // Arrange: Prepare multiple ticket requests
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);  // 2 Adults
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);  // 3 Children
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1); // 1 Infant

        ticketService.purchaseTickets(1L, adultRequest, childRequest, infantRequest);

        verify(paymentService).makePayment(1L, 95);

        verify(seatReservationService).reserveSeat(1L, 5);
    }




}
