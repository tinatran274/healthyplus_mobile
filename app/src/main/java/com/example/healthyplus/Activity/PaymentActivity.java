package com.example.healthyplus.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.healthyplus.Adapter.CartAdapter;
import com.example.healthyplus.Adapter.PayAdapter;
import com.example.healthyplus.Model.Alarm;
import com.example.healthyplus.Model.Bill;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    Button btnBack;
    RecyclerView recyclerView;
    Spinner spDelivery, spTime, spPay;
    CardView cvPrize;
    TextView txvTotalDelivery, txvTotalProduct, txvTotal, txvTotalAll, txvInfoName, txvBuy;
    EditText etInfoAddress;
    PayAdapter adapter;
    FirebaseFirestore db;
    FirebaseUser user;
    ArrayList<Product> list = new ArrayList<>();
    HashMap<Product, Long> receiveProduct = new HashMap<>();
    Map<String, Object> putFirebase =new HashMap<>();
    Map<String, Object> newOrder = new HashMap<>();
    String cDelivery;
    String cTime;
    String cPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        receiveProduct = (HashMap<Product,Long>) getIntent().getSerializableExtra("hashMapData");
        for (Product i : receiveProduct.keySet()) {
            list.add(i);
            putFirebase.put(i.getId(),receiveProduct.get(i));
        }
        String delivery[]={"Hỏa tốc","Bình thường"};
        String time[]={"Sáng - Trưa", "Chiều - Tối"};
        String pay[]={"Khi nhận hàng", "Momo"};

        txvInfoName=findViewById(R.id.inf_name);
        etInfoAddress=findViewById(R.id.inf_address);
        btnBack=findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.rec);
        spDelivery=findViewById(R.id.sp_delivery);
        spTime=findViewById(R.id.sp_time);
        spPay=findViewById(R.id.sp_pay);
        txvTotalDelivery=findViewById(R.id.txv_total_delivery);
        txvTotalProduct=findViewById(R.id.txv_total_product);
        txvTotal=findViewById(R.id.txv_total);
        txvTotalAll=findViewById(R.id.txv_total_all);
        cvPrize=findViewById(R.id.cv_prize);
        txvBuy=findViewById(R.id.txv_buy);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        txvTotalProduct.setText(castToMoney(getTotalProduct()));
        adapter = new PayAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter.setList(list);
        recyclerView.setAdapter(adapter);

        ArrayAdapter adapterDelivery = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, delivery);
        adapterDelivery.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spDelivery.setAdapter(adapterDelivery);

        ArrayAdapter adapterTime = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time);
        adapterTime.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spTime.setAdapter(adapterTime);

        ArrayAdapter adapterPay = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pay);
        adapterPay.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spPay.setAdapter(adapterPay);

        db.collection("user").document(user.getUid())
            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User u = document.toObject(User.class);
                        txvInfoName.setText(u.getName());
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        spDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cDelivery=delivery[i];
                if(i==0) {
                    txvTotalDelivery.setText("50.000");
                    txvTotal.setText(castToMoney(getTotalProduct()+50000));
                    if(getTotalProduct()>500000){
                        txvTotalAll.setText(castToMoney((getTotalProduct()-(getTotalProduct()/5))+50000));
                        cvPrize.setVisibility(View.VISIBLE);
                    }
                    else{
                        txvTotalAll.setText(castToMoney(getTotalProduct()+50000));
                    }
                }
                else {
                    txvTotalDelivery.setText("30.000");
                    txvTotal.setText(castToMoney(getTotalProduct()+30000));
                    if(getTotalProduct()>500000){
                        txvTotalAll.setText(castToMoney((getTotalProduct()-(getTotalProduct()/5))+30000));
                        cvPrize.setVisibility(View.VISIBLE);
                    }
                    else{
                        txvTotalAll.setText(castToMoney(getTotalProduct()+30000));
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cTime=time[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spPay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cPay=pay[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View viewDay = inflater.inflate(R.layout.order_success, null);
                builder.setView(viewDay);
                Dialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                dialog.show();

                Button btnCancel = viewDay.findViewById(R.id.btn_cancel);
                Button btnConfirm = viewDay.findViewById(R.id.btn_confirm);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bill bill=new Bill("",user.getUid()
                                , String.valueOf(etInfoAddress.getText())
                                , putFirebase
                                ,cDelivery
                                ,cTime
                                ,cPay
                                ,false
                                ,false
                                , String.valueOf(txvTotalAll.getText()).replace(".","")
                                ,castDateToInt());
                        DocumentReference doc = db.collection("bill").document();
                        bill.setId(doc.getId());

                        if (cPay=="Momo"){
                            Intent intent = new Intent(PaymentActivity.this, MoMoActivity.class);
                            Bundle bundle =new Bundle();
                            bundle.putSerializable("object_bill", (Serializable) bill);
                            intent.putExtras(bundle);
                            PaymentActivity.this.startActivity(intent);
                        }
                        else{

                            for (String i : putFirebase.keySet()) {
                                DocumentReference docRef = db.collection("cart").document(user.getUid());
                                Map<String,Object> updates = new HashMap<>();
                                updates.put(i, FieldValue.delete());
                                docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating document", e);
                                            }
                                        });

                            }
                            doc.set(bill).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                            newOrder.put(doc.getId(), true);
                            db.collection("order").document(user.getUid()).set(newOrder, SetOptions.mergeFields(doc.getId()))
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "get failed ");
                                        }
                                    });
                            dialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });

            }
        });
    }

    private long getTotalProduct() {
        long sum = 0;
        for (Product i : receiveProduct.keySet()) {
            System.out.println("key: " + i + " value: " + receiveProduct.get(i));
            float temp = Float.valueOf(i.getCost()) * receiveProduct.get(i);
            sum += temp;
        }
        return sum;
    }
    private String castToMoney(long a){

        String numString = String.valueOf(a);
        String str = "";
        for (int i = 0; i < numString.length() ; i++){
            if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                str += Character.toString(numString.charAt(i)) + ".";
            }else{
                str += Character.toString(numString.charAt(i));
            }
        }
        return str;
    }
    private int castDateToInt(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Add 1 because months are zero-based
        int day = calendar.get(Calendar.DAY_OF_MONTH)+1;
        int dateAsInt = year * 10000 + month * 100 + day;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        int dateTimeAsInt = year * 1000000 + month * 10000 + day * 100 + hour;

        Log.e(ContentValues.TAG, String.valueOf(dateTimeAsInt));
        return dateTimeAsInt%1000000000;
    }
}