package com.ramish.spotit;
import static android.opengl.ETC1.encodeImage;

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
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostItem extends AppCompatActivity {
    ImageView close_post_item;
    LinearLayout uploadImage, uploadVideo;
    EditText name, rate, description;
    Button post;

    String userId, imageUrl, videoUrl;

    String day, month, year;

    String encodedImage;
    String imageName;
    String pictureUrl;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);

        // Retrieve the email passed from the Login activity
        String email = getIntent().getStringExtra("email");

        Calendar calendar = Calendar.getInstance();
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
        year = String.valueOf(calendar.get(Calendar.YEAR));
        imageUrl = "";
        videoUrl = "";

        close_post_item = findViewById(R.id.image_close_post_item);
        uploadImage = findViewById(R.id.uploadImage);
        uploadVideo = findViewById(R.id.uploadVideo);
        name = findViewById(R.id.name);
        rate = findViewById(R.id.rate);
        description = findViewById(R.id.description);
        post = findViewById(R.id.post);

//        mAuth = FirebaseAuth.getInstance();
//
//        userId = mAuth.getUid().toString();
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();

        Spinner citySpinner = findViewById(R.id.spinner_city_dropdown);
        ArrayAdapter<CharSequence> citySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.cities_array, R.layout.spinner_dropdown_item);
        citySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(citySpinnerAdapter);

        uploadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 102);
            }
        });

        uploadVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 103);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Retrieve IP address from strings.xml
                String ipAddress = getString(R.string.ip_addr);
                // Concatenate the retrieved IP address with the URL
                String url = "http://" + ipAddress + "/spot%20it/insert_items.php";
                StringRequest request = new StringRequest(POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject res = new JSONObject(response);
                                    if (res.getInt("status") == 1) {
                                        Toast.makeText(PostItem.this, "Post Item Successful", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText( PostItem.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(PostItem.this, "Error parsing JSON response", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.getMessage() != null) {
                                    Toast.makeText(PostItem.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("owner",email.toString());
                        params.put("name",name.getText().toString());
                        params.put("rate",rate.getText().toString());
                        params.put("description",description.getText().toString());
                        params.put("city",citySpinner.getSelectedItem().toString());
                        params.put("pictureUrl",pictureUrl);
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(PostItem.this);
                queue.add(request);

            }
        });

        close_post_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PostItem.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
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
                                        pictureUrl = "http://" + ipAddress + "/spot%20it/pictures/"+imageName;
                                        Log.d("PictureURL", pictureUrl);
                                        Toast.makeText(PostItem.this, pictureUrl, Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText( PostItem.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error gracefully
                                    Toast.makeText(PostItem.this, "Error parsing JSON response", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.getMessage() != null) {
                                    Toast.makeText(PostItem.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

                RequestQueue queue = Volley.newRequestQueue(PostItem.this);
                queue.add(request);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        else if (requestCode == 103 && resultCode == RESULT_OK) {
            Uri video = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(video);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(selectedImage);
                imageName = getImageName(video);
                Toast.makeText(this, "Image Name: " + imageName, Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(PostItem.this, "Post Item Successful", Toast.LENGTH_LONG).show();
//                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
//                                        intent.putExtra("email", email.getText().toString());
//                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText( PostItem.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.getMessage() != null) {
                                    Toast.makeText(PostItem.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

                RequestQueue queue = Volley.newRequestQueue(PostItem.this);
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