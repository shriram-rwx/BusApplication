package booking;

import bus.Bus;
import customer.Customer;
import customerBookingDetails.CustomerBookingDetails;
import customerBookingDetails.CustomerDetails;

public class CustomerBookingDetailsImpl implements BookingDetails {

    public void updatebookingDetails(String customerId,String boarding_point, String departure_point,
                                     String optedBus,String date, String seatNo,int fare){

    }

    public CustomerBookingDetails updatebookingDetails(int fare,String customerId,String boarding_point, String departure_point,
                                     String optedBus,String date, String seatNo){
    CustomerBookingDetails customerBookingDetails = new CustomerBookingDetails();
        customerBookingDetails.setFare(fare);
        customerBookingDetails.setCustomer(customerId);
        customerBookingDetails.setStartPoint(boarding_point);
        customerBookingDetails.setEndPoint(departure_point);
        customerBookingDetails.setBus(optedBus);
        customerBookingDetails.setDate(date);
        customerBookingDetails.setSeatNumber(seatNo);
    return customerBookingDetails;
    }
}