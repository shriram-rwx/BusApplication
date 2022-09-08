package customerBookingDetails;


import customer.Customer;
import test.testDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//Customer details class consists customer details and their login credentials
public class CustomerDetails {
    Random random = new Random();
    final String PREFIX =  "111";
    final int MAX = 1111;
    final int MIN = 9999;
    String idCreated = null;

    public synchronized void createCustomer(String Name,String DOB,String Gender,final String Password){
        testDB td = new testDB();
        List<String> customerIds = td.fetchCustomerIds();
        Customer customer = new Customer();
        customer.setName(Name);
        customer.setDOB(DOB);
        customer.setGender(Gender);
        boolean status = false;
        do{
            idCreated = PREFIX + String.valueOf(random.nextInt(1111));
        }while(customerIds.contains(idCreated));
        customer.setId(idCreated);
        status = td.createCustomer(customer,String.valueOf(Password.hashCode()));
        if(status)System.out.println("Customer Created Successfully, You're ID is : " + idCreated);
        else System.out.println("Customer creation Failed");
    }

}
