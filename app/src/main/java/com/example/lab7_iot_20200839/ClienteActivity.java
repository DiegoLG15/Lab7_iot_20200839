package com.example.lab7_iot_20200839;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lab7_iot_20200839.databinding.ActivityClienteBinding;
import com.example.lab7_iot_20200839.databinding.ActivityGestorBinding;
import com.example.lab7_iot_20200839.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ClienteActivity extends AppCompatActivity {

    ActivityClienteBinding binding;

    private final static String TAG = "msg-test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    }
}