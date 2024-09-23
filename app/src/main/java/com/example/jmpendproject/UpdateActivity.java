package com.example.jmpendproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    Button btnSubUp, btnCnlUp;
    EditText txt_barang_update, txt_stok_update, txt_harga_update;
    String barangId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_barang);

        dbHelper = new DatabaseHelper(this);
        txt_barang_update = findViewById(R.id.txt_barang_update);
        txt_stok_update = findViewById(R.id.txt_stok_update);
        txt_harga_update = findViewById(R.id.txt_harga_update);
        btnSubUp = findViewById(R.id.btn_submit_update);
        btnCnlUp = findViewById(R.id.btn_cancel_update);

        // Ambil ID barang dari intent
        barangId = getIntent().getStringExtra("barang_id");

        if (barangId == null) {
            Toast.makeText(this, "ID barang tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish(); // Selesaikan activity jika barangId null
            return;
        }

        // Tampilkan data barang yang ada
        loadData();

        // Set listener untuk tombol update
        btnSubUp.setOnClickListener(v -> updateBarang());

        // Set listener untuk tombol cancel
        btnCnlUp.setOnClickListener(v -> finish());
    }

    private void loadData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM barang WHERE id_barang = ?";
        Cursor cursor = db.rawQuery(query, new String[]{barangId});

        if (cursor.moveToFirst()) {
            txt_barang_update.setText(cursor.getString(cursor.getColumnIndex("nama_barang")));
            txt_stok_update.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("stok"))));
            txt_harga_update.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("harga"))));
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    private void updateBarang() {
        String namaBarang = txt_barang_update.getText().toString();
        int stok;
        int harga;

        try {
            stok = Integer.parseInt(txt_stok_update.getText().toString());
            harga = Integer.parseInt(txt_harga_update.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Stok dan harga harus berupa angka", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama_barang", namaBarang);
        values.put("stok", stok);
        values.put("harga", harga);

        int rowsAffected = db.update("barang", values, "id_barang = ?", new String[]{barangId});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
        }
    }
}
