package com.example.healthyplus.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder>{


    private Context context;
    List<Dish> DishList;

    public DishAdapter(Context context) {
        this.context = context;
    }

    public void setDishList(List<Dish> dishList) {
        DishList = dishList;
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

//        // Get a dish img
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReferenceFromUrl(dish.getImg());
//        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                String imageUrl = uri.toString();
//                Picasso.get().load(imageUrl).into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        // Set the loaded bitmap as the background of the ImageView
//                        holder.img.setBackground(new BitmapDrawable(context.getResources(), bitmap));
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });
//                }
//        });

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
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            name=itemView.findViewById(R.id.txvName);
            kcal=itemView.findViewById(R.id.txvKcal);
            carbs=itemView.findViewById(R.id.txvCarbs);
            protein=itemView.findViewById(R.id.txvProtein);
            fat=itemView.findViewById(R.id.txvFat);
            cv=itemView.findViewById(R.id.cv);
        }
    }


}
