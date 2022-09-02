package test;

import customer.Customer;
import customerBookingDetails.CustomerBookingDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class testDB {
    private static final String url ="jdbc:mysql://localhost:3306/";
    private static final String userName ="root";
    private static final String passWord= "";


    /*
Immuntiy against sql injection

SELECT * FROM busbooking.customercredentials WHERE idCustomer = 111 AND password = '1187246478'; // with hashing
SELECT * FROM busbooking.customercredentials WHERE idCustomer = 111 AND password = '' or ''=''; // without hashing
*/
    public boolean validateUser(String userId,String password) {
        try {
            String query = "SELECT * FROM busbooking.customercredentials " +
                    "WHERE idCustomer = " + userId +
                    " AND password = " +"'"+password.hashCode()+"'" + ";";
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
            String query = "INSERT INTO `busbooking`.`booking`" +
                    " (`idbooking`, `customerId`, `startPoint`, `endPoint`, `busId`, `date`, " +
                    "`fare`) VALUES (?, ?, ?, ?, ?, ?,?);";
            String seat_query = " INSERT INTO `busbooking`.`seating` (`bookingId`, `seatNo`) VALUES (?,?);";
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,bookingId);
            statement.setString(2,customerId);
            statement.setString(3,startPoint);
            statement.setString(4,endPoint);
            statement.setString(5,busId);
            statement.setString(6,formatDateForDb(date));
            statement.setInt(7,fare);
            int res = statement.executeUpdate();
            if(res != -1) System.out.println("Booking successful");
            else System.out.println("Booking Failed");
            statement = connection.prepareStatement(seat_query);
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

    public  void logUpdate(String bookingId){
        String query = "INSERT INTO `busbooking`.`serverlogs` (`bookingId`) VALUES (?);";
 try{Connection connection = DriverManager.getConnection(url,userName,passWord);
     PreparedStatement statement = connection.prepareStatement(query);
     statement.setString(1,bookingId);
     statement.executeUpdate();
     statement.close();
     connection.close();
 }catch(SQLException e){
     e.printStackTrace();
     System.out.println("Couldn't log data in server");
 }
    }
    public List<String> fetchSeats(String Date,String busId){
        List<String> seatNo = new ArrayList<>();
        try{
            String query =
                    "SELECT *" +
                    "FROM busbooking.booking AS booking " +
                    "JOIN busbooking.seating AS seating " +
                    "ON booking.idbooking = seating.bookingId " +
                    "WHERE booking.date = ? AND " +
                    "booking.busId = ?;";
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,formatDateForDb(Date));
            statement.setString(2,busId);
            ResultSet result = statement.executeQuery();
            if(!result.next()){statement.close(); connection.close(); return seatNo;}
            seatNo.add(result.getString("seatNo"));
            while(result.next()) seatNo.add(result.getString("seatNo"));
            statement.close();
            connection.close();
            return seatNo;
        }catch(SQLException e){
            System.out.println("Couldn't fetch seat details sorry for the inconvinence cause");
            return seatNo;
        }
    }



    public boolean cancelBooking(String bookingId){
        String query = "DELETE FROM `busbooking`.`booking` WHERE (`idbooking` = ?);";
        try{
            Connection connection  = DriverManager.getConnection(url,userName,passWord);
            PreparedStatement statement = connection.prepareStatement(query);
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
        String query = "SELECT * FROM busbooking.booking WHERE customerId=? ORDER BY date,fare DESC;";
        String seat_query = "SELECT * FROM busbooking.seating WHERE bookingId = ?";
        String sum_query = "SELECT SUM(fare) AS TOTAL FROM busbooking.booking WHERE customerId =?;";
        CustomerBookingDetails bookingDetail = null;
        try{
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            PreparedStatement statement = connection.prepareStatement(query);
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
               statement = connection.prepareStatement(seat_query);
               statement.setString(1,result.getString("idbooking"));
               ResultSet seat_result = statement.executeQuery();
               while(seat_result.next()) seatString = seatString + seat_result.getString("seatNo") + ",";
               if(seatString.length() >= 0)
               bookingDetail.setSeatNumber(seatString.substring(0,seatString.length()-1));
               bookingDetail.setFare(result.getInt("fare"));
               bookingDetails.add(bookingDetail);
            }
            statement = connection.prepareStatement(sum_query);
            statement.setString(1,bookingDetail.getCustomer());
            result = statement.executeQuery();
            System.out.println("------------------------------------------------");
            while(result.next()) System.out.println("Money spent on booking:" + result.getString("TOTAL"));
            System.out.println("------------------------------------------------");
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
            ResultSet result = statement.executeQuery("SELECT idbooking FROM busbooking.booking;");
            while(result.next()) bookingIds.add(result.getString("idbooking"));
            statement.close();
            result.close();
        } catch (SQLException e) {
            System.out.println("Error in fetching booking Id details");
        }
        return bookingIds;
    }

    public List<String> fetchCustomerIds(){
        List<String> bookingIds = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url,userName,passWord);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT idCustomer FROM busbooking.customer;");
            while(result.next()) bookingIds.add(result.getString("idCustomer"));
            statement.close();
            result.close();
        } catch (SQLException e) {
            System.out.println("Error in fetching Customer Id details");
        }
        return bookingIds;
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
