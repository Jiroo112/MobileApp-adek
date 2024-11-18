package com.alphatz.adek.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alphatz.adek.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Regist extends AppCompatActivity {

    private EditText editTextNamaLengkap, editTextEmail, editTextPassword, editTextRePassword;
    private Button buttonRegister;
    private String URL_REGISTER = "http://10.0.2.2/ads_mysql/registrasi_adek.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        editTextNamaLengkap = findViewById(R.id.name_regist);
        editTextEmail = findViewById(R.id.input_emailRegist);
        editTextPassword = findViewById(R.id.input_password);
        editTextRePassword = findViewById(R.id.input_repasword_regist);
        buttonRegister = findViewById(R.id.btn_regist);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaLengkap = editTextNamaLengkap.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String rePassword = editTextRePassword.getText().toString();

                if (!namaLengkap.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.equals(rePassword)) {
                    register(namaLengkap, email, password);
                } else {
                    Toast.makeText(Regist.this, "Please fill all fields and make sure passwords match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(final String namaLengkap, final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            Toast.makeText(Regist.this, message, Toast.LENGTH_LONG).show();
                            if (success) {
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Regist.this, "Parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Regist.this, "Registration failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", namaLengkap);
                params.put("email", email);
                params.put("password", password);
                params.put("re_password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
