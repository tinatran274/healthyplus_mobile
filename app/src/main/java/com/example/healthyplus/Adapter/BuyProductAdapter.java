package com.example.healthyplus.Adapter;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Activity.ControlWaterActivity;
import com.example.healthyplus.Activity.DetailProductActivity;
import com.example.healthyplus.Model.BuyProduct;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyProductAdapter extends RecyclerView.Adapter<BuyProductAdapter.BuyProductViewHolder>{
    private Context context;
    List<BuyProduct> buyProductList;
    FirebaseStorage storage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public BuyProductAdapter(Context context)
    {
        this.context=context;
    }
    public void setData (List<BuyProduct> list)
    {
        this.buyProductList=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public BuyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pay, parent,false);
        return new BuyProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyProductViewHolder holder, int position) {
        BuyProduct buyProduct = buyProductList.get(position);
        if (buyProduct == null)
            return;
        storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReferenceFromUrl(buyProduct.getImg());
        try {
            File localFile = File.createTempFile("images", "jpg");
            imgRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.img.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "Loi", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        holder.name.setText(buyProduct.getName());
        holder.quantity.setText(buyProduct.getNum());
        String numString = String.valueOf(buyProduct.getCost());
        String str = "";
        for (int i = 0; i < numString.length() ; i++){
            if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                str += Character.toString(numString.charAt(i)) + ".";
            }else{
                str += Character.toString(numString.charAt(i));
            }
        }
        holder.cost.setText(str);
        holder.supplier.setText(buyProduct.getSupplierName());



    }

    @Override
    public int getItemCount() {
        if(buyProductList!=null) {
            return buyProductList.size();
        }
        return 0;
    }

    public class BuyProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView name, cost, supplier, quantity;
        public BuyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imvProductImage);
            name=itemView.findViewById(R.id.txvProductName);
            cost=itemView.findViewById(R.id.txvPrice);
            supplier=itemView.findViewById(R.id.txvSupplierName);
            quantity=itemView.findViewById(R.id.txvQuantity);
        }


    }
}
