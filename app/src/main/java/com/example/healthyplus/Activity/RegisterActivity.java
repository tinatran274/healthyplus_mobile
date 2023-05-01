package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthyplus.Activity.BasicInfo.GenderActivity;
import com.example.healthyplus.DAO.AccountDAO;
import com.example.healthyplus.DAO.UserDAO;
import com.example.healthyplus.Model.Account;
import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    EditText email, username, password;
    Button btnDangKy;
    FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        btnDangKy = findViewById(R.id.btnDangKy);
        email = findViewById(R.id.edtEmail);
        username = findViewById(R.id.edtName);
        password = findViewById(R.id.edtPassword);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isEmpty(email))
                    Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                else if(isEmpty(username))
                    Toast.makeText(RegisterActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                else if(isEmpty(password))
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                else {
                    String user_email = email.getText().toString().trim();
                    String user_name = username.getText().toString().trim();
                    String user_password = password.getText().toString().trim();

                    auth.createUserWithEmailAndPassword(user_email, user_password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");

                                        FirebaseUser currentUser = auth.getCurrentUser();

                                        // Create new User to use in BasicInfo activity package
                                        // We will need these user's info: id, username
                                        User user = new User();
                                        user.setId(currentUser.getUid());
                                        user.setName(user_name);

//                                    // Add new User to collection account in firestore, each document is
//                                       auto-generated userID
//                                    UserDAO userDAO = new UserDAO();
//                                    userDAO.addUser(userBase);

                                        // Add new Account to collection account in firestore,
                                        // each document is auto-generated accountID
                                        Account account = new Account();
                                        account.setEmail(user_email);
                                        account.setPassword(user_password);
                                        account.setUserName(user_name);
                                        account.setUserID(currentUser.getUid());

                                        AccountDAO accountDAO = new AccountDAO();
                                        accountDAO.addAccount(account);
                                        account = null; // remove the object

                                        Intent intent = new Intent(getApplicationContext(), GenderActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }
}
