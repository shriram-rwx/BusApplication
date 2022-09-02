package booking;


import busBookingDetails.BusBookingDetails;
import busBookingDetails.BusDetails;
import customerBookingDetails.CustomerBookingDetails;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Booking {
    public Map<String, BusDetails> BusMap;// key is date , value is bus details
    public Map<String, CustomerBookingDetails> customerBookingDetails;//key is bookingID , value is customer booking details
    public Map<String, BusBookingDetails> busBookingDetails;
    public BusDetails busDetails;
    //creating a booking info for 30 days
    public Booking(Calendar calendar) throws IOException {
        BusMap = new HashMap<>();
        for(int i=0;i<60;i++) {
            calendar.add(Calendar.DATE, 1);
            if (i > 30){
                busDetails = new BusDetails();
                String Date;
                String Day = String.valueOf(calendar.get(Calendar.DATE));
                String Month  = String.valueOf(calendar.get(Calendar.MONTH));
                if(Day.length()!=2 || Month.length()!=2){
                    Date = (Day.length()!=2) ? "0" + Day + "/" : String.valueOf(calendar.get(Calendar.DATE)) + "/";
                    Date = ( Month.length()!=2) ? Date + "0" + Month + "/":Date + String.valueOf(calendar.get(Calendar.MONTH)) + "/";
                }else {
                    Date = String.valueOf(calendar.get(Calendar.DATE)) + "/";
                    Date = Date + String.valueOf(calendar.get(Calendar.MONTH)) + "/";
                }
                Date = Date + String.valueOf(calendar.get(Calendar.YEAR));
                busDetails.testBusCreate();
                BusMap.put(Date, busDetails);
            }
        }
    }
}
