package com.ramish.spotit;
import static com.android.volley.Request.Method.POST;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignUp extends AppCompatActivity {

    Button sign_up;
    TextView login;
    EditText name, email, contact, password;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Spinner countrySpinner = findViewById(R.id.spinner_country_dropdown);
        ArrayAdapter<CharSequence> countrySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.countries_array, R.layout.spinner_dropdown_item);
        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countrySpinnerAdapter);

        Spinner citySpinner = findViewById(R.id.spinner_city_dropdown);
        ArrayAdapter<CharSequence> citySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.cities_array, R.layout.spinner_dropdown_item);
        citySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(citySpinnerAdapter);

        //Log.d("mAuth", mAuth.toString());

        sign_up = findViewById(R.id.button_sign_up);
        login = findViewById(R.id.text_login);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.contact);
        password = findViewById(R.id.password);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Retrieve IP address from strings.xml
                String ipAddress = getString(R.string.ip_addr);
                // Concatenate the retrieved IP address with the URL
                String url = "http://" + ipAddress + "/spot%20it/insert.php";
                StringRequest request = new StringRequest(POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject res = new JSONObject(response);
                                    if (res.getInt("status") == 1) {
                                        Toast.makeText(SignUp.this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                                        intent.putExtra("email", email.getText().toString());
                                        startActivity(intent);
                                        finish(); // Finish SignUp activity
                                    }
                                    else {
                                        Toast.makeText( SignUp.this, res.getString("message"), Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(SignUp.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", name.getText().toString());
                        params.put("contact", contact.getText().toString());
                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());
                        params.put("city", citySpinner.getSelectedItem().toString());
                        params.put("country", countrySpinner.getSelectedItem().toString());
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(SignUp.this);
                queue.add(request);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }
}