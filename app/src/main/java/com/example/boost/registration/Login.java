package com.example.boost.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.boost.main.MainInterface;
import com.example.boost.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Login extends AppCompatActivity {
    private EditText loginUsername, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> userCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.editTextTextEmailAddress);
        loginPassword = findViewById(R.id.editTextTextPassword);
        signupRedirectText = findViewById(R.id.button3);
        loginButton = findViewById(R.id.button4);

        sharedPreferences = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        userCredentials = getSavedCredentials();

        if (isUserLoggedIn()) {
            Intent intent = new Intent(Login.this, MainInterface.class);
            startActivity(intent);
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUsername() && validatePassword()) {
                    if (checkOfflineCredentials()) {
                        Toast.makeText(Login.this, "Logged in offline!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainInterface.class);
                        startActivity(intent);
                        finish();
                    } else {
                        checkUser();
                    }
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);

        return username != null && password != null;
    }

    public void storeUserCredentials(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    private ArrayList<String> getSavedCredentials() {
        Set<String> credentialsSet = sharedPreferences.getStringSet("credentials", new HashSet<>());
        return new ArrayList<>(credentialsSet);
    }

    public boolean checkOfflineCredentials() {
        String enteredUsername = loginUsername.getText().toString().trim();
        String enteredPassword = loginPassword.getText().toString().trim();

        for (String credentials : userCredentials) {
            String[] parts = credentials.split(":");
            if (parts.length == 2) {
                String storedUsername = parts[0];
                String storedPassword = parts[1];

                if (storedUsername.equals(enteredUsername) && storedPassword.equals(enteredPassword)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                        storeUserCredentials(userUsername, userPassword);

                        Intent intent = new Intent(Login.this, MainInterface.class);
                        startActivity(intent);
                        finish();
                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("LoginActivity", "Firebase error: " + error.getMessage());
                Toast.makeText(Login.this, "Error connecting to Firebase.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
