package Model;

/**
 * Created by user on 12/31/2017.
 */

public class User {

    private String PhoneNumber;
    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;
    private String Address;
    private String UserTypeID;

    /*
                param
             */
    public User(String PhoneNumber, String FirstName, String LastName,
                String Email, String Password,String Address,String UserTypeID) {
        this.PhoneNumber = PhoneNumber;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.Password = Password;
        this.Address = Address;
        this.UserTypeID = UserTypeID;
    }


    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUserTypeID() {
        return UserTypeID;
    }

    public void setUserTypeID(String userTypeID) {
        UserTypeID = userTypeID;
    }

}
