package main.java.routes;

public class Routes {
    private String route_id;
    private int fare;
    private String start_point;
    private String end_point;

    public Routes() {

    }

    public Routes(String route_id, int fare, String start_point, String end_point) {
        this.route_id = route_id;
        this.fare = fare;
        this.start_point = start_point;
        this.end_point = end_point;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public String getStart_point() {
        return start_point;
    }

    public void setStart_point(String start_point) {
        this.start_point = start_point;
    }

    public String getEnd_point() {
        return end_point;
    }

    public void setEnd_point(String end_point) {
        this.end_point = end_point;
    }
}