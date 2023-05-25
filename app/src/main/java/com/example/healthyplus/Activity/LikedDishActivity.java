package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.healthyplus.Adapter.DishAdapter;
import com.example.healthyplus.Adapter.LikedDishAdapter;
import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LikedDishActivity extends AppCompatActivity {
    Button btnBack;
    RecyclerView rec;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LikedDishAdapter adapter = new LikedDishAdapter(this);
    List<Dish> favList = new ArrayList<>();
    List<String> favID = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_dish);

        findView();
        showFavDish();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DishActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showFavDish() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        rec.setLayoutManager(new GridLayoutManager(this, 2));


        // Get list of favorite dish id
        db.collection("favorite").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            Map<String, Object> data = doc.getData();
                            if(data != null && data.containsKey("listID")) {
                                favID = (List<String>) data.get("listID");
                                db.collection("dish").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Dish dish = document.toObject(Dish.class);
                                                if(favID.contains(dish.getId())) favList.add(dish); // if the dish in favorite then display it
                                            }
                                            adapter.setDishList(favList);
                                        }
                                        else {
                                            Log.e(TAG, "Cannot get data", task.getException());
                                        }
                                    }
                                });

                                rec.setAdapter(adapter);
                            }

                        }
                    }
                });


    }

    private void findView() {
        btnBack = findViewById(R.id.btn_back_liked);
        rec = findViewById(R.id.rec);

    }
}