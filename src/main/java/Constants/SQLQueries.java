package main.java.Constants;

public class SQLQueries {

    public final static String FETCHING_SEAT_STATION_ORDER_QUERY=
            "SELECT routes.station_order AS `order`" +
                    " FROM busbooking.bus  AS bus JOIN " +
                    "busbooking.routes AS routes " +
                    "ON bus.routeId = routes.route_id "+
                    "WHERE bus.id = ? and routes.station_id = ?;";
    public final static String FETCHING_BOOKED_SEATS_QUERY =    "SELECT seatNo FROM busbooking.seating WHERE " +
            "bookingId IN (" +
            "SELECT booking.idbooking FROM busbooking.booking AS booking " +
            "JOIN busbooking.bus AS bus " +
            "ON bus.id = booking.busId " +
            "JOIN busbooking.routes AS routes_1  " +
            "ON (bus.routeId = routes_1.route_id AND booking.startPoint = routes_1.station_id) " +
            "JOIN busbooking.routes AS routes_2 " +
            "ON (bus.routeId = routes_2.route_id AND booking.endPoint = routes_2.station_id) " +
            "WHERE routes_2.station_order > ?   AND " +
            "(booking.date = ? and booking.busId=?) AND +" +
            "booking.idbooking NOT IN " +
                "(SELECT booking.idbooking FROM busbooking.booking AS booking " +
                        "JOIN busbooking.bus AS bus " +
                        "ON bus.id = booking.busId " +
                        "JOIN busbooking.routes AS routes_1  " +
                        "ON (bus.routeId = routes_1.route_id AND booking.startPoint = routes_1.station_id) " +
                        "JOIN busbooking.routes AS routes_2 " +
                        "ON (bus.routeId = routes_2.route_id AND booking.endPoint = routes_2.station_id) " +
                        "WHERE routes_1.station_order >= ?));";
    public final static String CANCEL_BOOKING_QUERY = "DELETE FROM `busbooking`.`booking` WHERE (`idbooking` = ?);";
    public final static String FETCH_CUSTOMER_BOOKING_DETAILS_QUERY = "SELECT * FROM busbooking.booking WHERE customerId=? ORDER BY date,fare DESC;";
    public  static String FETCH_SEAT_BOOKING_DETAILS_QUERY = "SELECT * FROM busbooking.seating WHERE bookingId = ?";
    public final static String WALLET_FARE_QUERY = "SELECT SUM(fare) AS TOTAL FROM busbooking.booking WHERE customerId =?;";
    public final static String FETCH_BOOKING_ID_QUERY = "SELECT idbooking FROM busbooking.booking;";
    public final static String FETCH_CUSTOMER_ID_QUERY = "SELECT idCustomer FROM busbooking.customer;";
    public final static String FETCH_AVAILABLE_BUSES = "SELECT bus.id AS BusId, " +
            "bus.startTime AS StartTime, " +
            "bus.endTime AS EndTime,    " +
            "r1.route_id AS RouteId,    " +
            "((r1.station_order - r2.station_order)*75) AS FARE,    " +
            "r2.station_id AS Source,   " +
            "r1.station_id AS Destination   "   +
            "FROM busbooking.routes AS r1   "  +
            "JOIN busbooking.routes AS r2   " +
            "ON (r1.station_order > r2.station_order " +
            " AND r1.route_id = r2.route_id) " +
            "JOIN busbooking.bus AS bus     "+
            "ON bus.routeId = r1.route_id " +
            "WHERE r1.station_id = ?  and   " +
            "r2.station_id = ?  "   +
            "GROUP BY r1.route_id   "   +
            "ORDER BY BusId;" ;
    public final static String BOOKING_UPDATION_QUERY = "INSERT INTO `busbooking`.`booking`" +
            " (`idbooking`, `customerId`, `startPoint`, `endPoint`, `busId`, `date`, " +
            "`fare`) VALUES (?, ?, ?, ?, ?, ?,?);";
    public final static String BOOKING_UPDATION_SEAT_QUERY =  " INSERT INTO `busbooking`.`seating` (`bookingId`, `seatNo`) VALUES (?,?);";
    public final static  String LOG_UPDATION_QUERY =  "INSERT INTO `busbooking`.`serverlogs` (`bookingId`,`bookingStatus`) VALUES (?,?);";


}
