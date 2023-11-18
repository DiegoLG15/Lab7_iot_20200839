package com.example.lab7_iot_20200839;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lab7_iot_20200839.databinding.ActivityLoginBinding;
import com.example.lab7_iot_20200839.dtos.Usuario;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseFirestore db;

    private final static String TAG = "msg-test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) { //user logged-in
            if (currentUser.isEmailVerified()) {
                Log.d(TAG, "Firebase uid: " + currentUser.getUid());
                goToMainActivity();
            }
        }

        binding.loginBtn.setOnClickListener(view -> {

            binding.loginBtn.setEnabled(false);

            /*AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.login_auth)
                    .setEmailButtonId(R.id.btn_login_mail)
                    .build();*/



            Intent intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setTheme(R.style.Base_Theme_Lab7_iot_20200839)
                    .setIsSmartLockEnabled(false)
                    //.setAuthMethodPickerLayout(authMethodPickerLayout)
                    .setLogo(R.drawable.home)
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build()
                    ))
                    .build();

            signInLauncher.launch(intent);
        });

    }
    ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Log.d(TAG, "Firebase uid: " + user.getUid());
                        Log.d(TAG, "Display name: " + user.getDisplayName());
                        Log.d(TAG, "Email: " + user.getEmail());

                        user.reload().addOnCompleteListener(task -> {
                            if (user.isEmailVerified()) {
                                Usuario usuario = new Usuario();
                                goToMainActivity();
                            } else {
                                user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                    Toast.makeText(LoginActivity.this, "Se le ha enviado un correo para validar su cuenta", Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
                    } else {
                        Log.d(TAG, "user == null");
                    }
                } else {
                    Log.d(TAG, "Canceló el Log-in");
                }
                binding.loginBtn.setEnabled(true);
            }
    );
    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}