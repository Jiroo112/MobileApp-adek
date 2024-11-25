package com.alphatz.adek.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Regist extends AppCompatActivity {

    private EditText editTextNamaLengkap, editTextEmail, editTextPassword, editTextRePassword;
    private Button buttonRegister;
    private String URL_REGISTER = "http://10.0.2.2/ads_mysql/account/registrasi_adek.php"; // URL untuk registrasi
    private static final String TAG = "Regist"; // For logging

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
                String namaLengkap = editTextNamaLengkap.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                String rePassword = editTextRePassword.getText().toString();

                if (!namaLengkap.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.equals(rePassword)) {
                    showPersonalInfoDialog(namaLengkap, email, password);
                } else {
                    Toast.makeText(Regist.this, "Please fill all fields and make sure passwords match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showPersonalInfoDialog(final String namaLengkap, final String email, final String password) {
        final Dialog dialog = new Dialog(Regist.this);
        dialog.setContentView(R.layout.dialog_personal_info);
        dialog.setCancelable(false);

        final EditText tanggalLahir = dialog.findViewById(R.id.input_tanggal_lahir);
        final EditText tinggiBadan = dialog.findViewById(R.id.input_tinggi_badan);
        final EditText beratBadan = dialog.findViewById(R.id.input_berat_badan);
        final Spinner spinnerGender = dialog.findViewById(R.id.spinner_gender);
        final RadioGroup radioGroupDiet = dialog.findViewById(R.id.radio_group_diet);

        Button buttonCancel = dialog.findViewById(R.id.button_cancel);
        Button buttonSave = dialog.findViewById(R.id.button_save);

        // Set up date picker
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        tanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Regist.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                tanggalLahir.setText(dateFormatter.format(calendar.getTime()));
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String tanggal = tanggalLahir.getText().toString().trim();
                    String tinggi = tinggiBadan.getText().toString().trim();
                    String berat = beratBadan.getText().toString().trim();
                    String gender = spinnerGender.getSelectedItemPosition() > 0 ? spinnerGender.getSelectedItem().toString() : "";

                    // Validasi: Pastikan semua field terisi
                    if (tanggal.isEmpty() || tinggi.isEmpty() || berat.isEmpty() || gender.isEmpty()) {
                        Toast.makeText(Regist.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int selectedDietId = radioGroupDiet.getCheckedRadioButtonId();
                    if (selectedDietId == -1) {
                        Toast.makeText(Regist.this, "Please select a diet type", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RadioButton selectedDiet = dialog.findViewById(selectedDietId);
                    String dietType = selectedDiet.getText().toString();

                    // Debug log
                    Log.d(TAG, "Sending registration data:");
                    Log.d(TAG, "Nama: " + namaLengkap);
                    Log.d(TAG, "Email: " + email);
                    Log.d(TAG, "Password: " + password);
                    Log.d(TAG, "Tanggal Lahir: " + tanggal);
                    Log.d(TAG, "Tinggi Badan: " + tinggi);
                    Log.d(TAG, "Berat Badan: " + berat);
                    Log.d(TAG, "Gender: " + gender);
                    Log.d(TAG, "Diet Type: " + dietType);

                    // Perform registration with all user data (name, email, password, personal info)
                    performRegistration(namaLengkap, email, password, tanggal, tinggi, berat, gender, dietType);
                    dialog.dismiss();  // Dismiss the dialog after saving

                } catch (Exception e) {
                    Log.e(TAG, "Error in buttonSave click: ", e);
                    Toast.makeText(Regist.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    // Step 1: Register user
    private void performRegistration(final String namaLengkap, final String email,
                                     final String password, final String tanggalLahir,
                                     final String tinggiBadan, final String beratBadan,
                                     final String gender, final String dietType) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Registration response: " + response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            Toast.makeText(Regist.this, message, Toast.LENGTH_LONG).show();
                            if (success) {
                                // Get user ID from the registration response
                                String userId = jsonResponse.optString("user_id", "");

                                if (!userId.isEmpty()) {
                                    // Call method to send personal information after successful registration
                                    updatePersonalInfo(userId, tanggalLahir, tinggiBadan, beratBadan, gender, dietType);
                                }
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: ", e);
                            Toast.makeText(Regist.this, "Registration error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: ", error);
                        Toast.makeText(Regist.this, "Network error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", namaLengkap);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Step 2: Update personal information after successful registration
    private void updatePersonalInfo(final String userId, final String tanggalLahir,
                                    final String tinggiBadan, final String beratBadan,
                                    final String gender, final String dietType) {
        String URL_UPDATE = "http://10.0.2.2/ads_mysql/account/update_personal_info.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Personal info update response: " + response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            Toast.makeText(Regist.this, message, Toast.LENGTH_LONG).show();
                            if (success) {
                                // Optionally, you can close the activity or navigate the user elsewhere
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: ", e);
                            Toast.makeText(Regist.this, "Update error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: ", error);
                        Toast.makeText(Regist.this, "Network error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("tanggal_lahir", tanggalLahir);
                params.put("tinggi_badan", tinggiBadan);
                params.put("berat_badan", beratBadan);
                params.put("gender", gender);
                params.put("tipe_diet", dietType);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
