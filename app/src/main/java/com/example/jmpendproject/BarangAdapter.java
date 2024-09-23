package com.example.jmpendproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class BarangAdapter extends CursorAdapter {

    public BarangAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.item_barang, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView namaBarang = view.findViewById(R.id.nama_barang);
        TextView stok = view.findViewById(R.id.stok_barang);
        TextView harga = view.findViewById(R.id.harga_barang);

        String nama = cursor.getString(cursor.getColumnIndexOrThrow("nama_barang"));
        int stokValue = cursor.getInt(cursor.getColumnIndexOrThrow("stok"));
        int hargaValue = cursor.getInt(cursor.getColumnIndexOrThrow("harga"));

        namaBarang.setText(nama);
        stok.setText(String.valueOf(stokValue));
        harga.setText(String.valueOf(hargaValue));
    }


}