package com.example.healthyplus.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Activity.CartActivity;
import com.example.healthyplus.Activity.NotificationActivity;
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
import java.util.HashMap;
import java.util.Map;

public class PayAdapter extends  RecyclerView.Adapter<PayAdapter.PayViewHolder>{
    public interface OnItemCheckedListener {
        void onItemChecked(Map<Product, Long> dataChecked);
    }
    private Context context;
    private ArrayList <Product> list;

    private ArrayList <Product> checkedProduct = new ArrayList<>();
    Map<Product, Long> dataChecked = new HashMap<>();
    private OnItemCheckedListener itemCheckedListener;
    private boolean isSelectedAll = false;

    FirebaseStorage storage;

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        this.itemCheckedListener = listener;
    }

    public PayAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }
    public void selectAll() {
        isSelectedAll=true;
        notifyDataSetChanged();
    }

    public void unSelectAll() {
        isSelectedAll=false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PayAdapter.PayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pay, parent,false);
        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayAdapter.PayViewHolder holder, int position) {
        Product product = list.get(position);

        holder.txvProductName.setText(product.getName());
        holder.txvSupplierName.setText(product.getSupplierName());

        String numString = String.valueOf(product.getCost());
        String str = "";
        for (int i = 0; i < numString.length() ; i++){
            if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                str += Character.toString(numString.charAt(i)) + ".";
            }else{
                str += Character.toString(numString.charAt(i));
            }
        }
        holder.txvPrice.setText(str);

        long[] quantity = {Integer.valueOf(holder.txvQuantity.getText().toString())};
        // get product quantity
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        db.collection("cart").document(user.getUid()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Map<String, Object> map = task.getResult().getData();
                        if(map.containsKey(product.getId())){
                            holder.txvQuantity.setText(String.valueOf(map.get(product.getId())));
                            quantity[0]= (long) map.get(product.getId());
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

    public class PayViewHolder extends RecyclerView.ViewHolder {
        private TextView txvProductName, txvSupplierName, txvPrice, txvQuantity;
        private ImageButton imbPlus, imbMinus;
        private CheckBox cbChoose;
        private ImageView imvProductImage, imgDelete;
        public PayViewHolder(@NonNull View itemView) {
            super(itemView);

            txvQuantity = itemView.findViewById(R.id.txvQuantity);
            txvProductName = itemView.findViewById(R.id.txvProductName);
            txvSupplierName = itemView.findViewById(R.id.txvSupplierName);
            txvPrice = itemView.findViewById(R.id.txvPrice);
            imbMinus = itemView.findViewById(R.id.imb_minus_button);
            imbPlus = itemView.findViewById(R.id.imb_plus_button);
            imvProductImage = itemView.findViewById(R.id.imvProductImage);
            imgDelete = itemView.findViewById(R.id.img_delete);
            cbChoose = itemView.findViewById(R.id.cb_choose);
        }
    }
}
