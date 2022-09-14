package main.java;

import main.java.bus.Bus;
import main.java.customer.Customer;
import main.java.customerBookingDetails.CustomerBookingDetails;
import main.java.test.testDB;

import java.io.*;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class BusApplicationHelper {
    private boolean isConnected = false;
    private Socket socket = null;
    private BufferedReader inComing = null;
    private ObjectOutputStream outputStream = null;

    private PrintWriter writer = null;
    private static final int   PORT = 2022;

    private testDB testDb = new testDB();

    public  void updateBooking(String customerId,String boarding_point, String departure_point,
                               String optedBus,String date, String seatNo,int fare){
        testDB td = getTestDb();
        List<String> bookingIds = td.fetchBookingIds();
        String bookingId;
        final String PREFIX = "111";
        do{
            bookingId = PREFIX + date.replace("/","") + String.valueOf(new Random().nextInt(1111));
        }while(bookingIds.contains(bookingId));
        CustomerBookingDetails customerBookingDetails = new CustomerBookingDetails(bookingId,customerId,boarding_point,departure_point,optedBus,date,seatNo,fare);
        String result = connectServer(customerBookingDetails);
        if(null != result) System.out.println(result);
    }

    public String connectServer(CustomerBookingDetails customerBookingDetails){
        String result = null;
            try {
                socket = new Socket("localhost",PORT);
                socket.setSoTimeout(10*1000);
                System.out.println("Request being processed by server");
                outputStream = new  ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(customerBookingDetails);
                
                outputStream.flush();
                inComing = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                result = inComing.readLine();
                inComing.close();
                outputStream.close();
                socket.close();
            }catch(ConnectException e){
                System.out.println("Server is currently un available , please try again later");
                return null;
            }
            catch (IOException e) {
                System.out.println("Error in reaching Server");
                throw new RuntimeException(e);
            }
        return result;
    }
    public  String seatAllocation(String date, String busNumber,String startingPoint,String endPoint){
        testDB td = getTestDb();
        Scanner sc =  new Scanner(System.in);
        CustomerBookingDetails customerBookingDetails = null;
        int numberOfSeats;
        String seatNo = "";
        try {
            customerBookingDetails = new CustomerBookingDetails();
            customerBookingDetails.setDate(date);
            customerBookingDetails.setBus(busNumber);
            customerBookingDetails.setStartPoint(startingPoint);
            customerBookingDetails.setEndPoint(endPoint);
            List<String> allocated = td.fetchSeats(customerBookingDetails);
            int num = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 4; j++) {
                    if (allocated.contains(String.valueOf(++num))) System.out.print("X\t\t");
                    else System.out.print(num + "\t\t");
                }
                System.out.println();
            }
            System.out.print("Enter number of seats to book:");
            numberOfSeats = sc.nextInt();
            if(numberOfSeats > 6 || numberOfSeats <= 0){ System.out.println("Only 6 seats can be booked at a time"); return null;}
            for(int i=0;i<numberOfSeats;i++) {
                System.out.print("Enter a seat number:");
                int selectedSeat = sc.nextInt();
                if (allocated.contains(String.valueOf(selectedSeat)) || ((Integer.valueOf(String.valueOf(selectedSeat)) > 40) || (Integer.valueOf(String.valueOf(selectedSeat)) < 0))) {
                    System.out.println("Error in booking seat");
                    return null;
                }
                seatNo = seatNo + selectedSeat + ",";
            }
            System.out.println("Seat Seems to be Available");
            return seatNo;
        }catch(InputMismatchException e){
            System.out.println("Invalid input provided");
            return null;
        }
    }


    public  void showBookings(String customerId)
    {
        testDB td = getTestDb();
        List<CustomerBookingDetails> bookingDetails = td.fetchBookingDetails(customerId);
        if (null ==  bookingDetails || bookingDetails.isEmpty()){ System.out.println("No booking details available"); return;}
        int i = 0;
        if(!bookingDetails.isEmpty()) {
            for (CustomerBookingDetails bookingDetail : bookingDetails) {
                System.out.println(++i);// may be the booking index of the customer
                System.out.println("-----------------------------------------------------------");
                System.out.println("Booking id is : " + bookingDetail.getBookingId());
                System.out.println("Booking Date is : " + bookingDetail.getDate());
                System.out.println("Booked bus number is : " + bookingDetail.getBus());
                System.out.println("Journey starting at: " + bookingDetail.getStartPoint());
                System.out.println("Journey ending at: " + bookingDetail.getEndPoint());
                System.out.println("Allocated seat number is : " + bookingDetail.getSeatNumber());
                System.out.println("Journey Fare is : " + bookingDetail.getFare());
                Customer customer = td.fetchCustomer(bookingDetail.getCustomer());
                System.out.println("Booked by:" + customer.getName());
                System.out.println("-----------------------------------------------------------");
            }
        }
    }

    public List<Bus>  fetchBuses(String startPoint, String endPoint){
        testDB td = getTestDb();
        List<Bus> availableBuses = td.fetchBuses(startPoint,endPoint);
        return availableBuses;
    }
    public void cancelBooking(String customerId){
        System.out.println("Available booking details are");
        showBookings(customerId);
        testDB td = getTestDb();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter booking ID to cancel: ");
        if(td.cancelBooking(sc.nextLine())) System.out.println("Booking canceled");
        else System.out.println("Cancellation of booking failed");
    }
    public boolean validBookingDate(String date){
        Calendar calendar = Calendar.getInstance();
        List<String> availableDates = new ArrayList<>();
        for(int i=0;i<60;i++) {
            calendar.add(Calendar.DATE, 1);
            if (i >= 30){
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
                availableDates.add(Date);
            }
        }
        return availableDates.contains(date);
    }

    public boolean validateUser(String id,String passWord){
        return getTestDb().validateUser(id,passWord);
    }

    public Customer getCustomer(String id){
        return getTestDb().fetchCustomer(id);
    }
    public String md5Password(String password){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md5.digest(password.getBytes());
            BigInteger number =new BigInteger(1,messageDigest);
            String hashText = number.toString(16);
            while (hashText.length() < 16)hashText = "0" + hashText;
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error in hashing the password");
            throw new RuntimeException(e);
        }
    }

    public void printRoutes(){
        System.out.println();
        System.out.println("We are Operating in");
        System.out.println("|Chennai-CMBT->Tambaram->Perungalathur->Chengalpet->Vilupuram->Trichy|");
        System.out.println("|Chennai-CMBT->Kanchipuram->Vellore->Krishnagiri->Banglore|");
        System.out.println("|Chennai-CMBT->Kanchipuram->Chengalpet->Panruti->Neyveli|");
        System.out.println("|Chennai-CMBT->Chengalpet->Vilupuram->Trichy->Madurai->Tirunelveli|");
        System.out.println("|Chennai-CMBT->Chengalpet->Trichy->Madurai->Tirunelveli->Thoothukudi|");
        System.out.println();
        System.out.println();
    }

    public void printBus(){
        System.out.println("                          __");
        System.out.println(" .-----------------------'  |");
        System.out.println("/| _ .---. .---. .---. .---.|");
        System.out.println("|j||||___| |___| |___| |___||");
        System.out.println("|=|||=======================|");
        System.out.println("[_|j||(O)\\__________|(O)\\___]");
        System.out.println();
    }

    public void printBusDetails(List<Bus> buses) {
        for (Bus bus : buses) {
            System.out.println("------------------");
            System.out.println("Bus number:" + bus.getNumberPlate());
            System.out.println("Bus start Time:" + bus.getStartTime());
            System.out.println("Bus end Time:" + bus.getEndTime());
            System.out.println("Fare is:" + bus.getRoute().getFare());
            System.out.println();
        }
    }
    public testDB getTestDb() {
        return testDb;
    }

    public void setTestDb(testDB testDb) {
        this.testDb = testDb;
    }
}
