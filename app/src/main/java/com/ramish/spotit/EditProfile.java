package com.ramish.spotit;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    String Useremail;
    String encodedImage;
    String imageName;
    String profileUrl,coverUrl;
    ImageView back_edit;
    LinearLayout changeDp, changeCover;
    TextView save, email;
    EditText name, contact;

    String userId, dpUrl;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        Useremail = intent.getStringExtra("email");

        Spinner countrySpinner = findViewById(R.id.spinner_country_dropdown);
        ArrayAdapter<CharSequence> countrySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.countries_array, R.layout.spinner_dropdown_item);
        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countrySpinnerAdapter);

        Spinner citySpinner = findViewById(R.id.spinner_city_dropdown);
        ArrayAdapter<CharSequence> citySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.cities_array, R.layout.spinner_dropdown_item);
        citySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(citySpinnerAdapter);

        back_edit = findViewById(R.id.image_back_edit);
        changeDp = findViewById(R.id.changeDp);
        changeCover = findViewById(R.id.changeCover);
        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        email = findViewById(R.id.email);

        save = findViewById(R.id.save);
        email.setText(Useremail);

        // Retrieve IP address from strings.xml
        String ipAddress = getString(R.string.ip_addr);
        // Concatenate the retrieved IP address with the URL
        String url = "http://" + ipAddress + "/spot%20it/get_profile.php?email=" + Useremail;
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
                                        contact.setText(item.getString("contact"));
                                    }
                                }
                            }
                            else {
                                Toast.makeText( EditProfile.this, res.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EditProfile.this, "Error JSON response", Toast.LENGTH_LONG).show();
                            Log.d("Response", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            Toast.makeText(EditProfile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }){
        };

        RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
        queue.add(request);



        changeDp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });

        changeCover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //should update here
                // Retrieve IP address from strings.xml
                String ipAddress = getString(R.string.ip_addr);
                // Concatenate the retrieved IP address with the URL
                String url = "http://" + ipAddress + "/spot%20it/update_profile.php";
                StringRequest request = new StringRequest(POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject res = new JSONObject(response);
                                    if (res.getInt("status") == 1) {
                                        Toast.makeText(EditProfile.this, "Edit Profile Successful", Toast.LENGTH_LONG).show();
                                        Log.i("dpUrl",profileUrl);
                                        Log.i("coverUrl",coverUrl);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText( EditProfile.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(EditProfile.this, "Error parsing JSON response", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.getMessage() != null) {
                                    Toast.makeText(EditProfile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email",email.toString());
                        params.put("name",name.getText().toString());
                        params.put("contact",contact.getText().toString());
                        params.put("profileUrl",profileUrl);
                        params.put("coverUrl",coverUrl);
                        params.put("city",citySpinner.getSelectedItem().toString());
                        params.put("country",countrySpinner.getSelectedItem().toString());
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
                queue.add(request);

                Intent intent = new Intent(EditProfile.this, MainActivity.class);
                intent.putExtra("email",Useremail);
                startActivity(intent);
            }
        });

        back_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EditProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri image = data.getData();
            //profile url
            try {
                InputStream imageStream = getContentResolver().openInputStream(image);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(selectedImage);
                imageName = getImageName(image);
                // Retrieve IP address from strings.xml
                String ipAddress = getString(R.string.ip_addr);
                // Concatenate the retrieved IP address with the URL
                String url = "http://" + ipAddress + "/spot%20it/insert_pic.php";
                StringRequest request = new StringRequest(POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject res = new JSONObject(response);
                                    if (res.getInt("status") == 1) {
                                        profileUrl = "http://" + ipAddress + "/spot%20it/pictures/"+imageName;
                                        Log.d("PictureURL", profileUrl);
                                        Toast.makeText(EditProfile.this, profileUrl, Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText( EditProfile.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error gracefully
                                    Toast.makeText(EditProfile.this, "Error parsing JSON response", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.getMessage() != null) {
                                    Toast.makeText(EditProfile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("encoded_string",encodedImage);
                        params.put("image_name",imageName);
                        params.put("ipadrr",ipAddress);
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
                queue.add(request);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        else if (requestCode == 101 && resultCode == RESULT_OK) {
            Uri image = data.getData();
            //cover url
            //profile url
            try {
                InputStream imageStream = getContentResolver().openInputStream(image);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(selectedImage);
                imageName = getImageName(image);
                // Retrieve IP address from strings.xml
                String ipAddress = getString(R.string.ip_addr);
                // Concatenate the retrieved IP address with the URL
                String url = "http://" + ipAddress + "/spot%20it/insert_pic.php";
                StringRequest request = new StringRequest(POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject res = new JSONObject(response);
                                    if (res.getInt("status") == 1) {
                                        coverUrl = "http://" + ipAddress + "/spot%20it/pictures/"+imageName;
                                        Log.d("PictureURL", coverUrl);
                                        Toast.makeText(EditProfile.this, coverUrl, Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText( EditProfile.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error gracefully
                                    Toast.makeText(EditProfile.this, "Error parsing JSON response", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.getMessage() != null) {
                                    Toast.makeText(EditProfile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("encoded_string",encodedImage);
                        params.put("image_name",imageName);
                        params.put("ipadrr",ipAddress);
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
                queue.add(request);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
    private String encodeImage(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }
    private String getImageName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    result = cursor.getString(index);
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}