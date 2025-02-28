package com.example.ebel_reece_cs360_project_two;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity {

    private EditText editItemName, editItemDesc, editItemPrice, editItemQty;
    private Button btnSaveItem, btnCancelItem;
    DatabaseHelper dbHelper;

    long itemId = -1; // Default for new items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        dbHelper = new DatabaseHelper(this);

        editItemName = findViewById(R.id.editItemName);
        editItemDesc = findViewById(R.id.editItemDesc);
        editItemPrice = findViewById(R.id.editItemPrice);
        editItemQty = findViewById(R.id.editItemQty);
        btnSaveItem = findViewById(R.id.btnSaveItem);
        btnCancelItem = findViewById(R.id.btnCancelItem);

        // Check if editing existing item
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ITEM_ID")) {
            itemId = intent.getLongExtra("ITEM_ID", -1);
            editItemName.setText(intent.getStringExtra("ITEM_NAME"));
            editItemDesc.setText(intent.getStringExtra("ITEM_DESC"));
            double price = intent.getDoubleExtra("ITEM_PRICE", 0.0);
            editItemPrice.setText(String.valueOf(price));
            int quantity = intent.getIntExtra("ITEM_QTY", 0);
            editItemQty.setText(String.valueOf(quantity));
        }

        btnSaveItem.setOnClickListener(v -> {
            String name = editItemName.getText().toString().trim();
            String desc = editItemDesc.getText().toString().trim();
            double price = 0.0;
            int qty = 0;
            try {
                price = Double.parseDouble(editItemPrice.getText().toString().trim());
            } catch (NumberFormatException e) {
                // Default price remains at 0 if parsing failed
            }
            try {
                qty = Integer.parseInt(editItemQty.getText().toString().trim());
            } catch (NumberFormatException e) {}

            if (itemId == -1) {
                // Insert new item w/ price
                long result = dbHelper.insertItem(name, desc, qty, price);
                if (result == -1) {
                    Toast.makeText(this, "Error adding item.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Update existing item w/ price
                int result = dbHelper.updateItem(itemId, name, desc, qty, price);
                if (result > 0) {
                    Toast.makeText(this, "Item updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error updating item.", Toast.LENGTH_SHORT).show();
                }
            }
            finish(); // Go back to InventoryActivity
        });

        btnCancelItem.setOnClickListener(v -> finish());
    }
}
