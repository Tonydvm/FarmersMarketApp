package com.example.farmersmarketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // clear the file and write new test data
//        try {
//            FileOutputStream fos = openFileOutput("data.txt", MODE_PRIVATE);
//            fos.write("".getBytes());
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        writeData(this.findViewById(android.R.id.content));

        // Get data from file
        String fileName = "data.txt";
        List<String> list = new ArrayList<String>();
        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            // populate listview with data from file
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        // Searchbar Functionality
        EditText editText = (EditText) findViewById(R.id.editText);
        Button searchButton = (Button) findViewById(R.id.button2);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get text input from edittext
                String input = editText.getText().toString();
                List<String> searchList = null;
                if (input == null || input.isEmpty()) {
                    // revert to original list by reading file again
                    String fileName = "data.txt";
                    List<String> list = new ArrayList<String>();
                    try {
                        FileInputStream fis = openFileInput(fileName);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        // populate listview with data from file
                        String line;
                        while ((line = br.readLine()) != null) {
                            list.add(line);
                        }
                        br.close();
                        isr.close();
                        fis.close();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                        ListView listView = (ListView) findViewById(R.id.listview);
                        listView.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error reading file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // search for input in list
                    searchList = new ArrayList<String>();
                    for (String item : list) {
                        if (item.toLowerCase().contains(input.toLowerCase())) {
                            searchList.add(item);
                        }
                    }
                    // display search results
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, searchList);
                    ListView listView = (ListView) findViewById(R.id.listview);
                    listView.setAdapter(adapter);
                }
            }
        });

        // Name Button Functionality
        Button nameButton = (Button) findViewById(R.id.name);
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        list.sort(String::compareToIgnoreCase);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                    ListView listView = (ListView) findViewById(R.id.listview);
                    listView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error sorting by name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Price Button Functionality
        Button priceButton = (Button) findViewById(R.id.price);
        priceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        list.sort((s1, s2) -> {
                            String s1Price = s1.substring(s1.indexOf("$") + 1);
                            String s2Price = s2.substring(s2.indexOf("$") + 1);
                            return s1Price.compareTo(s2Price);
                        });
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                    ListView listView = (ListView) findViewById(R.id.listview);
                    listView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error sorting by price", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ASC DESC Button Functionality
        Button ascButton = (Button) findViewById(R.id.ascending);
        ascButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(list);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                ListView listView = (ListView) findViewById(R.id.listview);
                listView.setAdapter(adapter);
            }
        });

        // request location popup
        ImageButton configButton = findViewById(R.id.button);
        TextView locationText = findViewById(R.id.location);
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // get location from user, if access to location is granted, if not then request access
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // get location
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        // get latitude and longitude from location object and display in textview
                        if(location!=null){
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            // get city + province/state name from latitude and longitude
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            String cityName = addresses.get(0).getLocality();
                            String stateName = addresses.get(0).getAdminArea();
                            // display city name
                            locationText.setText(cityName + ", " + stateName);
                        } else {
                            Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // request permission
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error getting location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to write test data to data.txt file
    public void writeData(View view) {
        String fileName = "data.txt";
        String data = "Apples, $2.99, 5\nOranges, $3.99, 10\nBananas, $1.99, 15";
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
            Toast.makeText(this, "Data written to file", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error writing to file", Toast.LENGTH_SHORT).show();
        }
    }
}