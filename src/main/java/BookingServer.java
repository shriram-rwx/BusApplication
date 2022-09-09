package main.java;

import main.java.customerBookingDetails.CustomerBookingDetails;
import main.java.test.testDB;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class BookingServer {

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectInputStream inStream = null;
    private testDB td= null;

    private PrintWriter writer = null;
    private static  final int PORT = 2022;
    private static  final int backlog = 100;

    private boolean bookingConflict = false;
    public BookingServer(){

    }

    public void updateTable(){


        System.out.println("   _____________");
        System.out.println(" _/_|[][][][][] | - -");
        System.out.println("(      City Bus | - -");
        System.out.println("=--OO-------OO--=dwb");
        try {
            td = new testDB();
            serverSocket = new ServerSocket(PORT,backlog);
            System.out.println("Booking server Started");
            while(true) {
                socket = serverSocket.accept();
                bookingConflict = false;
                System.out.println("Receiving a connection");
                writer = new PrintWriter(socket.getOutputStream(),true);
                inStream = new ObjectInputStream(socket.getInputStream());
                CustomerBookingDetails customerBookingDetails = (CustomerBookingDetails) inStream.readObject();
                if(null != customerBookingDetails)
                {
                    List<String> bookedSeats = td.fetchSeats(customerBookingDetails.getDate(),customerBookingDetails.getBus(),customerBookingDetails.getStartPoint(),customerBookingDetails.getEndPoint());
                    for(String seat:bookedSeats){
                        if(Arrays.asList(customerBookingDetails.getSeatNumber().split(",")).contains(seat)) {
                            bookingConflict = true;
                            break;
                        }
                    }

                    List<String> bookingIds = td.fetchBookingIds();
                    for(String bookingId:bookingIds){
                        if(customerBookingDetails.getBookingId().equals(bookingId)){
                            bookingConflict = true;
                            break;
                        }
                    }
                    if(!bookingConflict) {
                        td.updateBookingDetails(customerBookingDetails.getBookingId(),
                                customerBookingDetails.getCustomer(),
                                customerBookingDetails.getStartPoint(),
                                customerBookingDetails.getEndPoint(),
                                customerBookingDetails.getBus(),
                                customerBookingDetails.getDate(),
                                Arrays.asList(customerBookingDetails.getSeatNumber().split(",")),
                                customerBookingDetails.getFare());
                        td.logUpdate(customerBookingDetails.getBookingId(),"success");
                        writer.write("Booking successful, you're booking id is " + customerBookingDetails.getBookingId());
                        writer.close();
                        System.out.println("Booking Id is : " + customerBookingDetails.getBookingId());
                    }else{
                        td.logUpdate(customerBookingDetails.getBookingId(),"failure");
                        System.out.println("Seat is currently not available");
                    }
                }else{System.out.println("Received a null object and it cannot be deserialized");}
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Server Error");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.out.println("Server Error, object reading not possible");
            throw new RuntimeException(e);
        }
    }
    public static void main(String args[]){
    BookingServer server = new BookingServer();
    server.updateTable();
    }
}
