package com.example.ebel_reece_cs360_project_two;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private Button btnLogin, btnCreateAccount;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        // Login Btn click event listener
        btnLogin.setOnClickListener(view -> {
            String uname = editUsername.getText().toString().trim();
            String pword = editPassword.getText().toString().trim();

            if (uname.isEmpty() || pword.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check user exists
            String storedPass = dbHelper.getUserPassword(uname);
            if (storedPass == null) {
                Toast.makeText(this, "User does not exist.", Toast.LENGTH_SHORT).show();
            } else if (storedPass.equals(pword)) {
                // Success → go to inventory page
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, InventoryActivity.class));
            } else {
                Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show();
            }
        });

        // Create account button click event
        btnCreateAccount.setOnClickListener(view -> {
            String uname = editUsername.getText().toString().trim();
            String pword = editPassword.getText().toString().trim();

            if (uname.isEmpty() || pword.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.userExists(uname)) {
                Toast.makeText(this, "Username already taken.", Toast.LENGTH_SHORT).show();
            } else {
                long result = dbHelper.insertUser(uname, pword);
                if (result == -1) {
                    Toast.makeText(this, "Error creating user.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "User created! Logging in...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, InventoryActivity.class));
                }
            }
        });
    }
}
