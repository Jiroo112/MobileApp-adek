package sql_lite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alphatz.adek.R;

public class ViewUsersActivity extends AppCompatActivity {

    private TextView textViewUsers;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_views_user);

        textViewUsers = findViewById(R.id.textViewUsers);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        displayUsers();
    }

    private void displayUsers() {
        String[] projection = {
            DatabaseContract.UserEntry._ID,
            DatabaseContract.UserEntry.COLUMN_EMAIL,
            DatabaseContract.UserEntry.COLUMN_USERNAME
        };

        Cursor cursor = db.query(
            DatabaseContract.UserEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        );

        StringBuilder usersInfo = new StringBuilder();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry._ID));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_EMAIL));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_USERNAME));

            usersInfo.append("ID: ").append(itemId).append("\n");
            usersInfo.append("Email: ").append(email).append("\n");
            usersInfo.append("Username: ").append(username).append("\n\n");
        }
        cursor.close();

        textViewUsers.setText(usersInfo.toString());
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}