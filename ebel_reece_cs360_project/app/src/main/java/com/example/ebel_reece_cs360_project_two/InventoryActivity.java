package com.example.ebel_reece_cs360_project_two;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.ebel_reece_cs360_project_two.R;

public class InventoryActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    private TableLayout tableLayoutData;
    private Button btnAddItem, btnGoToSMS, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Init DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        tableLayoutData = findViewById(R.id.tableLayoutData);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnGoToSMS = findViewById(R.id.btnGoToSMS);
        btnLogout = findViewById(R.id.btnLogout);

        // "Add Item" button ---> opens AddItemActivity
        btnAddItem.setOnClickListener(view -> {
            Intent intent = new Intent(InventoryActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        // SMS button ---> opens SMSActivity
        btnGoToSMS.setOnClickListener(view -> {
            Intent intent = new Intent(InventoryActivity.this, SMSActivity.class);
            startActivity(intent);
        });

        // Logout button ---> return to LoginActivity
        btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(InventoryActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Load table onResume
    @Override
    protected void onResume() {
        super.onResume();
        loadItemsIntoTable();
    }

    private void loadItemsIntoTable() {
        tableLayoutData.removeAllViews(); // Clear old rows

        Cursor cursor = dbHelper.getAllItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve data from cols
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_DESC));
                int qty = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_QTY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_PRICE));

                // Add new row
                TableRow row = new TableRow(this);

                // Item Name
                TextView nameView = new TextView(this);
                nameView.setText(name);
                row.addView(nameView);

                // Quantity
                TextView qtyView = new TextView(this);
                qtyView.setText(String.valueOf(qty));
                row.addView(qtyView);

                // Price
                TextView priceView = new TextView(this);
                priceView.setText(String.format("$%.2f", price));
                row.addView(priceView);

                // Delete Btn
                Button deleteBtn = new Button(this);
                deleteBtn.setText("Delete");
                deleteBtn.setOnClickListener(v -> {
                    dbHelper.deleteItem(id);
                    loadItemsIntoTable(); // Refreshes the table after deletion
                });
                row.addView(deleteBtn);

                // Update Btn
                Button updateBtn = new Button(this);
                updateBtn.setText("Update");
                updateBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(this, AddItemActivity.class);
                    intent.putExtra("ITEM_ID", id);
                    intent.putExtra("ITEM_NAME", name);
                    intent.putExtra("ITEM_DESC", desc);
                    intent.putExtra("ITEM_QTY", qty);
                    intent.putExtra("ITEM_PRICE", price);
                    startActivity(intent);
                });
                row.addView(updateBtn);

                tableLayoutData.addView(row);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
