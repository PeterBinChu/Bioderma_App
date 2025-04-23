package com.example.bioderma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


import Interface.FirebaseAdapter;
import Objects.Product;
import fragments.DescriptionFragment;

import fragments.StoreFragment;

public class DownloadingScreen extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAdapter firebaseAdapter;
    private static StoreFragment storeFragment;
    private ArrayList<Product> mList;
    private DescriptionFragment descriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeFragment = new StoreFragment();
        descriptionFragment=new DescriptionFragment();
        mList = new ArrayList<>();
        this.database = FirebaseDatabase.getInstance("https://bioderma-d8248-default-rtdb.firebaseio.com/");
        this.reference = database.getReference().child("Bioderma Products");


        setContentView(R.layout.activity_downloading_screen);
        firebaseAdapter = new FirebaseAdapter() {
            @Override
            public void readInfo() {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        mList.clear();
                        for (DataSnapshot single : snapshot.getChildren()) {
                            Product product = single.getValue(Product.class);
                            mList.add(product);
                        }

                        storeFragment.setMlist(mList);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        };
        firebaseAdapter.readInfo();
    }

    protected  DescriptionFragment putDescriptionFragment(){return descriptionFragment;}
    protected StoreFragment putStoreFragment(){
        return storeFragment;
    }
}