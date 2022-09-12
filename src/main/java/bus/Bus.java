package main.java.bus;



import main.java.routes.Routes;

import java.util.*;
public class Bus{
    private String NumberPlate;
    private String startTime;
    private String endTime;

    private Routes route;


    public Bus(){}

    public Bus(String NumberPlate,String startTime,String endTime,Routes route){
        this.NumberPlate = NumberPlate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.route = route;
    }

    public Bus(String NumberPlate,String startTime,String endTime) {
        this.NumberPlate = NumberPlate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Routes getRoute() {
        return route;
    }

    public void setRoute(Routes route) {
        this.route = route;
    }

    public String getNumberPlate(){
        return NumberPlate;
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



    public void startTime(String startTime){
        this.startTime =  startTime;
    }

    public void endTime(String endTime){
        this.endTime = endTime;
    }
}