package com.example.jmpendproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText txt_barang, txt_stok, txt_harga;
    private Button btn_submit_add, btn_cancel_add;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_barang);

        db = new DatabaseHelper(this);

        txt_barang = findViewById(R.id.txt_barang);
        txt_stok = findViewById(R.id.txt_stok);
        txt_harga = findViewById(R.id.txt_harga);
        btn_submit_add = findViewById(R.id.btn_submit_add);
        btn_cancel_add = findViewById(R.id.btn_cancel_add);

        btn_submit_add.setOnClickListener(v -> {
            String txtBarang = txt_barang.getText().toString();
            String txtStok = txt_stok.getText().toString();
            String txtHarga = txt_harga.getText().toString();

            if(txtBarang.isEmpty() || txtStok.isEmpty() || txtHarga.isEmpty()){
                Toast.makeText(AddActivity.this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            int stok = Integer.parseInt(txtStok);
            int harga = Integer.parseInt(txtHarga);

            boolean insert = db.insertBarang(txtBarang, stok, harga);

            if(!insert){
                Toast.makeText(AddActivity.this, "Gagal menambahkan barang", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AddActivity.this, "Berhasil menambahkan barang", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
