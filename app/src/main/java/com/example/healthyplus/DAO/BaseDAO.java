package com.example.healthyplus.DAO;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseDAO {
    protected FirebaseFirestore db;
    protected String collectionName;

    public BaseDAO(String collectionName) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionName = collectionName; // collection can lay
    }

    public BaseDAO() {
        this.db = FirebaseFirestore.getInstance();
        this.collectionName = "";
    }

    /*
    Hàm chuẩn để lấy 1 thuộc tính của 1 đối tượng trên firestore về (base function)
    input: 1. ID của đối tượng đó (document)
	        2. Thuộc tính cần lấy của đối tượng (field của document)

	output: trả về thuộc tính với kiểu dữ liệu Object
     */
    public Object getAttribute(String baseID, String attribute) {
        Map<String, Object> data = new HashMap<>();
        Task <DocumentSnapshot> task = this.db.collection(collectionName).document(baseID).get();
        if(task.isSuccessful()) {
            data = task.getResult().getData();
            // .get() return a Task <Documentsnapshot>
            // .getResult() return a list of Documentsnapshot
            // .getData() return a Map of data from the current document
            if (data.containsKey(attribute))
                return data.get(attribute);
            else Log.d(TAG, "Key not exists");
        }
        return null;
    }

    // Base function to search all documents has the attribute equals to values
    // Output: list of all document_id satisfy the condition
    public ArrayList searchByValue(String attribute, String value) {
        ArrayList<String> listOfID = new ArrayList<>();
        Task<QuerySnapshot> task = this.db.collection(collectionName).whereEqualTo(attribute, value).get();

        if (task.isSuccessful())
            for (QueryDocumentSnapshot document : task.getResult()) listOfID.add(document.getId());

        return listOfID;
    }
}
