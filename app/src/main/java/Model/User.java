package Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by user on 12/31/2017.
 */

public class User {
    private static FirebaseAuth mAuth;
    private static DatabaseReference dbRef;
    public static User currentUser = null;
    public static Bitmap currentPicture = null;

//    public static Bitmap getCurrentPicture() {
//        if (FirebaseAuth.getInstance().getCurrentUser() == null)
//            return null;
//        else if (currentPicture == null){
//            initializePicture();
//            try {
//                Thread.sleep(4000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return currentPicture;
//    }


    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String userTypeID;


    /*
                param
             */
    public User(String PhoneNumber, String FirstName, String LastName,
                String Email, String Password, String Address, String UserTypeID) {
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

    private static void initializePicture() {
        final Bitmap[] bitmap = {null};
        final int FIVE_MEGABYTE = 1024 * 1024 * 5;
        if (currentUser != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference strf = storageReference.child("images").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("privateimg.jpg");
            strf.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    currentPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    private static void initialize() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String value = String.valueOf(child.getKey());
                    if (child.getKey().toString().equals(uid)) {
                        currentUser = (User) child.getValue(User.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static User getCurrentUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return null;
        else if (currentUser == null) {
            initialize();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return currentUser;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserTypeID() {
        return userTypeID;
    }

    public void setUserTypeID(String userTypeID) {
        this.userTypeID = userTypeID;
    }


}
