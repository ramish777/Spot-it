package com.ramish.spotit;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemDetails extends AppCompatActivity {

    ImageView back_item, itemImg, ownerImg, message;
    TextView report, itemName, rate, city, date, description, ownerName, ownerRentedCount;
    Button rent;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    String userId, itemId, ownerId, dateString, cityString, rateString, itemNameString, imageUrlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        back_item = findViewById(R.id.image_back_item);
        report = findViewById(R.id.text_report);
        itemImg = findViewById(R.id.itemImg);
        ownerImg = findViewById(R.id.ownerImg);
        message = findViewById(R.id.message);
        itemName = findViewById(R.id.itemName);
        rate = findViewById(R.id.rate);
        city = findViewById(R.id.city);
        date = findViewById(R.id.date);
        description = findViewById(R.id.description);
        ownerName = findViewById(R.id.ownerName);
        ownerRentedCount = findViewById(R.id.ownerRentedCount);
        rent = findViewById(R.id.rent);

        back_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ItemDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ItemDetails.this, Report.class);
                startActivity(intent);
            }
        });

//        mAuth = FirebaseAuth.getInstance();
//        userId = mAuth.getUid().toString();

        itemId = getIntent().getStringExtra("itemId");
        Log.i("item id : ",itemId);
        //mDatabase = FirebaseDatabase.getInstance().getReference();

        // Retrieve IP address from strings.xml
        String ipAddress = getString(R.string.ip_addr);
        // Concatenate the retrieved IP address with the URL
        String url = "http://" + ipAddress + "/spot%20it/get_single_item.php";

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
                                        description.setText(item.getString("description"));
                                        rate.setText(item.getString("rate"));
                                        city.setText(item.getString("city"));
                                        itemName.setText(item.getString("name"));
                                        ownerName.setText(item.getString("owner"));


                                        Log.i("2", "first toast");
                                    }
                                }
                            }
                            else {
                                Toast.makeText( ItemDetails.this, res.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ItemDetails.this, "Error JSON response", Toast.LENGTH_LONG).show();
                            Log.d("Response", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(ItemDetails.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("itemId", itemId);
                return params;
            }
        };
        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(ItemDetails.this);
        queue.add(request);

        message.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                //create a chat in chats
//                // get key by pushing for chatId
//                //add the chatId to both users in userChats
//
//                String chatId = mDatabase.child("chats").push().getKey();
//
//                Chat chat = new Chat(chatId, userId, ownerId, "");
//
//                mDatabase.child("chats").child(chatId).setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        if (task.isSuccessful()) {
//
//                            mDatabase.child("userChats").child(userId).child(chatId).child("id").setValue(chatId).addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//
//                                    if (task.isSuccessful()) {
//
//                                        Toast.makeText(ItemDetails.this, "Chat created", Toast.LENGTH_LONG).show();
//                                    }
//
//                                    else {
//
//                                        Toast.makeText(ItemDetails.this, "Could not create chat", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//
//                            mDatabase.child("userChats").child(ownerId).child(chatId).child("id").setValue(chatId).addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//
//                                    if (task.isSuccessful()) {
//
//                                        Toast.makeText(ItemDetails.this, "Chat created", Toast.LENGTH_LONG).show();
//                                    }
//
//                                    else {
//
//                                        Toast.makeText(ItemDetails.this, "Could not create chat", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//
//                            Intent intent = new Intent(ItemDetails.this, DirectMessage.class);
//                            intent.putExtra("chatId", chatId);
//                            intent.putExtra("customerId", userId);
//                            intent.putExtra("ownerId", ownerId);
//                            startActivity(intent);
//                        }
//
//                        else {
//
//                            Toast.makeText(ItemDetails.this, "Could not create chat", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ItemDetails.this, RentItem.class);
                intent.putExtra("itemId", itemId);
                intent.putExtra("customerId", userId);
                intent.putExtra("ownerId", ownerId);
                intent.putExtra("rate", rateString);
                intent.putExtra("itemName", itemNameString);
                intent.putExtra("city", cityString);
                intent.putExtra("date", dateString);
                intent.putExtra("imageUrl", imageUrlString);
                startActivity(intent);
            }
        });
    }
}