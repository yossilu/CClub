package Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by user on 12/31/2017.
 */

public class User {
    private static FirebaseAuth mAuth;
    private static DatabaseReference dbRef;
    private  String phoneNumber;
    private  String firstName;
    private  String lastName;
    private  String email;
    private  String password;
    private  String address;
    private  String userTypeID;


    /*
                param
             */
    public User(String PhoneNumber, String FirstName, String LastName,
                String Email, String Password,String Address,String UserTypeID) {
        this.phoneNumber = PhoneNumber;
        this.firstName = FirstName;
        this.lastName = LastName;
        this.email = Email;
        this.password = Password;
        this.address = Address;
        this.userTypeID = UserTypeID;
    }

    public User() {
    }

    public  String getPhoneNumber() {
        return phoneNumber;
    }

    public  void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public  String getFirstName() {
        return firstName;
    }

    public  void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public  void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public  void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public  void setPassword(String password) { this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public  void setAddress(String address) {
        this.address = address;
    }

    public String getUserTypeID() {
        return userTypeID;
    }

    public  void setUserTypeID(String userTypeID) {
        this.userTypeID = userTypeID;
    }


}
