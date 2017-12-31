package Model;

/**
 * Created by user on 12/31/2017.
 */

public class User {
    private String phoneN;
    private String firstN;
    private String lastN;
    private String email;
    private String password;
    private String address;

    public User(String phoneN, String firstN, String lastN, String email, String password,String address) {
        this.phoneN = phoneN;
        this.firstN = firstN;
        this.lastN = lastN;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneN() {
        return phoneN;
    }

    public void setPhoneN(String phoneN) {
        this.phoneN = phoneN;
    }

    public String getFirstN() {
        return firstN;
    }

    public void setFirstN(String firstN) {
        this.firstN = firstN;
    }

    public String getLastN() {
        return lastN;
    }

    public void setLastN(String lastN) {
        this.lastN = lastN;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
