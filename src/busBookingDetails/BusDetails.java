package busBookingDetails;

import bus.*;
import com.mysql.jdbc.Driver;
import routes.Routes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.io.*;
import java.sql.*;
//BusDetails class consists Bus list
public class BusDetails {

    private static final String url ="jdbc:mysql://localhost:3306/";
    private static final String userName ="root";
    private static final String passWord= "";

    private static String CSV_PATH = "C:\\Users\\shriram-15495\\IdeaProjects\\BusApplication\\src\\busBookingDetails\\busBookingDetails\\routes.csv";
    List<Bus> buses = new ArrayList<>();




    public void printBusDetails(List<Bus> buses){
        for(Bus bus:buses){
            System.out.println("------------------");
            System.out.println("Bus number:" + bus.getNumberPlate());
            System.out.println("Bus start Time:" + bus.getStartTime());
            System.out.println("Bus end Time:" +bus.getEndTime());
            System.out.println("Fare is:" + bus.getRoute().getFare());
            System.out.println();
        }
    }

    public List<Bus> getBuses() {
        return buses;
    }

    public void setBuses(List<Bus> buses) {
        this.buses = buses;
    }
}



