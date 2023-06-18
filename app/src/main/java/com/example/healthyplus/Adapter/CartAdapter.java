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
import android.view.Window;
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
import com.example.healthyplus.Activity.DetailBillActivity;
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

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder>{
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

    public CartAdapter(Context context) {
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
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
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

        if (!isSelectedAll) holder.cbChoose.setChecked(false);
        else holder.cbChoose.setChecked(true);

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

        long[] quantity = {Integer.valueOf(holder.txvQuantity.getText().toString())};
        // get product quantity
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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

        // increase and decrease button

        holder.imbPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity[0] += 1;
                holder.txvQuantity.setText(String.valueOf(quantity[0]));
                // -- TODO increase product quantity in user's cart database
                db.collection("cart").document(user.getUid())
                        .update(product.getId(), quantity[0]);
                dataChecked.remove(product);
                dataChecked.put(product, quantity[0]);
                itemCheckedListener.onItemChecked(dataChecked);
            }
        });

        holder.imbMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity[0]-1 > 0){
                    quantity[0] -= 1;
                    holder.txvQuantity.setText(String.valueOf(quantity[0]));
                    // -- TODO decrease product quantity in user's cart database
                    db.collection("cart").document(user.getUid())
                            .update(product.getId(), quantity[0]);
                    dataChecked.remove(product);
                    dataChecked.put(product, quantity[0]);
                    itemCheckedListener.onItemChecked(dataChecked);

                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View viewDay = inflater.inflate(R.layout.confirm_delete_product, null);
                    builder.setView(viewDay);
                    Dialog dialog = builder.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                    dialog.show();

                    Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                    Button btnConfirm = dialog.findViewById(R.id.btn_confirm);

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DocumentReference docRef = db.collection("cart").document(user.getUid());
                            Map<String,Object> updates = new HashMap<>();
                            updates.put(product.getId(), FieldValue.delete());
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
                            dialog.dismiss();
                            notifyDataSetChanged();
                        }
                    });
                }

            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View viewDay = inflater.inflate(R.layout.confirm_delete_product, null);
                builder.setView(viewDay);
                Dialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                dialog.show();


                Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                Button btnConfirm = dialog.findViewById(R.id.btn_confirm);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentReference docRef = db.collection("cart").document(user.getUid());
                        Map<String,Object> updates = new HashMap<>();
                        updates.put(product.getId(), FieldValue.delete());
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
                        dialog.dismiss();
                        notifyDataSetChanged();
                    }
                });

            }
        });
        holder.cbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle checkbox state change
                if (isChecked) {
                    // Checkbox is checked
                    checkedProduct.add(product);
                    dataChecked.put(product, quantity[0]);

                } else{
                    //checkedProduct.remove(product);
                    dataChecked.remove(product);
                }
                itemCheckedListener.onItemChecked(dataChecked);

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
        private CheckBox cbChoose;
        private ImageView imvProductImage, imgDelete;
        public CartViewHolder(@NonNull View itemView) {
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
