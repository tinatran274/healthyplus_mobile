package com.example.healthyplus.Adapter;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Activity.DishDetailActivity;
import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder>{
    private Context context;
    List<Dish> DishList;
    FirebaseStorage storage;
    FirebaseFirestore db;
    List<String> favID = new ArrayList<>();

    public DishAdapter(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    public void setDishList(List<Dish> dishList) {
        DishList = dishList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DishAdapter.DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dish_view, parent,false);
        return new DishAdapter.DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishAdapter.DishViewHolder holder, int position) {
        Dish dish = this.DishList.get(position);

        holder.name.setText(dish.getName());
        holder.kcal.setText(Double.toString(dish.getCalo()));
        holder.protein.setText(Double.toString(dish.getProtein()));
        holder.fat.setText(Double.toString(dish.getFat()));
        holder.carbs.setText(Double.toString(dish.getCarb()));


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDishDetail(dish);
            }
        });

        // get image of the dish from firebase storage
        storage = FirebaseStorage.getInstance();
        Log.d(TAG, dish.getImg());
        StorageReference imgRef = storage.getReferenceFromUrl(dish.getImg());
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
                    Log.e(TAG, "Error getting dish image");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Check if current dish is in user favorite list or not
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("favorite").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            Map<String, Object> data = doc.getData();
                            if(data != null && data.containsKey("listID")){
                                favID = (List<String>) data.get("listID");
                                Log.d(TAG, "Get favorite list");
                            }
                            if(favID.contains(dish.getId())){
                                holder.favButton.setChecked(true);
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to get favorite list");
                    }
                });



        // Add dish to favorite list
        holder.favButton.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                FirebaseAuth auth =  FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                if(isChecked) {
                    db.collection("favorite").document(user.getUid())
                            .update("listID", FieldValue.arrayUnion(dish.getId()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Added to favorite");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    List <String> id = new ArrayList<>();
                                    id.add(dish.getId());
                                    db.collection("favorite").document(user.getUid())
                                            .set(Collections.singletonMap("listID", id))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "Create new fav list and Added to favorite");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "Failed to add to favorite");
                                                }
                                            });
                                }
                            });

                }
                else{
                    db.collection("favorite").document(user.getUid())
                            .update("listID", FieldValue.arrayRemove(dish.getId()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Removed from favorite");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
            }
        }));

    }

    private void showDishDetail(Dish dish) {
        Intent intent = new Intent(context, DishDetailActivity.class);
        intent.putExtra("dish", dish);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(DishList != null)
            return DishList.size();
        return 0;
    }

    public class DishViewHolder extends RecyclerView.ViewHolder{
        private CardView cv;
        private ImageView img;
        private TextView name, kcal, carbs, protein, fat;
        private ToggleButton favButton;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            name=itemView.findViewById(R.id.txvName);
            kcal=itemView.findViewById(R.id.txvKcal);
            carbs=itemView.findViewById(R.id.txvCarbs);
            protein=itemView.findViewById(R.id.txvProtein);
            fat=itemView.findViewById(R.id.txvFat);
            cv=itemView.findViewById(R.id.cv);
            favButton = itemView.findViewById(R.id.favButton);
        }
    }


}
