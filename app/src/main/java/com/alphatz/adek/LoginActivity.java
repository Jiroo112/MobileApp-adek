package com.alphatz.adek;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import sql_lite.DatabaseContract;
import sql_lite.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignUp;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Initialize UI elements
        editTextUsername = findViewById(R.id.username_text);
        editTextPassword = findViewById(R.id.input_password);
        buttonLogin = findViewById(R.id.btn_login);
        textViewSignUp = findViewById(R.id.register);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    login(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
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

        // Add dummy user if it doesn't exist
        if (!isUserExists("testuser")) {
            addDummyUser("testuser", "password123");
        }
    }

    private void login(String username, String password) {
        String[] projection = {
                DatabaseContract.UserEntry._ID,
                DatabaseContract.UserEntry.COLUMN_USERNAME
        };

        String selection = DatabaseContract.UserEntry.COLUMN_USERNAME + " = ? AND " +
                DatabaseContract.UserEntry.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };

        Cursor cursor = db.query(
                DatabaseContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            // Login successful
            String loggedInUsername = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_USERNAME));

            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
            intent.putExtra("username", loggedInUsername);
            startActivity(intent);
            finish();
        } else {
            // Login failed
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    private boolean isUserExists(String username) {
        String[] projection = {DatabaseContract.UserEntry._ID};
        String selection = DatabaseContract.UserEntry.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

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

    private void addDummyUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserEntry.COLUMN_USERNAME, username);
        values.put(DatabaseContract.UserEntry.COLUMN_PASSWORD, password);

        db.insert(DatabaseContract.UserEntry.TABLE_NAME, null, values);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}