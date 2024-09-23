package com.example.jmpendproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReadActivity extends AppCompatActivity {

    protected Cursor cursor;
    DatabaseHelper dbHelper;
    Button btnBack;
    TextView txt_id_read, txt_nama_read, txt_stok_read, txt_harga_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_barang);
        dbHelper = new DatabaseHelper(this);
        txt_id_read = findViewById(R.id.txt_read_id);
        txt_nama_read = findViewById(R.id.txt_read_barang);
        txt_stok_read = findViewById(R.id.txt_read_stok);
        txt_harga_read = findViewById(R.id.txt_read_harga);
        btnBack = findViewById(R.id.button1);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String barangId = getIntent().getStringExtra("barang_id");

        if (barangId != null) {
            String query = "SELECT * FROM barang WHERE id_barang = ?";
            cursor = db.rawQuery(query, new String[]{barangId});

            if (cursor.moveToFirst()) {
                txt_id_read.setText(cursor.getString(cursor.getColumnIndex("id_barang")));
                txt_nama_read.setText(cursor.getString(cursor.getColumnIndex("nama_barang")));
                txt_stok_read.setText(cursor.getString(cursor.getColumnIndex("stok")));
                txt_harga_read.setText(cursor.getString(cursor.getColumnIndex("harga")));
            } else {
                txt_id_read.setText("Data tidak ditemukan");
            }
            cursor.close(); // Jangan lupa untuk menutup cursor
        } else {
            txt_id_read.setText("ID barang tidak tersedia");
        }

        btnBack.setOnClickListener(v -> finish());
    }
}