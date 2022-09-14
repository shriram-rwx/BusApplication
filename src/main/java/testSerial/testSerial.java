package main.java.testSerial;

import main.java.customerBookingDetails.CustomerBookingDetails;

import java.io.*;

public class testSerial {


    public static  void main(String args[]){
        try {
             CustomerBookingDetails cb = null;
            FileOutputStream fos = new FileOutputStream("C:\\Users\\shriram-15495\\IdeaProjects\\BusApplication\\src\\main\\java\\testSerial\\test.txt",true);
            FileInputStream fin = new FileInputStream("C:\\Users\\shriram-15495\\IdeaProjects\\BusApplication\\src\\main\\java\\testSerial\\test.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            ObjectInputStream ois = new ObjectInputStream(fin);
          //  for(int i=0;i<6;i++) {
                oos = new ObjectOutputStream(fos);
                cb = new CustomerBookingDetails();
                oos.writeObject(cb);
                oos.reset();
            oos = new ObjectOutputStream(fos);
            cb = new CustomerBookingDetails();
            oos.writeObject(cb);
        //    }

            oos.close();

            for(int i=0;i<6;i++) {
                ois.readObject();
                System.out.println("Reading the object number " + (i+1));
            }





            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
