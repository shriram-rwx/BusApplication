package main.java.customerBookingDetails;

import java.io.Serializable;

public class CustomerBookingDetails implements Serializable {
    private static final long serialVersionUID = 5950169519310163575L;
    private String bookingId;
    private String customer;
    private String startPoint;
    private String endPoint;

    private String bus;


    private String date;
    private String seatNumber;

    private int fare;
    public CustomerBookingDetails(){

    }
    public CustomerBookingDetails(String bookingId, String customerId, String startPoint, String endPoint,
                                  String busId, String date, String seats, int fare){
    this.bookingId = bookingId;
    this.customer = customerId;
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    this.bus = busId;
    this.date =  date;
    this.seatNumber = seats;
    this.fare = fare;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }



    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getDate() {
        return date;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }
}
