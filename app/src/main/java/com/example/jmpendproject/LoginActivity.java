package com.example.jmpendproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jmpendproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    public static final String SHARED_PREF_NAME = "myPref";
    private ActivityLoginBinding binding;
    private DatabaseHelper db;
    AlertDialog.Builder dialog;
    LayoutInflater inflaters;
    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(this);
        db.getWritableDatabase();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        binding.btnLoginUser.setOnClickListener(v -> {
            String getEmail = binding.etUserEmail.getText().toString();
            String getPassword = binding.etUserPassword.getText().toString();

            // Validasi email harus diisi
            if (getEmail.isEmpty()) {
                binding.etUserEmail.setError("Email harus diisi");
                return; // Menghentikan proses jika email kosong
            }

            // Validasi email harus valid (memiliki karakter '@')
            if (!isValidEmail(getEmail)) {
                binding.etUserEmail.setError("Email tidak valid");
                return; // Menghentikan proses jika email tidak valid
            }

            if (getPassword.isEmpty()) {
                binding.etUserPassword.setError("Password harus diisi");
                return; // Menghentikan proses jika password kosong
            }

            Boolean masuk = db.checkLogin(getEmail, getPassword);
            if (masuk) {
                Boolean updateSession = db.upgradeSession("ada", 1);
                int userId = db.getUserId(getEmail);
                if (updateSession) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean("masuk", true);
                    editor.putInt("userId", userId); // Simpan ID pengguna
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Berhasil Masuk", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Gagal Masuk", Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}