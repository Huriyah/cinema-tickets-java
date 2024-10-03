package uk.gov.dwp.uc.pairtest.services;

public class TicketSummary {
    private int totalTickets;
    private int adultTickets;
    private int childTickets;
    private int infantTickets;
    private int totalPrice;

    private static final int ADULT_TICKET_PRICE = 25;
    private static final int CHILD_TICKET_PRICE = 15;

    public TicketSummary(int totalTickets, int adultTickets, int childTickets, int infantTickets, int totalPrice) {
        this.totalTickets = totalTickets;
        this.adultTickets = adultTickets;
        this.childTickets = childTickets;
        this.infantTickets = infantTickets;
        this.totalPrice = totalPrice;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getAdultTickets() {
        return adultTickets;
    }

    public int getChildTickets() {
        return childTickets;
    }

    public int getInfantTickets() {
        return infantTickets;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void incrementAdultTickets(int count) {
        this.adultTickets += count;
        this.totalTickets += count;
        this.totalPrice += count * ADULT_TICKET_PRICE;
    }

    public void incrementChildTickets(int count) {
        this.childTickets += count;
        this.totalTickets += count;
        this.totalPrice += count * CHILD_TICKET_PRICE;
    }

    public void incrementInfantTickets(int count) {
        this.infantTickets += count;
        this.totalTickets += count;
    }
}
