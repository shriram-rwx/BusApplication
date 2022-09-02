package bus;

import java.util.*;
public class Bus{
    private String NumberPlate;
    private String startTime;
    private String endTime;
    private int[][] seating = new int[10][4];
    private String busType;//will implemet this later
    private List<String> routes;

    private List<String> bookings;

    public Bus(){}

    public Bus(String NumberPlate,String startTime,String endTime){
        this.NumberPlate = NumberPlate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getNumberPlate(){
        return NumberPlate;
    }

    public List<String> getRoutes(){
        return routes;
    }


/*	public int getStartPoint(){
		return StartPoint;
	}

	public int getDestinationPoint(){
		return DestinationPoint;
	}*/

    public List<String> getBookings() {
        return bookings;
    }

    public void setBookings(List<String> bookings) {
        this.bookings = bookings;
    }

    public void setRoutes(List<String> routes){
        this.routes =  routes;
    }

    public String getStartTime(){
        return startTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public void setNumberPlate(String NumberPlate){
        this.NumberPlate = NumberPlate;
    }

    /*public void setStartPoint(int StartPoint){
        this.StartPoint =  StartPoint;
    }

    public void setDestinationpoint(int DestinationPoint){
        this.DestinationPoint = DestinationPoint;
    }
    */

    public int[][] getSeating() {
        return seating;
    }

    public void setSeating(int[][] seating) {
        this.seating = seating;
    }

    public void startTime(String startTime){
        this.startTime =  startTime;
    }

    public void endTime(String endTime){
        this.endTime = endTime;
    }
}