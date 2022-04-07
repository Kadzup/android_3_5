package com.esc.laba3_5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    HorizontalScrollView mainLayout;
    TableLayout table;
    TabLayout tabs;

    DatabaseWorker worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init FB app to work with FB DB
        FirebaseApp.initializeApp(this);

        worker = new DatabaseWorker();
        worker.getAllChemicals();
        worker.getAllProviders();

        mainLayout = findViewById(R.id.linear_layout);

        table = new TableLayout(this);
        table.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        table.setShrinkAllColumns(true);
        table.setStretchAllColumns(false);
        table.setScrollContainer(true);
        table.layout(5,5,5,5);

        tabs = findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(
            new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    cleanTable();
                    buildTable(tab.getPosition());
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) { }
                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            }
        );

        cleanTable();
        buildTable(0);
    }

    void cleanTable(){
        table.removeAllViews();
        mainLayout.removeAllViews();
    }

    void buildTable(int index){
        switch(index){
            case 0:
                chemicals();
                break;
            case 1:
                providers();
                break;
            case 2:
                byType();
                break;
            case 3:
                byPrice();
                break;
            case 4:
                byProvider();
                break;
        }

        mainLayout.addView(table);
    }

    void chemicals(){
        CollectionReference collectionRef = worker.db.collection(DatabaseWorker.chemicalsCollection);
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
                    Collections.sort(chemicalsList, Comparator.comparing(Chemical::getId));
                    table.addView(createHeader(Chemical.headers));
                    for (Chemical chemical : chemicalsList) {
                        table.addView(createRow(chemical.toArrayList()));
                    }
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
    void providers(){
        CollectionReference collectionRef = worker.db.collection(DatabaseWorker.providersCollection);
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
                        documents.sort(Comparator.comparing(DocumentSnapshot::getId));
                        for(DocumentSnapshot document : documents){
                            String id = document.getId();
                            Map<String, Object> data = document.getData();
                            data.put("id", id);
                            providersList.add(new Provider(data));
                        }
                    }
                    table.addView(createHeader(Provider.headers));
                    for (Provider provider : providersList) {
                        table.addView(createRow(provider.toArrayList()));
                    }
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
    void byType(){
        CollectionReference collectionRef = worker.db.collection(DatabaseWorker.chemicalsCollection);
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

                    ArrayList<ArrayList<String>> data = new ArrayList<>();
                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("Type");
                    headers.add("Count");
                    headers.add("Price");

                    Integer i = 0;
                    String type = chemicalsList.get(i).type;
                    Integer count = 0;
                    Double price = 0.0;

                    while(i < chemicalsList.size()){
                        if(chemicalsList.get(i).type.contains(type)){
                            count += chemicalsList.get(i).count;
                            price += chemicalsList.get(i).price;
                            i++;
                        }
                        else{
                            data.add(new ArrayList<String>(Arrays.asList(type, count.toString(), price.toString())));
                            type = chemicalsList.get(i).type;
                            count = 0;
                            price = 0.0;
                        }
                    }

                    table.addView(createHeader(headers));
                    for (ArrayList<String> item : data) {
                        table.addView(createRow(item));
                    }
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
    void byPrice(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введіть вартість");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("Ввести", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Double price;
                try{price = Double.parseDouble(input.getText().toString());}
                catch(Exception ex){price = 100.0;}


                CollectionReference collectionRef = worker.db.collection(DatabaseWorker.chemicalsCollection);
                collectionRef.whereLessThanOrEqualTo("price", price).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                            table.addView(createHeader(Chemical.headers));
                            for (Chemical chemical : chemicalsList) {
                                table.addView(createRow(chemical.toArrayList()));
                            }
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
        });

        builder.show();
    }
    void byProvider(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введіть постачальника");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Ввести", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String provider;
                try {
                    provider = input.getText().toString();
                }catch(Exception ex){
                    provider = "Test";
                }
                CollectionReference collectionRef = worker.db.collection(DatabaseWorker.providersCollection);
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
                                CollectionReference collectionRef = worker.db.collection(DatabaseWorker.chemicalsCollection);
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
                                            table.addView(createHeader(Chemical.headers));
                                            for (Chemical chemical : chemicalsList) {
                                                table.addView(createRow(chemical.toArrayList()));
                                            }
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
        });

        builder.show();
    }

    TableRow createHeader(ArrayList<String> titles){
        TableRow row = new TableRow(MainActivity.this);
        row.layout(5,5,5,5);

        for(String title : titles){
            TextView tv = new TextView(MainActivity.this);
            tv.setPadding(3,3,3,3);
            tv.setWidth(500);
            tv.setGravity(0);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            tv.setText(title);

            row.addView(tv);
        }

        return row;
    }

    TableRow createRow(ArrayList<String> fields){
        TableRow row = new TableRow(MainActivity.this);
        row.layout(5,5,5,5);

        for(String field : fields){
            TextView tv = new TextView(MainActivity.this);
            tv.setPadding(3,3,3,3);
            tv.setGravity(0);
            tv.setWidth(450);
            tv.setTextColor(Color.parseColor("#838a92"));
            tv.setText(field);

            row.addView(tv);
        }

        return row;
    }
}