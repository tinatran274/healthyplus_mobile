package com.example.healthyplus.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthyplus.Adapter.BillAdapter;
import com.example.healthyplus.Adapter.BuyProductAdapter;
import com.example.healthyplus.Adapter.CancelBillAdapter;
import com.example.healthyplus.Adapter.PayAdapter;
import com.example.healthyplus.Model.Bill;
import com.example.healthyplus.Model.BuyProduct;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DetailBillActivity extends AppCompatActivity {

    Button btnBack, btnDelete, btnConfirm;
    TextView txvStatus, txvName, txvAddress, txvDelivery, txvTime, txvDate, txvPay, txvId, txvTotal, txvReturnPay;
    RecyclerView recyclerView;

    LinearLayout linearLayout;
    private ArrayList<BuyProduct> list = new ArrayList<>();
    BuyProductAdapter adapter;
    HashMap<String, Object> receiveProduct = new HashMap<>();

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);

        Bundle bundle = getIntent().getExtras();
        if (bundle==null)
            return;
        Bill bill= (Bill) bundle.get("object_bill");

        btnBack=findViewById(R.id.btn_back);
        btnDelete=findViewById(R.id.btn_delete);
        btnConfirm=findViewById(R.id.btn_confirm);
        recyclerView=findViewById(R.id.rec);
        txvStatus=findViewById(R.id.txv_status);
        txvName=findViewById(R.id.txv_name);
        txvAddress=findViewById(R.id.txv_address);
        txvDelivery=findViewById(R.id.txv_delivery);
        txvTime=findViewById(R.id.txv_time);
        txvDate=findViewById(R.id.txv_date);
        txvPay=findViewById(R.id.txv_pay);
        txvId=findViewById(R.id.txv_id);
        txvTotal=findViewById(R.id.txv_total);
        txvReturnPay=findViewById(R.id.txv_return_pay);
        linearLayout=findViewById(R.id.ln_doing);

        db = FirebaseFirestore.getInstance();

        adapter = new BuyProductAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter.setData(list);
        recyclerView.setAdapter(adapter);

        System.out.println("key: " + bill.toString());

        if((boolean) bill.isStatus()==true){
            txvStatus.setText("Đơn hàng đã hoàn thành");
        }else{
            txvStatus.setText("Đơn hàng đang trên đường vận chuyển");
            linearLayout.setVisibility(View.VISIBLE);
        }
        if((boolean) bill.isReturnPay()==true){
            txvReturnPay.setText("Đã thanh toán");
        }else{
            txvReturnPay.setText("Chưa thanh toán");
        }
        txvName.setText(bill.getUserID());
        txvAddress.setText(bill.getAddress());
        txvDelivery.setText(bill.getDelivery());
        txvDate.setText(convertDatetoString(bill.getDate()));
        txvTime.setText(bill.getTime());
        txvPay.setText(bill.getPay());
        txvId.setText(bill.getId());
        txvTotal.setText(bill.getTotal());
        receiveProduct= (HashMap<String, Object>) bill.getProducts();
        for (String i : receiveProduct.keySet()) {
            System.out.println("key: " + i + " value: " + receiveProduct.get(i));

            db.collection("product").document(i)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Product p = document.toObject(Product.class);
                            Log.d(TAG, "Pr: " + p.getName());
                            BuyProduct bp = new BuyProduct(p.getId(),p.getName(),p.getCost(),p.getImg(),p.getSupplierName(), receiveProduct.get(i).toString());
                            list.add(bp);
                            Log.d(TAG, "Doc: " + bp.getNum());
                            adapter.notifyDataSetChanged();
                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            db.collection("technology_product").document(i)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Product p = document.toObject(Product.class);
                                    Log.d(TAG, "Pr: " + p.getName());
                                    BuyProduct bp = new BuyProduct(p.getId(),p.getName(),p.getCost(),p.getImg(),p.getSupplierName(), receiveProduct.get(i).toString());
                                    list.add(bp);
                                    Log.d(TAG, "Doc: " + bp.getNum());
                                    adapter.notifyDataSetChanged();
                                    //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailBillActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View viewDay = inflater.inflate(R.layout.confirm_bill, null);
                builder.setView(viewDay);
                Dialog dialog = builder.create();
                dialog.show();

                Button bcancel = viewDay.findViewById(R.id.btn_cancel);
                Button bconfirm = viewDay.findViewById(R.id.btn_confirm);

                bcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                bconfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("bill").document(bill.getId())
                                .update("status", true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        adapter.notifyDataSetChanged();
                        finish();
                    }
                });
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailBillActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View viewDay = inflater.inflate(R.layout.confirm_delete_bill, null);
                builder.setView(viewDay);
                Dialog dialog = builder.create();
                dialog.show();

                Button cancel = viewDay.findViewById(R.id.btn_cancel);
                Button confirm = viewDay.findViewById(R.id.btn_confirm);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        if((castDateToInt() - bill.getDate())/24 > 10){
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetailBillActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View viewNo = inflater.inflate(R.layout.notify_cannot_delete, null);
                            builder.setView(viewNo);
                            Dialog dialogNo = builder.create();
                            dialogNo.show();

                            Button cancel = viewNo.findViewById(R.id.btn_cancel);

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogNo.dismiss();
                                }
                            });
                        }
                        else{
                            db.collection("order").document(bill.getUserID())
                                    .update(bill.getId(), false)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            adapter.notifyDataSetChanged();
                            finish();
                        }
                    }
                });
            }
        });
    }
    public String convertDatetoString(int date){

        int year = (date/1000000)%100;
        int month = (date/10000)%100;
        int day = date%100;
        int hour = (date/100)%100;

        String str = hour+"h "+day+"/"+month+"/20"+year;

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