package com.example.ebel_reece_cs360_project_two;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SMSActivity extends AppCompatActivity {

    private TextView textSMSDescription, textSMSStatus;
    private Button btnRequestSMSPermission, btnHome;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    textSMSStatus.setText("Status: Granted");
                } else {
                    textSMSStatus.setText("Status: Denied");
                    Toast.makeText(this, "SMS permission is required to send alerts.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        textSMSDescription = findViewById(R.id.textSMSDescription);
        textSMSStatus = findViewById(R.id.textSMSStatus);
        btnRequestSMSPermission = findViewById(R.id.btnRequestSMSPermission);
        btnHome = findViewById(R.id.btnHome);  // Initialize the Home button

        // Check perm status
        updateSMSPermissionStatus();

        // Request SMS perms if btn is clicked
        btnRequestSMSPermission.setOnClickListener(view -> requestSMSPermission());

        // Home button ---> InventoryActivity when clicked
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(SMSActivity.this, InventoryActivity.class);
            startActivity(intent);
        });
    }

    private void requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
        } else {
            updateSMSPermissionStatus();
        }
    }

    private void updateSMSPermissionStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            textSMSStatus.setText("Status: Granted");
        } else {
            textSMSStatus.setText("Status: Denied");
        }
    }

    private void maybeSendSmsAlert(String itemName) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = getSystemService(SmsManager.class);
                smsManager.sendTextMessage(
                        "5554", null,
                        "Item: [" + itemName + "] is out of stock",
                        null, null
                );
                Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
