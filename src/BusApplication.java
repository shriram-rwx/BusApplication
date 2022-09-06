import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import customerBookingDetails.*;
import bus.*;
import busBookingDetails.*;
import customer.*;
import booking.*;
import test.testDB;


class BusApplication extends  Thread {

     public static void main(String args[])
    {
        try{
            CustomerDetails cd =  new CustomerDetails();
            startApplication(cd);
        }
        catch(Exception exc){
            exc.printStackTrace();
            System.out.println(exc);
        }

    }

    private static void startApplication(CustomerDetails cd){

//customer validation
        Scanner sc = new Scanner(System.in);
        int choice = 0;//setting the choice to 0 which is a default condition

        System.out.println("We are Operating in");
        System.out.println("|Chennai-CMBT->Tambaram->Perungalathur->Chengalpet->Vilupuram->Trichy|");
        System.out.println("|Chennai-CMBT->Kanchipuram->Vellore->Krishnagiri->Banglore|");
        System.out.println("|Chennai-CMBT->Kanchipuram->Chengalpet->Panruti->Neyveli|");
        System.out.println("|Chennai-CMBT->Chengalpet->Vilupuram->Trichy->Madurai->Tirunelveli|");
        System.out.println("|Chennai-CMBT->Chengalpet->Trichy->Madurai->Tirunelveli->Thoothukudi|");

                    System.out.println("                          __");
                    System.out.println(" .-----------------------'  |");
                    System.out.println("/| _ .---. .---. .---. .---.|");
                    System.out.println("|j||||___| |___| |___| |___||");
                    System.out.println("|=|||=======================|");
                    System.out.println("[_|j||(O)\\__________|(O)\\___]");
                    System.out.println();

        System.out.println(" _______________________");
        System.out.println("|Bus Booking Application|");
        System.out.println(" -----------------------");

        do {
            try {
                System.out.println("-> Press 1 for Sign In");
                System.out.println("-> Press 2 for Creating Customer Account");
                System.out.println("-> Press 3 for Quit");
                System.out.print("You're choice ??? :");
                choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        customerSignIn();
                        break;
                    case 2:
                        createCustomer(cd);
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Please enter a valid option !");
                }
            }
            catch(InputMismatchException exception){
                System.out.println("Please enter a valid option !");
                choice = 0;
                sc.nextLine();
            }
        } while (choice != 3);
    }


    // method to check if the customer is a valid person
    public static void customerSignIn(){
        testDB td = new testDB();
        Customer customer ;
        char[] ch_pwd;
        String id="";
        Scanner sc = new Scanner(System.in);
        Console console = System.console();
        try {
        System.out.print("Enter customer id:");
        id = sc.nextLine();
            System.out.print("Please enter your password:");
            ch_pwd = sc.nextLine().toCharArray();
        }catch(NullPointerException e){
            System.out.println("Sorry for the inconvenience caused ");
            System.out.print("please enter your password:");
            ch_pwd = sc.nextLine().toCharArray();
        }catch(RuntimeException e){
            System.out.println("Session expired");
            return;
        }
        if(!td.validateUser(id,String.valueOf(ch_pwd)))
        {
            System.out.println("User name / Password incorrect");
            return;
        }else {
            customer = td.fetchCustomer(id);
            if(customer != null) {
                System.out.println("Sign in Successful");
                customerScreen( id,customer);
            }else{System.out.println("Couldn't fetch customer details");}
        }
    }

    // page after customer signIn is successful
    public static void customerScreen(String customerId,Customer customer){
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome "+ customer.getName());
        int ch;
        do{
        System.out.println("Please select any one of the following options");
        System.out.println("1.Book a bus");
        System.out.println("2.Show Bookings");
        System.out.println("3.Cancel Booking");
        System.out.println("4.Sign out");
        System.out.print("Enter you're choice.....");
        try{
        ch = sc.nextInt();
        switch(ch){
            case 1:reserveSeat(customerId);break;
            case 2: new BusApplicationHelper().showBookings(customerId);break;
            case 3: new BusApplicationHelper().cancelBooking();break;
            case 4:System.out.println("Signing you out...bye ");break;
            default:System.out.println("Selection invalid  :(");
        }  }catch(InputMismatchException exception){
            System.out.println("Selection invalid  :(");
            ch = 0;
            sc.nextLine();
        }
        }while(ch != 4);
    }

    //method to create customer
    public static void createCustomer(CustomerDetails cd){
        String Name;
        String DOB;
        String gender;
        String password;
        Scanner sc = new Scanner(System.in);
        Console console = System.console();
        System.out.print("Enter Your Name:");
        Name = sc.nextLine();

        // giving 3 chances to input invalid gender and password and D.O.B
        for(int i=0;i<3;i++){
            System.out.print("Enter Your D.O.B(dd/MM/yyyy):");
            DOB = sc.nextLine();
            if(!dateValidator(DOB)) {
                System.out.println("Use the following format dd/MM/yyyy");
                continue;
            }  System.out.print("To choose your Gender press 1 for Female / 2 for Male:");gender = sc.nextLine();
            if(gender.equalsIgnoreCase("1") || gender.equalsIgnoreCase("2") )
            {
                if((gender.equalsIgnoreCase("1"))) gender = Gender.FEMALE.toString(); else gender = Gender.MALE.toString();
            }else {System.out.println("PLease make a valid selection"); continue;}
            try {
                System.out.print("Please enter your password:");
                password = sc.nextLine();
                   cd.createCustomer(Name, DOB, gender, password);
                    break;
            }
            catch(NullPointerException e){
                System.out.println("Sorry for the inconvenience caused ");
                System.out.print("please enter your password:");
                password = sc.nextLine();
                cd.createCustomer(Name, DOB, gender, password);
                break;
            }

        }
    }


    //Reserving a seat ion bus
    public static void reserveSeat(String customerId) {
        String date;
        String boarding_point;
        String departure_point;
        int fare_amount = 0;
        String seatNo = null;
        List<Integer> fare = new ArrayList<>();
        List<Bus> availableBuses = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter you're date of travel in dd/mm/yyyy:");
            date = sc.nextLine();
            if (!dateValidator(date)) {
                return;
            } else if (!new BusApplicationHelper().validBookingDate(date)) {
                System.out.println("Booking is available only for 30 days from tomorrow , please select a date on that");
                return;
            }
            System.out.print("Enter you're boarding point:");
            boarding_point = sc.nextLine();
            System.out.print("Enter you're departure point:");
            departure_point = sc.nextLine();
            availableBuses = new BusApplicationHelper().fetchBuses(boarding_point, departure_point);
            if (null == availableBuses) return;
            else if (availableBuses.isEmpty()) System.out.println("no buses run on the path you asked for");
            else {
                new BusDetails().printBusDetails(availableBuses);
                String code;
                System.out.print("Enter the bus code of your choice:");
                code = sc.nextLine();
                for (Bus bus : availableBuses) {
                    if (bus.getNumberPlate().equals(code)) {
                        fare_amount = bus.getRoute().getFare();
                        seatNo = new BusApplicationHelper().seatAllocation(date, code);
                    }
                }
                    if (seatNo == null) return;
                    else {
                        new BusApplicationHelper().updateBooking(customerId, boarding_point, departure_point, code, date, seatNo, fare_amount);
                    }
            }
        }catch(InputMismatchException e){
         System.out.println("Input invalid,please try again with valid input");
        }
    }

    public static boolean dateValidator(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        try {
            format.parse(date);
        } catch (ParseException e) {
            System.out.println("Date Invalid");
            return false;
        }
        return true;
    }


}


enum Gender{
    MALE,
    FEMALE
}
