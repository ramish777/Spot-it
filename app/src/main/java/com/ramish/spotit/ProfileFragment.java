package com.ramish.spotit;
import static com.android.volley.Request.Method.GET;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    String Username,email;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView edit_nav, profilePhoto, coverPhoto;
    TextView logout, name, city, itemsPosted, itemsRented;
    RecyclerView profileYourRV;
    RecyclerView rentedRV;

    GridAdapter profileYourAdapter;
    FeaturedItemAdapter rentedAdapter;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    String userId;
    List<Item> profileYourList;
    List<Item> rentedList;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle args = getArguments();
        if (args != null) {
            email = args.getString("email", "");
            Username = args.getString("name", "");
            // Use 'userEmail' and 'userName' here in your fragment
        }

        edit_nav = view.findViewById(R.id.image_edit_nav);
        logout = view.findViewById(R.id.logout);
        profilePhoto = view.findViewById(R.id.profilePhoto);
        coverPhoto = view.findViewById(R.id.coverPhoto);
        name = view.findViewById(R.id.name);
        city = view.findViewById(R.id.city);
        itemsPosted = view.findViewById(R.id.itemsPosted);
        itemsRented = view.findViewById(R.id.itemsRented);
        profileYourRV = view.findViewById(R.id.profileYourRV);
        rentedRV = view.findViewById(R.id.rentedRV);

        String email2=email;
        Log.i("email: ",email2);

        // Retrieve IP address from strings.xml
        String ipAddress = getString(R.string.ip_addr);
        // Concatenate the retrieved IP address with the URL
        String url = "http://" + ipAddress + "/spot%20it/get_profile.php?email=" + email;
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
                                        name.setText(item.getString("name"));
                                        city.setText(item.getString("city"));
                                        Picasso.get().load(item.getString("coverUrl")).into(coverPhoto);
                                        Picasso.get().load(item.getString("profileUrl")).into(profilePhoto);

                                    }
                                }
                            }
                            else {
                                Toast.makeText( getContext(), res.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error JSON response", Toast.LENGTH_LONG).show();
                            Log.d("Response", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }){
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

        profileYourList = new ArrayList<>();
        profileYourAdapter = new GridAdapter(profileYourList, getContext(), "profile");
        profileYourRV.setAdapter(profileYourAdapter);
        RecyclerView.LayoutManager profileYourLM = new GridLayoutManager(getContext(), 2);
        profileYourRV.setLayoutManager(profileYourLM);

        rentedList = new ArrayList<>();
        rentedAdapter = new FeaturedItemAdapter(rentedList, getContext(), userId);
        rentedRV.setAdapter(rentedAdapter);
        RecyclerView.LayoutManager rentedLM = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rentedRV.setLayoutManager(rentedLM);

        // for recent items
        // Retrieve IP address from strings.xml
        ipAddress = getString(R.string.ip_addr);
        // Concatenate the retrieved IP address with the URL
        url = "http://" + ipAddress + "/spot%20it/get_all_items.php";
        request = new StringRequest(GET, url,
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
                                        profileYourList.add(m);
                                        profileYourAdapter.notifyDataSetChanged();

                                        rentedList.add(m);
                                        rentedAdapter.notifyDataSetChanged();
                                        Log.i("2", "first toast");
                                    }
                                }
                            }
                            else {
                                Toast.makeText( getContext(), res.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error JSON response", Toast.LENGTH_LONG).show();
                            Log.d("Response", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                return params;
            }
        };

        queue = Volley.newRequestQueue(getContext());
        queue.add(request);


        edit_nav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), EditProfile.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                mAuth = FirebaseAuth.getInstance();
//
//                mAuth.signOut();
//
//                Intent intent = new Intent(getActivity(), Login.class);
//                startActivity(intent);
//
//                getActivity().finish();

                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mAuth = FirebaseAuth.getInstance();

                            mAuth.signOut();

                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);

                            getActivity().finish();
                        }
                    }
                });
            }
        });

        return view;
    }
}