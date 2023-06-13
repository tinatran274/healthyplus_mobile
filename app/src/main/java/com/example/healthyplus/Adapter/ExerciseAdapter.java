package com.example.healthyplus.Adapter;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Model.Exercise;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import android.os.Handler;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>{

    ArrayList <Exercise> list = new ArrayList<>();
    Context context;
    long startTime = 0;
    int calories = 0;
    Handler handler;
    FirebaseStorage storage;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    Thread timerThread;
    int hour, min, sec, milliSec;
    long tMilliSec, tStart, tStop, tBuff, tUpdate = 0L;
    public ExerciseAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseAdapter.ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseViewHolder holder, int position) {
        Exercise exercise = list.get(position);

        int caloriesPerHour = exercise.getCaloriesPerHour();
        String id = exercise.getId();
        String name = exercise.getName();
        String img = exercise.getImg();

        holder.txvExercise.setText(name);
        holder.txvCalories.setText(Integer.toString(caloriesPerHour));

        // get image of the exercise from firebase storage
        storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReferenceFromUrl(img);
        try {
            File localFile = File.createTempFile("images", "jpg");
            imgRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e(TAG, "Error getting exercise image");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        holder.cvExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog(caloriesPerHour);
            }
        });
    }

    private void showTimerDialog(int caloriesPerHour) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Bạn muốn nhập thời gian hay bấm giờ?");

        builder1.setPositiveButton("Nhập thời gian",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setCancelable(true);
                        // Khởi tạo layout cho dialog
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View dialogView = inflater.inflate(R.layout.dialog_add_time, null);


                        EditText edtCalo = dialogView.findViewById(R.id.edtTime);

                        builder2.setPositiveButton("Tính kết quả",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(edtCalo.getText().toString().isEmpty()){
                                            edtCalo.setError("Vui lòng nhập thời gian");
                                        }
                                        else {
                                            int calo = (int)caloriesPerHour * Integer.valueOf(edtCalo.getText().toString()) /60;
                                            Toast.makeText(context, calo + "", Toast.LENGTH_SHORT).show();
                                            dialogInterface.cancel();
                                        }

                                    }
                                });
                        builder2.setNegativeButton("Hủy",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                        builder2.setView(dialogView);
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                    }
                });
        builder1.setNegativeButton("Bấm giờ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        // Khởi tạo layout cho dialog
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View dialogView = inflater.inflate(R.layout.dialog_timer, null);

                        Button btnRestart = dialogView.findViewById(R.id.btnRestart);
                        ToggleButton btnStart = dialogView.findViewById(R.id.btnStart);
                        TextView txvCalorie = dialogView.findViewById(R.id.txvTimerCalories);
                        Chronometer chronometer = dialogView.findViewById(R.id.timerChronometer);


                        handler = new Handler();

                        builder.setNegativeButton("Hủy",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i){
                                        dialogInterface.cancel();
                                    }
                                });
                        builder.setPositiveButton("Lưu kết quả",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context, txvCalorie.getText().toString(), Toast.LENGTH_SHORT).show();
                                        dialogInterface.cancel();
                                    }
                                });
                        builder.setView(dialogView);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                tMilliSec = SystemClock.uptimeMillis() - tStart;
                                tUpdate = tBuff + tMilliSec;
                                sec = (int) tUpdate/1000;
                                min = sec/60;
                                sec = sec%60;
                                hour = min/60;
                                milliSec = (int) tUpdate%100;

                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        txvCalorie.setText(Integer.toString( (int) caloriesPerHour * sec / 3600));
                                        String time;
                                        if(min < 1) time = String.format(Locale.ENGLISH,"%02d:%02d", sec ,milliSec);
                                        else if(hour > 0) time =  String.format(Locale.ENGLISH,"%02:%02d:%02d:%02d",hour, min, sec ,milliSec);
                                        else {
                                            time = String.format(Locale.ENGLISH,"%02d:%02d:%02d", min, sec ,milliSec);
                                        }
                                        chronometer.setText(time);
                                    }
                                });
                                handler.postDelayed(this, 60);

                            }
                        };

                        btnStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if(b){
                                    tStart = SystemClock.uptimeMillis();
                                    handler.postDelayed(runnable, 0);
                                    chronometer.start();
                                }
                                else{
                                    tBuff += tMilliSec;
                                    handler.removeCallbacks(runnable);
                                    chronometer.stop();
                                }
                            }
                        });
                        btnRestart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tMilliSec = 0;
                                tStart = 0L;
                                tBuff = 0L;
                                tUpdate = 0L;
                                sec = min = milliSec = hour = 0;

                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        chronometer.setText("00:00:00");
                                    }
                                });
                            }
                        });

                    }
                });
        AlertDialog dialog1 = builder1.create();
        dialog1.show();

    }

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    public void setList(ArrayList<Exercise> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        CardView cvExercise;
        ImageView imageView;
        TextView txvExercise, txvCalories, txvCaloriesPerHour;
        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            cvExercise = itemView.findViewById(R.id.cvExercise);
            imageView = itemView.findViewById(R.id.imvExercise);
            txvExercise = itemView.findViewById(R.id.txvExercise);
            txvCalories = itemView.findViewById(R.id.txvCalories);
        }
    }
}
