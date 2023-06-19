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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private Context context;
    List<Product> productList;
    FirebaseStorage storage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ProductAdapter(Context context)
    {
        this.context=context;
    }
    public void setData (List<Product> list)
    {
        this.productList=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product_view, parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null)
            return;
        //holder.img.setImageResource(product.getImg());
        storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReferenceFromUrl(product.getImg());
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
        holder.name.setText(product.getName());
        String numString = String.valueOf(product.getCost());
        String str = "";
        for (int i = 0; i < numString.length() ; i++){
            if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                str += Character.toString(numString.charAt(i)) + ".";
            }else{
                str += Character.toString(numString.charAt(i));
            }
        }
        holder.cost.setText(str);
        holder.supplier.setText(product.getSupplierName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetailProduct(product);
            }
        });


        holder.imbAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Lấy số lượng hiện tại của sản phẩm trong giỏ hàng và tăng lên 1
                DocumentReference userCartRef = db.collection("cart").document(user.getUid());
                String productID = product.getId(); // ID của sản phẩm cần tăng số lượng

                userCartRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Document của người dùng đã tồn tại
                            Map<String, Object> cartData = document.getData();
                            if (cartData != null && cartData.containsKey(productID)) {
                                // Sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1
                                int currentQuantity = ((Long) cartData.get(productID)).intValue();
                                int newQuantity = currentQuantity + 1;

                                userCartRef.update(productID, newQuantity)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(context, "+1 "+product.getName(), Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> System.err.println("Lỗi khi tăng số lượng: " + e));
                            } else {
                                // Sản phẩm chưa tồn tại trong giỏ hàng, đặt số lượng là 1
                                userCartRef.update(productID, 1)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(context, product.getName()+" đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> System.err.println("Lỗi khi thêm sản phẩm: " + e));
                            }
                        }
                        else{
                            // Document của người dùng chưa tồn tại
                            userCartRef.set(Collections.singletonMap(productID, 1))
                                    .addOnSuccessListener(aVoid -> System.out.println("Sản phẩm đã được thêm vào giỏ hàng"))
                                    .addOnFailureListener(e -> System.err.println("Lỗi khi thêm sản phẩm: " + e));
                        }

                    } else {
                        System.err.println("Lỗi khi lấy dữ liệu: " + task.getException());
                    }
                });
            }
        });
    }

    private void goToDetailProduct(Product p) {
        Intent intent = new Intent(context, DetailProductActivity.class);
        Bundle bundle =new Bundle();
        bundle.putSerializable("object_product", (Serializable) p);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(productList!=null) {
            return productList.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private ImageButton imbAddToCart;
        private ImageView img;
        private TextView name, cost, supplier;
        private CardView layout;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            name=itemView.findViewById(R.id.name);
            cost=itemView.findViewById(R.id.cost);
            supplier=itemView.findViewById(R.id.supplier);
            layout=itemView.findViewById(R.id.cv_layout);
            imbAddToCart = itemView.findViewById(R.id.imbAddToCart);
        }


    }
}
