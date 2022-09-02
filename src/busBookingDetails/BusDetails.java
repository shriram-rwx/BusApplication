package busBookingDetails;

import bus.*;
import com.mysql.jdbc.Driver;

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

    //for testing purpose adding 5 buses in a list with number plate number 101 to 105
    public void testBusCreate()  {
        //Bus one chennai to trichy

        try {
            String query = "SELECT * FROM busbooking.bus;";
            String line = null;
            BufferedReader reader = new BufferedReader(new FileReader(CSV_PATH));
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            Statement statement  = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            while(result.next()) {
                buses.add(new Bus(result.getString("id"), result.getString("startTime"), result.getString("endTime")));
            }
            line = reader.readLine();// checking our file has atleast one value
            if(line == null){System.out.println("error in mapping buses and routes"); return;}
            for(Bus bus: buses){
                bus.setRoutes(Arrays.asList(line.split(",")));
                if((line = reader.readLine()) == null){
                    reader.close();
                    reader = new BufferedReader(new FileReader(CSV_PATH));
                    line = reader.readLine();
                };
            }
            reader.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error in creating test bus data");
        } catch (FileNotFoundException e) {
            System.out.println("Error in  loading routes");
        } catch (IOException e) {
           // e.printStackTrace();
           // System.out.println("Sorry some problem from our end , will get back to you soon :(");
        }

       /* List<String> routes_1 = new LinkedList<>();
        Bus bus_1 = new Bus("101","9:00AM","4:00PM");
        routes_1.add("Chennai-CMBT");
        routes_1.add("Tambaram");
        routes_1.add("Perungalathur");
        routes_1.add("Chengalpet");
        routes_1.add("Vilupuram");
        routes_1.add("Trichy");
        bus_1.setRoutes(routes_1);
        buses.add(bus_1);

        //Bus two chennai to bangalore
        List<String> routes_2 = new LinkedList<>();
        routes_2.add("Chennai-CMBT");
        routes_2.add("Kanchipuram");
        routes_2.add("Vellore");
        routes_2.add("Krishnagiri");
        routes_2.add("Banglore");
        Bus bus_2 = new Bus("102","12:00noon","6:00PM");
        bus_2.setRoutes(routes_2);
        buses.add(bus_2);

        //Bus three chennai to neyveli
        List<String> routes_3 =  new LinkedList<>();
        routes_3.add("Chennai-CMBT");
        routes_3.add("Kanchipuram");
        routes_3.add("Chengalpet");
        routes_3.add("Panruti");
        routes_3.add("Neyveli");
        Bus bus_3 = new Bus("103","1:00PM","7:00PM");
        bus_3.setRoutes(routes_3);
        buses.add(bus_3);

        //Bus four chennai to tirunelveli
        List<String> routes_4 = new LinkedList<>();
        routes_4.add("Chennai-CMBT");
        routes_4.add("Chengalpet");
        routes_4.add("Trichy");
        routes_4.add("Madurai");
        routes_4.add("Tirunelveli");
        Bus bus_4 = new Bus("104","8:00PM","7:00AM");
        bus_4.setRoutes(routes_4);
        buses.add(bus_4);

        //Bus five chennai to Thoothukudi
        List<String> routes_5 = new LinkedList<>();
        routes_5.add("Chennai-CMBT");
        routes_5.add("Chengalpet");
        routes_5.add("Trichy");
        routes_5.add("Madurai");
        routes_5.add("Tirunelveli");
        routes_5.add("Thoothukudi");
        Bus bus_5 = new Bus("105","9:00PM","8:00AM");
        bus_5.setRoutes(routes_5);
        buses.add(bus_5);
*/
    }

    public List<Bus> availableBuses(String startPoint, String endPoint,List<Bus> buses){
        final int  FARE = 75;
        List<Bus> routing = new ArrayList<>();
        for(Bus bus : buses){
            if(bus.getRoutes().contains(startPoint) && bus.getRoutes().contains(endPoint) ){
                if(bus.getRoutes().indexOf(endPoint) - bus.getRoutes().indexOf(startPoint) > 0){
                    routing.add(bus);
                }else return routing;
            }
        }
        if(!routing.isEmpty()) return routing;
    System.out.println("Start/end Point invalid");
    return null;
    }

    public void printBusDetails(List<Bus> buses,List<Integer> fare){
        int i=0;
        for(Bus bus:buses){
            System.out.println("------------------");
            System.out.println("Bus number:" + bus.getNumberPlate());
            System.out.println("Bus start Time:" + bus.getStartTime());
            System.out.println("Bus end Time:" +bus.getEndTime());
            System.out.println("Fare is:" + fare.get(i++));
            System.out.print("Routes");
            for(String routes : bus.getRoutes()){
                System.out.print("->"+routes);
            }
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



