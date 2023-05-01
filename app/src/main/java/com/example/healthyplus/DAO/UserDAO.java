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

//    public void onGetUser(OnUserLoadedCallback onUserLoadedCallback){
//        this.db.collection(collectionName).document(this.user.getUid()).get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if(task.isSuccessful()){
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                                if(documentSnapshot != null){
//                                    User
//                                    userBase = documentSnapshot.toObject(User.class);
//                                }
//                        }
//                    }
//                });
//    }
//
//    public interface OnUserLoadedCallback {
//        void onUserLoaded(User user);
//    }
//
//    public User getCurrentUser(){
//        this.onGetUser(new OnUserLoadedCallback(){
//            @Override
//            public void onUserLoaded(User user) {
//                // Do something with the loaded user object
//                userBase = user;
//            }
//        });
//        return userBase;
//    }


}