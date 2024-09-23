package com.example.jmpendproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "JMPENDPro.db";
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.d(TAG, "Inisiasi DatabaseHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Membuat Database...");
        db.execSQL("CREATE TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL, password TEXT NOT NULL, level TEXT NOT NULL)");
        db.execSQL("CREATE TABLE barang(id_barang INTEGER PRIMARY KEY AUTOINCREMENT, nama_barang TEXT NOT NULL, stok INTEGER NOT NULL, harga INTEGER NOT NULL)");
        db.execSQL("INSERT INTO barang(id_barang, nama_barang, stok, harga) VALUES(1, 'Pensil', 10, 1000)");
        db.execSQL("INSERT INTO user(id, email, password, level) VALUES(1, 'admin@gmail.com', 'admin1234', 'admin')");
        db.execSQL("CREATE TABLE session (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT NOT NULL)");
        db.execSQL("INSERT INTO session (id, login) VALUES (1, 'kosong')");
        Log.d(TAG, "Database table berhasil dibuat dan diinisiasikan...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Database diupgrade...");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    public Boolean checkLogin(String email, String password){
        Log.d(TAG, "Checking login...");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE email = ? AND password = ?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        Log.d(TAG, "checkLogin: " + result);
        return result;
    }

    public Boolean upgradeSession(String value, int id){
        SQLiteDatabase FPdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("login", value);
        long update = FPdb.update("session", values, "id=" + id, null);
        Log.d(TAG, "upgradeSession: " + (update != -1));
        return update != -1;
    }

    public Cursor getAllBarang() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id_barang AS _id, nama_barang, stok, harga FROM barang", null);
    }


    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM user WHERE email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        } else {
            return -1;
        }
    }

    public int getBarangId(String namaBarang) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_barang FROM barang WHERE nama_barang = ?", new String[]{namaBarang});
        if (cursor != null && cursor.moveToFirst()) {
            int barangId = cursor.getInt(0);
            cursor.close();
            return barangId;
        } else {
            return -1; // Mengembalikan -1 jika tidak ditemukan
        }
    }

    public boolean insertBarang(String namaBarang, int stok, int harga) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nama_barang", namaBarang);
        contentValues.put("stok", stok);
        contentValues.put("harga", harga);

        long result = db.insert("barang", null, contentValues);
        return result != -1;
    }

}