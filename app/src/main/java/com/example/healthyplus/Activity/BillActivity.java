package com.example.healthyplus.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.healthyplus.Adapter.BillAdapter;
import com.example.healthyplus.Adapter.CancelBillAdapter;
import com.example.healthyplus.Adapter.PayAdapter;
import com.example.healthyplus.Model.Alarm;
import com.example.healthyplus.Model.Bill;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class BillActivity extends AppCompatActivity {

    Button btnBack;
    RecyclerView recyclerView;
    CardView cvCancel, cvHistory;
    ArrayList<Bill> list = new ArrayList<>();
    ArrayList<Bill> cancelList = new ArrayList<>();
    BillAdapter adapter;
    CancelBillAdapter cancelBillAdapter;
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        btnBack = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.rec);
        cvCancel = findViewById(R.id.cv_cancel);
        cvHistory = findViewById(R.id.cv_history);


        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        adapter = new BillAdapter(this);
        cancelBillAdapter = new CancelBillAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getListBill();

        getListCancelBill();
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBillAdapter.setList(cancelList);
                recyclerView.setAdapter(cancelBillAdapter);
                Log.d(TAG, "Cancel: "+cancelList);
            }
        });
        cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setList(list);
                recyclerView.setAdapter(adapter);
                Log.d(TAG, "His: "+list);
            }
        });
    }

    private void getListBill() {
        DocumentReference docRef = db.collection("order").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();
                        for (String i : map.keySet()) {
                            System.out.println("key: " + i + " value: " + map.get(i));
                            if ((boolean) map.get(i) == true) {
                                DocumentReference docRef = db.collection("bill").document(i);

                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                Bill b = document.toObject(Bill.class);
                                                list.add(b);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }



    private void getListCancelBill() {
        DocumentReference docRef = db.collection("order").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();
                        for (String i : map.keySet()) {
                            System.out.println("key: " + i + " value: " + map.get(i));
                            if ((boolean) map.get(i) != true) {
                                DocumentReference docRef = db.collection("bill").document(i);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                Bill b = document.toObject(Bill.class);
                                                cancelList.add(b);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}