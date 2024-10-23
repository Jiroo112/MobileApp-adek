package com.alphatz.adek.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class ChangepwFragment extends Fragment {

    private EditText editTextNewPassword, editTextConfirmPassword;
    private Button buttonUpdatePassword;
    private String URL_UPDATE_PASSWORD = "http://10.0.2.2/ads_mysql/updatepassword.php"; // Ganti dengan URL yang sesuai
    private String userEmail; // Menyimpan email pengguna

    public ChangepwFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_changepw, container, false);

        editTextNewPassword = view.findViewById(R.id.input_new_password);
        editTextConfirmPassword = view.findViewById(R.id.input_confirm_password);
        buttonUpdatePassword = view.findViewById(R.id.btn_update_password);

        // Ambil email dari SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", getContext().MODE_PRIVATE);
        userEmail = sharedPreferences.getString("username", null); // Mengambil email dari SharedPreferences

        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = editTextNewPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (!newPassword.isEmpty() && newPassword.equals(confirmPassword) && newPassword.length() >= 8) {
                    updatePassword(newPassword);
                } else {
                    Toast.makeText(getActivity(), "Passwords do not match or are less than 8 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void updatePassword(final String newPassword) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            if (success) {
                                // Kembali ke ProfileFragment setelah berhasil
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new ProfileFragment())
                                        .addToBackStack(null) // Menambahkan transaksi ini ke back stack
                                        .commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Update failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", userEmail); // Menggunakan email yang diambil dari SharedPreferences
                params.put("password", newPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}