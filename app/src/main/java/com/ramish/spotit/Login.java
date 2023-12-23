package com.ramish.spotit;
import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextView forgot_password, sign_up;
    EditText email, password;
    Button login;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        forgot_password = findViewById(R.id.text_forgot_password);
        sign_up = findViewById(R.id.text_sign_up);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.button_login);

        forgot_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Retrieve IP address from strings.xml
                String ipAddress = getString(R.string.ip_addr);
                // Concatenate the retrieved IP address with the URL
                String url = "http://" + ipAddress + "/spot%20it/login.php";

                StringRequest request = new StringRequest(POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);

                                    int success = jsonResponse.getInt("success");
                                    String message = jsonResponse.getString("message");

                                    if (success == 1) {
                                        // Retrieve the name from the JSON response
                                        String name = jsonResponse.optString("name", "");
                                        Toast.makeText(Login.this, "Login Successful "+name, Toast.LENGTH_LONG).show();
                                        // Login successful, navigate to the next activity
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.putExtra("email", email.getText().toString());
                                        intent.putExtra("name", name);
                                        startActivity(intent);
                                        finish(); // Finish the current activity if needed
                                    } else {
                                        // Login failed, show an error message or handle accordingly
                                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Login.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                                Toast.makeText(Login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());
                        return params;
                    }
                };
                // Add the request to the RequestQueue
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(request);
            }
        });

    }
}