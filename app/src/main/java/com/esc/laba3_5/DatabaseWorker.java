package com.esc.laba3_5;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DatabaseWorker {
    public FirebaseFirestore db;
    public static final String providersCollection = "providers";
    public static final String chemicalsCollection = "chemicals";
    public ArrayList<Provider> providers;
    public ArrayList<Chemical> chemicals;

    public DatabaseWorker() {
        db = FirebaseFirestore.getInstance();
        providers = new ArrayList<>();
        chemicals = new ArrayList<>();
    }

    public void getAllProviders() {
        CollectionReference collectionRef = db.collection(providersCollection);
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    ArrayList<Provider> providersList = new ArrayList<Provider>();

                    if(documents.isEmpty() == false) {
                        for(DocumentSnapshot document : documents){
                            String id = document.getId();
                            Map<String, Object> data = document.getData();
                            data.put("id", id);
                            providersList.add(new Provider(data));
                        }
                    }
                    // Add all to your list
                    providers = (providersList);
                    Log.d(TAG, "onSuccess: " + providersList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error getting data!!!");
            }
        });
    }

    public void getAllChemicalsByType(){
        CollectionReference collectionRef = db.collection(chemicalsCollection);
        collectionRef.orderBy("type").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    ArrayList<Chemical> chemicalsList = new ArrayList<Chemical>();

                    if(documents.isEmpty() == false) {
                        for(DocumentSnapshot document : documents){
                            String id = document.getId();
                            Map<String, Object> data = document.getData();
                            data.put("id", id);
                            chemicalsList.add(new Chemical(data));
                        }
                    }
                    // Add all to your list
                    chemicals = (chemicalsList);
                    Log.d(TAG, "onSuccess: " + chemicalsList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error getting data!!!");
            }
        });
    }

    public void getAllChemicalsLessPrice(double price){
        CollectionReference collectionRef = db.collection(chemicalsCollection);
        collectionRef.whereEqualTo("type", "Порошок").whereLessThanOrEqualTo("price", price).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    ArrayList<Chemical> chemicalsList = new ArrayList<Chemical>();

                    if(documents.isEmpty() == false) {
                        for(DocumentSnapshot document : documents){
                            String id = document.getId();
                            Map<String, Object> data = document.getData();
                            data.put("id", id);
                            chemicalsList.add(new Chemical(data));
                        }
                    }
                    // Add all to your list
                    chemicals = (chemicalsList);
                    Log.d(TAG, "onSuccess: " + chemicalsList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error getting data!!!");
            }
        });
    }

    public void getAllChemicalsWithProvider(String provider){
        CollectionReference collectionRef = db.collection(providersCollection);
        collectionRef.whereEqualTo("title", provider).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    if(documents.isEmpty() == false) {
                        String code = documents.get(0).getString("code");
                        CollectionReference collectionRef = db.collection(chemicalsCollection);
                        collectionRef.whereEqualTo("provider", code).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                if (documentSnapshots.isEmpty()) {
                                    Log.d(TAG, "onSuccess: LIST EMPTY");
                                    return;
                                } else {
                                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                                    ArrayList<Chemical> chemicalsList = new ArrayList<Chemical>();

                                    if(documents.isEmpty() == false) {
                                        for(DocumentSnapshot document : documents){
                                            String id = document.getId();
                                            Map<String, Object> data = document.getData();
                                            data.put("id", id);
                                            chemicalsList.add(new Chemical(data));
                                        }
                                    }
                                    // Add all to your list
                                    chemicals = (chemicalsList);
                                    Log.d(TAG, "onSuccess: " + chemicalsList);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG,"Error getting data!!!");
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error getting data!!!");
            }
        });
    }

    public void getAllChemicals() {
        CollectionReference collectionRef = db.collection(chemicalsCollection);
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    ArrayList<Chemical> chemicalsList = new ArrayList<Chemical>();

                    if(documents.isEmpty() == false) {
                        for(DocumentSnapshot document : documents){
                            String id = document.getId();
                            Map<String, Object> data = document.getData();
                            data.put("id", id);
                            chemicalsList.add(new Chemical(data));
                        }
                    }
                    // Add all to your list
                    chemicals = (chemicalsList);
                    Log.d(TAG, "onSuccess: " + chemicalsList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error getting data!!!");
            }
        });
    }
}
