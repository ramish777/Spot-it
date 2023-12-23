package com.ramish.spotit;
import static com.android.volley.Request.Method.GET;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchResults extends AppCompatActivity {

    ImageView back_search;
    TextView searchQuery;
    RecyclerView searchRV;

    GridAdapter searchAdapter;

    String searchInput;
    List<Item> searchList;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Spinner filterSpinner = findViewById(R.id.spinner_filter_dropdown);
        ArrayAdapter<CharSequence> filterSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.filters_array, R.layout.filter_spinner_dropdown_item);
        filterSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterSpinnerAdapter);

        back_search = findViewById(R.id.image_back_search);
        searchQuery = findViewById(R.id.searchQuery);
        searchRV = findViewById(R.id.searchRV);

        searchInput = getIntent().getStringExtra("searchInput");
        searchQuery.setText("\"" + searchInput + "\"");

        searchList = new ArrayList<>();
        searchAdapter = new GridAdapter(searchList, SearchResults.this, "search");
        searchRV.setAdapter(searchAdapter);
        RecyclerView.LayoutManager searchLM = new GridLayoutManager(SearchResults.this, 2);
        searchRV.setLayoutManager(searchLM);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {

                    searchList.clear();
                    searchAdapter.notifyDataSetChanged();

                    // Retrieve IP address from strings.xml
                    String ipAddress = getString(R.string.ip_addr);
                    // Concatenate the retrieved IP address with the URL
                    String url = "http://" + ipAddress + "/spot%20it/get_all_items.php";
                    StringRequest request = new StringRequest(GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject res = new JSONObject(response);
                                        if (res.getInt("status") == 1) {
                                            // Check if the "items" key exists in the response
                                            if (res.has("items")) {
                                                JSONArray items = res.getJSONArray("items");
                                                for (int i = 0; i < items.length(); i++) {
                                                    JSONObject item = items.getJSONObject(i);
                                                    Item m = new Item(
                                                            item.getString("id"),
                                                            item.getString("owner"),
                                                            item.getString("name"),
                                                            Double.parseDouble(item.getString("rate")),
                                                            item.getString("description"),
                                                            item.getString("city"),
                                                            item.getString("pictureUrl")
                                                    );
                                                    searchList.add(m);
                                                    searchAdapter.notifyDataSetChanged();
                                                    Log.i("2", "first toast");
                                                }
                                            }
                                        }
                                        else {
                                            Toast.makeText( SearchResults.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(SearchResults.this, "Error JSON response", Toast.LENGTH_LONG).show();
                                        Log.d("Response", response);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error.getMessage() != null) {
                                        Toast.makeText(SearchResults.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    RequestQueue queue = Volley.newRequestQueue(SearchResults.this);
                    queue.add(request);
                }

                else if (i == 1) {

                    searchList.clear();
                    searchAdapter.notifyDataSetChanged();

                    // Retrieve IP address from strings.xml
                    String ipAddress = getString(R.string.ip_addr);
                    // Concatenate the retrieved IP address with the URL
                    String url = "http://" + ipAddress + "/spot%20it/get_all_items_asc.php";
                    StringRequest request = new StringRequest(GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject res = new JSONObject(response);
                                        if (res.getInt("status") == 1) {
                                            // Check if the "items" key exists in the response
                                            if (res.has("items")) {
                                                JSONArray items = res.getJSONArray("items");
                                                for (int i = 0; i < items.length(); i++) {
                                                    JSONObject item = items.getJSONObject(i);
                                                    Item m = new Item(
                                                            item.getString("id"),
                                                            item.getString("owner"),
                                                            item.getString("name"),
                                                            Double.parseDouble(item.getString("rate")),
                                                            item.getString("description"),
                                                            item.getString("city"),
                                                            item.getString("pictureUrl")
                                                    );
                                                    searchList.add(m);
                                                    searchAdapter.notifyDataSetChanged();
                                                    Log.i("2", "first toast");
                                                }
                                            }
                                        }
                                        else {
                                            Toast.makeText( SearchResults.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(SearchResults.this, "Error JSON response", Toast.LENGTH_LONG).show();
                                        Log.d("Response", response);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error.getMessage() != null) {
                                        Toast.makeText(SearchResults.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    RequestQueue queue = Volley.newRequestQueue(SearchResults.this);
                    queue.add(request);
                }

                else if (i == 2) {

                    searchList.clear();
                    searchAdapter.notifyDataSetChanged();

                    // Retrieve IP address from strings.xml
                    String ipAddress = getString(R.string.ip_addr);
                    // Concatenate the retrieved IP address with the URL
                    String url = "http://" + ipAddress + "/spot%20it/get_all_items_des.php";
                    StringRequest request = new StringRequest(GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject res = new JSONObject(response);
                                        if (res.getInt("status") == 1) {
                                            // Check if the "items" key exists in the response
                                            if (res.has("items")) {
                                                JSONArray items = res.getJSONArray("items");
                                                for (int i = 0; i < items.length(); i++) {
                                                    JSONObject item = items.getJSONObject(i);
                                                    Item m = new Item(
                                                            item.getString("id"),
                                                            item.getString("owner"),
                                                            item.getString("name"),
                                                            Double.parseDouble(item.getString("rate")),
                                                            item.getString("description"),
                                                            item.getString("city"),
                                                            item.getString("pictureUrl")
                                                    );
                                                    searchList.add(m);
                                                    searchAdapter.notifyDataSetChanged();
                                                    Log.i("2", "first toast");
                                                }
                                            }
                                        }
                                        else {
                                            Toast.makeText( SearchResults.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(SearchResults.this, "Error JSON response", Toast.LENGTH_LONG).show();
                                        Log.d("Response", response);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error.getMessage() != null) {
                                        Toast.makeText(SearchResults.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    RequestQueue queue = Volley.newRequestQueue(SearchResults.this);
                    queue.add(request);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        back_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchResults.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}