package com.example.farmersmarketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ListView listView = findViewById(R.id.listView);

        // Populate the list with some grocery items
        List<String> arrList = new ArrayList<>();
        arrList.add("Apples");
        arrList.add("Bananas");
        arrList.add("Carrots");
        arrList.add("Oranges");
        arrList.add("Pears");
        arrList.add("Potatoes");
        arrList.add("Tomatoes");

        // Creating an array adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, arrList );
        listView.setAdapter(arrayAdapter);
    }
}