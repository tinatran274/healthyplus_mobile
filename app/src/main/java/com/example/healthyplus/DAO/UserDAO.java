package com.example.healthyplus.DAO;

import androidx.annotation.NonNull;

import com.example.healthyplus.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserDAO extends BaseDAO {

    FirebaseUser user;
    User userBase;

    public UserDAO (){
        super("user");
        this.userBase = new User();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        if (this.user != null) {
           System.out.println("ok");
        } else {
            // No user is signed in
            System.out.println("not ok");
        }
    }

    public void addUser(User user){
        this.db.collection(collectionName).document(this.user.getUid()).set(user);
    }

}