package com.alphatz.adek;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sql_lite.DatabaseContract;
import sql_lite.DatabaseHelper;

public class Regist extends AppCompatActivity {

    private EditText editTextEmail, editTextUsername, editTextPassword, editTextRePassword;
    private Button buttonRegister;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        // Initialize database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        editTextEmail = findViewById(R.id.input_emailRegist);
        editTextUsername = findViewById(R.id.name_regist);
        editTextPassword = findViewById(R.id.input_password);
        editTextRePassword = findViewById(R.id.input_repasword_regist);
        buttonRegister = findViewById(R.id.btn_regist);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                String rePassword = editTextRePassword.getText().toString();

                if (!email.isEmpty() && !username.isEmpty() && !password.isEmpty() && password.equals(rePassword)) {
                    register(email, username, password);
                } else {
                    Toast.makeText(Regist.this, "Please fill all fields and make sure passwords match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(String email, String username, String password) {
        // Check if email or username already exists
        if (isEmailOrUsernameExists(email, username)) {
            Toast.makeText(this, "Email or username already exists", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserEntry.COLUMN_EMAIL, email);
        values.put(DatabaseContract.UserEntry.COLUMN_USERNAME, username);
        values.put(DatabaseContract.UserEntry.COLUMN_PASSWORD, password);

        long newRowId = db.insert(DatabaseContract.UserEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isEmailOrUsernameExists(String email, String username) {
        String[] projection = {DatabaseContract.UserEntry._ID};
        String selection = DatabaseContract.UserEntry.COLUMN_EMAIL + " = ? OR " +
                DatabaseContract.UserEntry.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {email, username};

        Cursor cursor = db.query(
                DatabaseContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}