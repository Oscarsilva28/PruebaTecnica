package com.example.pruebatecnica.utils.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.mindrot.jbcrypt.BCrypt;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DatabaseName = "Signup3.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "Signup3.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("CREATE TABLE users(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    email TEXT NOT NULL,\n" +
                "    password TEXT NOT NULL,\n" +
                "    salt_password TEXT NOT NULL,\n" +
                "    first_name TEXT NOT NULL,\n" +
                "    last_name TEXT NOT NULL,\n" +
                "    identification TEXT NOT NULL,\n" +
                "    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
                "    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP\n" +
                ")");
        MyDatabase.execSQL("CREATE TABLE asteroids(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    id_user INTEGER,\n" +
                "    neo_reference_id TEXT NOT NULL,\n" +
                "    link TEXT NOT NULL,\n" +
                "    name TEXT NOT NULL,\n" +
                "    nasa_jpl_url TEXT NOT NULL,\n" +
                "    absolute_magnitude_h INTEGER NOT NULL,\n" +
                "    estimated_diameter TEXT NOT NULL,\n" +
                "    is_potentially_hazardous_asteroid INTEGER NOT NULL DEFAULT 0,\n" +
                "    close_approach_data TEXT NOT NULL,\n" +
                "    is_sentry_object INTEGER NOT NULL DEFAULT 0,\n" +
                "    FOREIGN KEY(id_user) REFERENCES users(id)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int i, int i1) {
        MyDatabase.execSQL("DROP TABLE IF EXISTS users");
    }

    public Boolean insertData(String email, String password, String firstName, String lastName, String identification) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email", email);
        String saltPassword = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, saltPassword);
        contentValues.put("password", hashedPassword);
        contentValues.put("salt_password", saltPassword);
        contentValues.put("first_name", firstName);
        contentValues.put("last_name", lastName);
        contentValues.put("identification", identification);

        long inserted = MyDatabase.insert("users", null, contentValues);

        if (inserted == -1) {
            return false;
        }
        return true;
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    @SuppressLint("Range")
    public int checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();

        Cursor cursor = MyDatabase.rawQuery("Select password, salt_password, id from users where email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String logedInPassword = BCrypt.hashpw(password, cursor.getString(cursor.getColumnIndex("salt_password")));
                if (logedInPassword.equals(cursor.getString(cursor.getColumnIndex("password"))))
                    return (int) cursor.getInt(cursor.getColumnIndex("id"));
            };

            return -1;
        }
        return -1;
    }
}
