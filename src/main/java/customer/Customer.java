package main.java.customer;
import java.util.*;
public class Customer{
    private String ID;
    private String Name;
    private String gender;
    private String dob;

    private List<String> bookings = new ArrayList<>();

    public Customer(){

    }

    public Customer(String ID,String Name,String gender,String dob){
        this.ID = ID;
        this.Name = Name;
        this.gender = gender;
        this.dob = dob;
    }

    public String getID(){
        return ID;
    }

    public List<String> getBookings() {
        return bookings;
    }

    public void setBookings(List<String> bookings) {
        this.bookings = bookings;
    }

    public String getName(){
        return Name;
    }

    public String gender(){
        return gender;
    }

    public String dob(){
        return dob;
    }

    public void setId(String ID){
        this.ID = ID;
    }

    public void setName(String Name){
        this.Name = Name;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public void setDOB(String dob){
        this.dob = dob;
    }
}