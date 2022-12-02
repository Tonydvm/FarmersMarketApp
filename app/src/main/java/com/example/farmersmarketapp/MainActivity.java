package com.example.farmersmarketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Create a list of items and add them to the list view
        // TODO: populate the list with more products
        ListView listView = findViewById(R.id.listView);
        List<String> arrList = new ArrayList<>();
        arrList.add("Apples");
        arrList.add("Bananas");
        arrList.add("Carrots");
        arrList.add("Oranges");
        arrList.add("Pears");
        arrList.add("Potatoes");
        arrList.add("Tomatoes");

        // Set the list view to display the items in the list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, arrList );
        listView.setAdapter(arrayAdapter);

        // request location popup
        Button configButton = findViewById(R.id.button);
        TextView location = findViewById(R.id.location);
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setText("Kelowna, BC");
                // check if the app has permission to access the location
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // if the app does not have permission, request permission
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }
}