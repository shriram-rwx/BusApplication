package main.java.test;


import main.java.Constants.SQLQueries;
import main.java.bus.Bus;
import main.java.customer.Customer;
import main.java.customerBookingDetails.CustomerBookingDetails;
import main.java.routes.Routes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class testDB {
    private static final String url ="jdbc:mysql://localhost:3306/";
    private static final String userName ="root";
    private static final String passWord= "";



    public boolean validateUser(String userId,String password) {
        try {
            String query = "SELECT * FROM busbooking.customercredentials " +
                    "WHERE idCustomer = " + userId +
                    " AND password = " +"'"+password+"'" + ";";
            Connection connection = DriverManager.getConnection(url, userName, passWord);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                System.out.println("You're not a valid user");
                statement.close();
                connection.close();
                return false;
            }
            statement.close();
            connection.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error in validating the customer credentials");
            return  false;
        }
    }

    public Customer fetchCustomer(String userId){
        Customer customer  = null;
        try {
            String query = "SELECT * FROM busbooking.customer " +
                    "WHERE idCustomer = " + "'"+userId + "'"+";";
           // System.out.println(query);
            Connection connection = DriverManager.getConnection(url, userName, passWord);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                System.out.println("record not found");
                statement.close();
                connection.close();
                return null;
            }
            customer = new Customer(result.getString("idCustomer"),result.getString("idName"),result.getString("idGender"),result.getString("idDOB"));
            statement.close();
            connection.close();
            return customer;
        } catch (SQLException e) {
            System.out.println("Error in fetching the customer details");
           return null;
        }
    }

    public boolean createCustomer(Customer customer,String password){
        try {
            // query to update customer details
            String query_1 = "INSERT INTO `busbooking`.`customer` (`idCustomer`, `idName`, `idGender`, `idDOB`)" +
                            " VALUES ("+"'"+
                                    customer.getID()+
                                    "', '" +
                                    customer.getName() +
                                    "', '" +
                                    customer.gender()+
                                    "', '"+
                                    formatDateForDb(customer.dob())+"');"
            ;

            //query to update customer credentials
            String query_2 ="INSERT INTO `busbooking`.`customercredentials`" +
                    " (`idCustomer`, `password`)" +
                    " VALUES ('"+
                    customer.getID()+
                    "', '"+
                    password +
                    "');";

            Connection connection = DriverManager.getConnection(url, userName, passWord);
            Statement statement = connection.createStatement();
            statement.addBatch(query_1);
            statement.addBatch(query_2);
            int result [] = statement.executeBatch();
            for(int i =0;i<result.length;i++){
                if(result[i] == -1){
                    System.out.println("record not found");
                    statement.close();
                    connection.close();
                    return false;
                }
            }
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error in Creating the customer details");
            return false;
        }
    }

    public void updateBookingDetails(String bookingId,String customerId,String startPoint,String endPoint,
                                     String busId,String date,List<String> seats,int fare){
        try{
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            PreparedStatement statement = connection.prepareStatement(SQLQueries.BOOKING_UPDATION_QUERY);
            statement.setString(1,bookingId);
            statement.setString(2,customerId);
            statement.setString(3,startPoint.toUpperCase());
            statement.setString(4,endPoint.toUpperCase());
            statement.setString(5,busId);
            statement.setString(6,formatDateForDb(date));
            statement.setInt(7,fare);
            int res = statement.executeUpdate();
            if(res != -1) System.out.println("Booking successful");
            else System.out.println("Booking Failed");
            statement = connection.prepareStatement(SQLQueries.BOOKING_UPDATION_SEAT_QUERY);
            for(String seatNo : seats){
                statement.setString(1,bookingId);
                statement.setString(2,seatNo);
                statement.addBatch();
            }
            int batch_res[] = statement.executeBatch();
            statement.close();
            connection.close();
        }catch(SQLException e){
            System.out.println("Booking cannot be performed at this point please try again later ");
        }
    }

    public  void logUpdate(String bookingId,String status){
        try{Connection connection = DriverManager.getConnection(url,userName,passWord);
     PreparedStatement statement = connection.prepareStatement(SQLQueries.LOG_UPDATION_QUERY);
     statement.setString(1,bookingId);
     statement.setString(2,status);
     statement.executeUpdate();
     statement.close();
     connection.close();
 }catch(SQLException e){
     e.printStackTrace();
     System.out.println("Couldn't log data in server");
 }
    }

    public List<String> fetchSeats(String Date,String busId,String startPoint,String endPoint){
        List<String> seatNo = new ArrayList<>();
        int stationOrder_1 = 0;
        int stationOrder_2 = 0;
        try{
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            PreparedStatement statement = connection.prepareStatement(SQLQueries.FETCHING_SEAT_STATION_ORDER_QUERY);
            statement.setString(1,busId);
            statement.setString(2,startPoint);
            ResultSet result = statement.executeQuery();
            if(result.next()) stationOrder_1 = result.getInt("order");
            statement.setString(1,busId);
            statement.setString(2,endPoint);
            result = statement.executeQuery();
            if(result.next()) stationOrder_2 = result.getInt("order");
            statement = connection.prepareStatement(SQLQueries.FETCHING_BOOKED_SEATS_QUERY);
            statement.setInt(1,stationOrder_1);
            statement.setString(2,formatDateForDb(Date));
            statement.setString(3,busId);
            statement.setInt(4,stationOrder_2);
            result = statement.executeQuery();
            while(result.next()){
                seatNo.add(result.getString("seatNo"));
            }
            statement.close();
            connection.close();
        }catch(SQLException e){
            System.out.println("Error in fetching seats");
        }
        return seatNo;
    }



    public boolean cancelBooking(String bookingId){
         try{
            Connection connection  = DriverManager.getConnection(url,userName,passWord);
            PreparedStatement statement = connection.prepareStatement(SQLQueries.CANCEL_BOOKING_QUERY);
            statement.setString(1,bookingId);
            int res = statement.executeUpdate();
            if(res == -1){
                statement.close();
                connection.close();
                return false;
            }else if(res == 0) {
                System.out.println("Invalid booking id");
                statement.close();
                connection.close();
                return false;
            }
            statement.close();
            connection.close();
        }catch(SQLException e){
            System.out.println("Booking cannot be canceled");
        }
        return true;
    }
    public List<CustomerBookingDetails> fetchBookingDetails(String customerId){
        List<CustomerBookingDetails> bookingDetails = new ArrayList<>();
        CustomerBookingDetails bookingDetail = null;
        try{
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            PreparedStatement statement = connection.prepareStatement(SQLQueries.FETCH_CUSTOMER_BOOKING_DETAILS_QUERY);
            statement.setString(1,customerId);
            ResultSet result = statement.executeQuery();
            while (result.next()){
               String seatString = "";
               bookingDetail = new CustomerBookingDetails();
               bookingDetail.setBookingId(result.getString("idbooking"));
               bookingDetail.setCustomer(result.getString("customerId"));
               bookingDetail.setStartPoint(result.getString("startPoint"));
               bookingDetail.setEndPoint(result.getString("endPoint"));
               bookingDetail.setBus(result.getString("busID"));
               bookingDetail.setDate(formatDateForObject(String.valueOf(result.getDate("date"))));
               statement = connection.prepareStatement(SQLQueries.FETCH_SEAT_BOOKING_DETAILS_QUERY);
               statement.setString(1,result.getString("idbooking"));
               ResultSet seat_result = statement.executeQuery();
               while(seat_result.next()) seatString = seatString + seat_result.getString("seatNo") + ",";
               if(seatString.length() >= 0)
               bookingDetail.setSeatNumber(seatString.substring(0,seatString.length()-1));
               bookingDetail.setFare(result.getInt("fare"));
               bookingDetails.add(bookingDetail);
            }
            if(null != bookingDetail) {
                statement = connection.prepareStatement(SQLQueries.WALLET_FARE_QUERY);
                statement.setString(1, bookingDetail.getCustomer());
                result = statement.executeQuery();
                System.out.println("------------------------------------------------");
                while (result.next()) System.out.println("Money spent on booking:" + result.getString("TOTAL"));
                System.out.println("------------------------------------------------");
            }
            statement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e);
            System.out.println("Couldn't fetch booking details for Customer");
        }
        return bookingDetails;
    }


    public List<String> fetchBookingIds(){
        List<String> bookingIds = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQLQueries.FETCH_BOOKING_ID_QUERY);
            while(result.next()) bookingIds.add(result.getString("idbooking"));
            statement.close();
            result.close();
        } catch (SQLException e) {
            System.out.println("Error in fetching booking Id details");
        }
        return bookingIds;
    }

    public List<String> fetchCustomerIds(){
        List<String> customerIds = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQLQueries.FETCH_CUSTOMER_ID_QUERY);
            while(result.next()) customerIds.add(result.getString("idCustomer"));
            statement.close();
            result.close();
        } catch (SQLException e) {
            System.out.println("Error in fetching Customer Id details");
        }
        return customerIds;
    }

    public List<Bus> fetchBuses(String startPoint, String endPoint){
        List<Bus>availablebuses = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            PreparedStatement statement = connection.prepareStatement(SQLQueries.FETCH_AVAILABLE_BUSES);
            statement.setString(2,startPoint);
            statement.setString(1,endPoint);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                availablebuses.add(new Bus(result.getString("BusId"),
                        result.getString("StartTime"),
                        result.getString("EndTime"),
                        new Routes(result.getString("RouteId"),
                                result.getInt("FARE"),
                                result.getString("Source"),
                                result.getString("Destination"))));
            }
            statement.close();
            connection.close();
        }catch (SQLException e) {
            System.out.println("Error in fetching bus details");
        }
        return availablebuses;
    }
    public static String  formatDateForDb(String Date){
      String resultDate = "";
      resultDate = Date.substring(6) + "-"+Date.substring(3,5) + "-"+Date.substring(0,2);
     return resultDate;
    }

    public static String formatDateForObject(String Date){
        String resultDate = "";
        resultDate = Date.substring(8) + "/" + Date.substring(5,7) + "/" + Date.substring(0,4);
        return resultDate;
    }
}
