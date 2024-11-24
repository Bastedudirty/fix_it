package com.example.boost.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.boost.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SignUp extends AppCompatActivity {

    EditText signupEmail, signupUsername, signupPassword, confirmPass;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<String> userCredentials = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupEmail = findViewById(R.id.signupemail);
        signupUsername = findViewById(R.id.Username);
        signupPassword = findViewById(R.id.editTextTextPassword2);
        signupButton = findViewById(R.id.submit_area);
        loginRedirectText = findViewById(R.id.gotologin);
        confirmPass = findViewById(R.id.editTextTextPassword3);

        sharedPreferences = getSharedPreferences("UserCredentials", MODE_PRIVATE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signupButton.setOnClickListener(v -> {
            String email = signupEmail.getText().toString().trim();
            String username = signupUsername.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();
            String confirmPassword = confirmPass.getText().toString().trim();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUp.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            String credentials = username + ":" + password;
            userCredentials.add(credentials);
            saveCredentialsOffline(userCredentials);

            database = FirebaseDatabase.getInstance();
            reference = database.getReference("users");

            HelperClass helperClass = new HelperClass(username, email, password);

            reference.child(username).setValue(helperClass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this, Login.class));
                            finish();
                        } else {
                            Toast.makeText(SignUp.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });
    }

    private void saveCredentialsOffline(ArrayList<String> credentials) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> credentialsSet = new HashSet<>(credentials);
        editor.putStringSet("credentials", credentialsSet);
        editor.apply();
    }

    private ArrayList<String> getSavedCredentials() {
        Set<String> credentialsSet = sharedPreferences.getStringSet("credentials", new HashSet<>());
        return new ArrayList<>(credentialsSet);
    }
}
