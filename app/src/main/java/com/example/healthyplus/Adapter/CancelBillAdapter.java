package com.example.healthyplus.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CancelBillAdapter extends  RecyclerView.Adapter<CancelBillAdapter.CancelBillViewHolder>{
    public interface OnItemCheckedListener {
        void onItemChecked(Map<Product, Long> dataChecked);
    }
    private Context context;
    private ArrayList <Bill> list;

    private OnItemCheckedListener itemCheckedListener;

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        this.itemCheckedListener = listener;
    }

    public CancelBillAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }
    @NonNull
    @Override
    public CancelBillAdapter.CancelBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cancel_bill, parent,false);
        return new CancelBillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CancelBillAdapter.CancelBillViewHolder holder, int position) {
        Bill bill = list.get(position);

        holder.txvId.setText(bill.getId());
        holder.txvDate.setText(convertDatetoString(bill.getDate()));
        holder.txvPrice.setText(bill.getTotal());
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

    public class CancelBillViewHolder extends RecyclerView.ViewHolder {
        private TextView txvId, txvDate, txvPrice;
        LinearLayout linearLayout;
        public CancelBillViewHolder(@NonNull View itemView) {
            super(itemView);

            txvId = itemView.findViewById(R.id.txv_id);
            txvDate = itemView.findViewById(R.id.txv_date);
            txvPrice = itemView.findViewById(R.id.txv_price);
            linearLayout = itemView.findViewById(R.id.ln);
        }
    }
    public String convertDatetoString(int date){

        int year = (date/1000000)%100;
        int month = (date/10000)%100;
        int day = date%100;
        int hour = (date/100)%100;

        String str = hour+"h "+day+"/"+month+"/20"+year;

        return str;
    }
}
