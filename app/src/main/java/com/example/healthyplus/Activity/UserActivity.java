package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    TextView id, name, aim, maxCalories, maxWater, bmi, ttde, age, gender, height, weight, exerciseFrequency, txvChangPass,
    txv_bmi;
    ImageView imv_bmi;
    Button btnUpdate, btnBackUser;
    FirebaseFirestore db;
    FirebaseUser user;
    Button imv_log_out;
    User p;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    ToggleButton carret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        name = findViewById(R.id.txv_name);
        aim = findViewById(R.id.txv_aim);
        maxCalories = findViewById(R.id.txv_max_calories);
        maxWater = findViewById(R.id.txv_max_water);
        bmi = findViewById(R.id.txv_bmi);
        ttde = findViewById(R.id.txv_ttde);
        age = findViewById(R.id.txv_age);
        gender = findViewById(R.id.txv_gender);
        height = findViewById(R.id.txv_height);
        weight = findViewById(R.id.txv_weight);
        exerciseFrequency = findViewById(R.id.txv_exercise);
        btnUpdate = findViewById(R.id.btn_update);
        btnBackUser = findViewById(R.id.btn_back_user);
        imv_log_out = findViewById(R.id.imv_log_out);
        txvChangPass = findViewById(R.id.txv_change_pass);
        txv_bmi = findViewById(R.id.txv_bmi123);
        imv_bmi = findViewById(R.id.imv_bmi123);
        carret = findViewById(R.id.carret);

        carret.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    imv_bmi.setVisibility(View.VISIBLE);
                    txv_bmi.setVisibility(View.VISIBLE);
                }
                else {
                    imv_bmi.setVisibility(View.GONE);
                    txv_bmi.setVisibility(View.GONE);
                }
            }
        });

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        setInfoUser();
        txvChangPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater builder = LayoutInflater.from(UserActivity.this);
                View dialogView = builder.inflate(R.layout.dialog_change_password, null);
                AlertDialog dialog = new AlertDialog.Builder(UserActivity.this).create();

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                dialog.setView(dialogView);
                dialog.show();

                EditText edtPass = dialogView.findViewById(R.id.edtNewPass),
                        edtConfirm = dialogView.findViewById(R.id.edtConfirmPass);
                Button btnChangePass = dialogView.findViewById(R.id.btnChangePass);

                btnChangePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(edtPass.getText().toString().isEmpty()) edtPass.setError("Vui lòng nhập mật khẩu mới!");
                        else if(edtConfirm.getText().toString().isEmpty()) edtConfirm.setError("Vui lòng nhập lại mật khẩu mới!");
                        else {
                            if(! edtPass.getText().toString().equals(edtConfirm.getText().toString()))
                                Toast.makeText(UserActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                            else {
                                String newPassword = edtPass.getText().toString().trim();
                                currentUser.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(UserActivity.this, "Đổi thành công! Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                dialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(UserActivity.this, "Không thể đổi mật khẩu!", Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, e.toString());
                                            }
                                        });
                            }
                        }
                    }
                });

            }
        });

        imv_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdate();
            }
        });
        btnBackUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void setInfoUser() {
        db = FirebaseFirestore.getInstance();
        Log.e(TAG, currentUser.getUid());
        db.collection("user").document(currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null){
                                p = documentSnapshot.toObject(User.class);

                                name.setText(p.getName());
                                age.setText(String.valueOf(p.getAge()));
                                height.setText(String.valueOf(p.getHeight()));
                                weight.setText(String.valueOf(p.getWeight()));
                                bmi.setText(String.valueOf(p.BMICal()));
                                ttde.setText(String.valueOf(p.TTDECal()));
                                maxWater.setText(String.valueOf(p.WaterCal()));
                                switch (p.getAim()) {
                                    case 2:
                                        aim.setText("Tăng cân");
                                        maxCalories.setText(String.valueOf(Math.round(p.TTDECal() * 1.1 )));
                                        break;
                                    case 1:
                                        aim.setText("Giữ cân");
                                        maxCalories.setText(String.valueOf(Math.round(p.TTDECal() * 1)));
                                        break;
                                    case 0:
                                        aim.setText("Giảm cân");
                                        maxCalories.setText(String.valueOf(Math.round(p.TTDECal() * 0.9 )));
                                        break;
                                }
                                switch (p.getGender()) {
                                    case 0:
                                        gender.setText("Nữ");
                                        break;
                                    case 1:
                                        gender.setText("Nam");
                                        break;
                                }
                                switch (p.getExerciseFrequency()) {
                                    case 0:
                                        exerciseFrequency.setText("Không vận động");
                                        break;
                                    case 1:
                                        exerciseFrequency.setText("Vận động nhẹ (1-3 ngày/tuần)");
                                        break;
                                    case 2:
                                        exerciseFrequency.setText("Vận động vừa (4-5 ngày/tuần)");
                                        break;
                                    case 3:
                                        exerciseFrequency.setText("Vận động nặng (6-7 ngày/tuần)");
                                        break;
                                }
                            }
                        }
                    }
                });
    }


    private void showDialogUpdate() {

        User updateUser = p;
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_info_user, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.show();

        EditText editAge = view.findViewById(R.id.edit_age);
        RadioGroup rdgGender = view.findViewById(R.id.rdg_gender);
        RadioButton rdFemale = view.findViewById(R.id.rd_female);
        RadioButton rdMale = view.findViewById(R.id.rd_male);
        EditText editHeight = view.findViewById(R.id.edit_height);
        EditText editWeight = view.findViewById(R.id.edit_weight);
        RadioGroup rdgActivityFrequency = view.findViewById(R.id.rdg_exercise);
        RadioButton rdKhong = view.findViewById(R.id.rd_khong);
        RadioButton rdNhe = view.findViewById(R.id.rd_nhe);
        RadioButton rdVua = view.findViewById(R.id.rd_vua);
        RadioButton rdNang = view.findViewById(R.id.rd_nang);
        Button btnReup = view.findViewById(R.id.btn_reup);

        editAge.setText(String.valueOf(p.getAge()));
        editHeight.setText(String.valueOf(p.getHeight()));
        editWeight.setText(String.valueOf(p.getWeight()));

        if(p.getGender()==0) {
            rdFemale.setChecked(true);
        }else{
            rdMale.setChecked(true);
        }
        if(p.getExerciseFrequency()==0) {
            rdKhong.setChecked(true);
        }else if (p.getExerciseFrequency()==1) {
            rdNhe.setChecked(true);
        }else if(p.getExerciseFrequency()==2) {
            rdVua.setChecked(true);
        }else{
            rdNang.setChecked(true);
        }

        rdgActivityFrequency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rd_khong:
                        updateUser.setExerciseFrequency(0);
                        break;
                    case R.id.rd_nhe:
                        updateUser.setExerciseFrequency(1);
                        break;
                    case R.id.rd_vua:
                        updateUser.setExerciseFrequency(2);
                        break;
                    case R.id.rd_nang:
                        updateUser.setExerciseFrequency(3);
                        break;
                }
            }
        });
        rdgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rd_female:
                        updateUser.setGender(0);
                        break;
                    case R.id.rd_male:
                        updateUser.setGender(1);
                        break;
                }
            }
        });
        btnReup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser.setAge(Integer.parseInt(String.valueOf(editAge.getText())));
                updateUser.setHeight(Integer.parseInt(String.valueOf(editHeight.getText())));
                updateUser.setWeight(Integer.parseInt(String.valueOf(editWeight.getText())));

                db.collection("user").document(user.getUid()).set(updateUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Success Update!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Fail Update", e);
                            }
                        });
                dialog.dismiss();
                recreate();

            }
        });


    }

    public void showAimPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.aimm_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.i1:
                db.collection("user").document(user.getUid())
                        .update("aim", 2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Mục tiêu của bạn là tăng cân");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                recreate();
                return true;
            case R.id.i2:
                db.collection("user").document(user.getUid())
                        .update("aim", 1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Mục tiêu của bạn là giữ cân");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Mục tiêu của bạn là tăng cân");
                            }
                        });
                recreate();
                return true;
            case R.id.i3:

                db.collection("user").document(user.getUid())
                        .update("aim", 0)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
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
                recreate();
                return true;
            default:
                return false;
        }
    }


}