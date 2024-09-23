package com.example.jmpendproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BarangActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listView;
    private FloatingActionButton fab;
    private BarangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layoutbarang);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.list_view);

        // Initialize adapter with an empty cursor
        adapter = new BarangAdapter(this, null, 0);
        listView.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(BarangActivity.this, AddActivity.class);
            startActivity(i);
        });

        // Load data initially
        loadData();

        // Set item click listener
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = (Cursor) adapter.getItem(position);
            String namaBarang = cursor.getString(cursor.getColumnIndexOrThrow("nama_barang"));
            final long barangId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));

            final CharSequence[] dialogItems = {"Read", "Update", "Delete"};
            AlertDialog.Builder builder = new AlertDialog.Builder(BarangActivity.this);
            builder.setTitle("Choose an action");
            builder.setItems(dialogItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0: // Read
                            Intent readIntent = new Intent(BarangActivity.this, ReadActivity.class);
                            readIntent.putExtra("barang_id", String.valueOf(barangId));
                            startActivity(readIntent);
                            break;
                        case 1: // Update
                            Intent updateIntent = new Intent(BarangActivity.this, UpdateActivity.class);
                            updateIntent.putExtra("barang_id", String.valueOf(barangId));
                            startActivity(updateIntent);
                            break;
                        case 2: // Delete
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            int barangId = dbHelper.getBarangId(namaBarang);

                            db.execSQL("DELETE FROM barang WHERE id_barang = " + barangId);

                            loadData();
                            break;
                    }
                }
            });
            builder.create().show();
        });
    }

    private void loadData() {
        Cursor cursor = dbHelper.getAllBarang();
        adapter.changeCursor(cursor); // Update adapter with new cursor
    }
}
