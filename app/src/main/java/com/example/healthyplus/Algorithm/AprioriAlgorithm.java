package com.example.healthyplus.Algorithm;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.healthyplus.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AprioriAlgorithm {

    List<String> transaction = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    User u;

    public AprioriAlgorithm() {


    }

    public List<Set<String>> findFrequentItemsets(List<Set<String>> transactions, double minSupport) {
        List<Set<String>> frequentItemsets = new ArrayList<>();

        Map<Set<String>, Integer> itemsetCounts = new HashMap<>();
        for (Set<String> transaction : transactions) {
            for (String item : transaction) {
                Set<String> itemset = new HashSet<>(Collections.singletonList(item));
                itemsetCounts.put(itemset, itemsetCounts.getOrDefault(itemset, 0) + 1);
            }
        }

        int transactionCount = transactions.size();
        for (Map.Entry<Set<String>, Integer> entry : itemsetCounts.entrySet()) {
            if ((double) entry.getValue() / transactionCount >= minSupport) {
                frequentItemsets.add(entry.getKey());
            }
        }

        return frequentItemsets;
    }
}
