package com.example.healthyplus.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private Context context;
    private ArrayList <Product> list;

    FirebaseStorage storage;

    public CartAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Product product = list.get(position);

        holder.txvProductName.setText(product.getName());
        holder.txvSupplierName.setText(product.getSupplierName());
        holder.txvPrice.setText(product.getCost());

        //get product image
        storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReferenceFromUrl(product.getImg());
        try {
            File localFile = File.createTempFile("images", "jpg");
            imgRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.imvProductImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "Loi", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get product quantity
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cart").document(user.getUid()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Map<String, Object> map = task.getResult().getData();
                        if(map.containsKey(product.getId())){
                            holder.txvQuantity.setText(String.valueOf(map.get(product.getId())));
                        }
                    }
                    else{
                        Log.e(TAG, "cannot get product quantity");
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

        // increase and decrease button
        int[] quantity = {Integer.valueOf(holder.txvQuantity.getText().toString())};
        holder.imbPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity[0] += 1;
                holder.txvQuantity.setText(String.valueOf(quantity[0]));
                // -- TODO increase product quantity in user's cart database
            }
        });

        holder.imbMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity[0] > 0){
                    quantity[0] -= 1;
                    holder.txvQuantity.setText(String.valueOf(quantity[0]));
                    // -- TODO decrease product quantity in user's cart database
                }

            }
        });

    }

    public void setList(ArrayList<Product> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(this.list != null)
            return this.list.size();
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView txvProductName, txvSupplierName, txvPrice, txvQuantity;
        private ImageButton imbPlus, imbMinus;
        private ImageView imvProductImage;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            txvQuantity = itemView.findViewById(R.id.txvQuantity);
            txvProductName = itemView.findViewById(R.id.txvProductName);
            txvSupplierName = itemView.findViewById(R.id.txvSupplierName);
            txvPrice = itemView.findViewById(R.id.txvPrice);
            imbMinus = itemView.findViewById(R.id.imb_minus_button);
            imbPlus = itemView.findViewById(R.id.imb_plus_button);
            imvProductImage = itemView.findViewById(R.id.imvProductImage);
        }
    }
}
