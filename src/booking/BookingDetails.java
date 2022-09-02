package booking;


import bus.Bus;
import customerBookingDetails.CustomerDetails;

public interface BookingDetails{
    void updatebookingDetails(String customerId,String boarding_point, String departure_point,
                              String optedBus,String date, String seatNo,int fare);
}
