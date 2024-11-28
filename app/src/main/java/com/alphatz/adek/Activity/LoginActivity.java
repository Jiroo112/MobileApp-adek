package com.alphatz.adek.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textViewSignUp;

    String URL_LOGIN = "http://10.0.2.2/ads_mysql/account/login_adek.php";

    SharedPreferences sharedPreferences;
    public static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAMA_LENGKAP = "namaLengkap";
    public static final String KEY_ID_USER = "idUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            String email = sharedPreferences.getString(KEY_EMAIL, "");
            String namaLengkap = sharedPreferences.getString(KEY_NAMA_LENGKAP, "");
            String idUser = sharedPreferences.getString(KEY_ID_USER, "");
            startDashboardActivity(email, namaLengkap, idUser);
            finish();
            return;
        }

        editTextEmail = findViewById(R.id.username_text);
        editTextPassword = findViewById(R.id.input_password);
        buttonLogin = findViewById(R.id.btn_login);
        textViewSignUp = findViewById(R.id.register);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    login(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Kolom email dan password harus diisi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Regist.class);
                startActivity(intent);
            }
        });
    }

    private void login(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                JSONObject user = jsonResponse.getJSONObject("user");
                                String namaLengkap = user.getString("nama_lengkap");
                                String idUser = user.getString("id_user");

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                                editor.putString(KEY_EMAIL, email);
                                editor.putString(KEY_NAMA_LENGKAP, namaLengkap);
                                editor.putString(KEY_ID_USER, idUser);
                                editor.apply();

                                startDashboardActivity(email, namaLengkap, idUser);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Kesalahan parsing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Gagal login: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void startDashboardActivity(String email, String namaLengkap, String idUser) {
        Intent intent = new Intent(LoginActivity.this, Dashboard.class);
        intent.putExtra("email", email);
        intent.putExtra("namaLengkap", namaLengkap);
        intent.putExtra("idUser", idUser);
        startActivity(intent);
    }

    public static void logout(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
