package com.example.healthyplus.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Activity.CartActivity;
import com.example.healthyplus.Activity.DetailAlarmActivity;
import com.example.healthyplus.Activity.DetailBillActivity;
import com.example.healthyplus.Activity.NotificationActivity;
import com.example.healthyplus.Model.Bill;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BillAdapter extends  RecyclerView.Adapter<BillAdapter.BillViewHolder>{
    public interface OnItemCheckedListener {
        void onItemChecked(Map<Product, Long> dataChecked);
    }
    private Context context;
    private ArrayList <Bill> list;

    private OnItemCheckedListener itemCheckedListener;

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        this.itemCheckedListener = listener;
    }

    public BillAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }
    @NonNull
    @Override
    public BillAdapter.BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_bill, parent,false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.BillViewHolder holder, int position) {
        Bill bill = list.get(position);

        holder.txvId.setText(bill.getId());
        holder.txvDate.setText(convertDatetoString(bill.getDate()));
        holder.txvPrice.setText(castToMoney(Long.valueOf(bill.getTotal())));
        if(bill.isStatus()){
            holder.txvStatus.setText("Đơn hàng đã được giao thành công");
        }else{
            holder.txvStatus.setText("Đơn hàng đang trên đường vận chuyển");
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailBillActivity.class);
                Bundle bundle =new Bundle();
                bundle.putSerializable("object_bill", (Serializable) bill);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }
    public void setList (ArrayList < Bill > list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(this.list != null)
            return this.list.size();
        return 0;
    }

    public class BillViewHolder extends RecyclerView.ViewHolder {
        private TextView txvId, txvDate, txvPrice, txvStatus;
        LinearLayout linearLayout;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);

            txvId = itemView.findViewById(R.id.txv_id);
            txvDate = itemView.findViewById(R.id.txv_date);
            txvStatus = itemView.findViewById(R.id.txv_status);
            txvPrice = itemView.findViewById(R.id.txv_price);
            linearLayout = itemView.findViewById(R.id.ln);
        }
    }
    public String convertDatetoString(int date){

        int year = (date/1000000)%100;
        int month = (date/10000)%100;
        int hour = date%100;
        int day = (date/100)%100;

        String str = hour+"h "+day+"/"+month+"/20"+year;

        return str;
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
}
