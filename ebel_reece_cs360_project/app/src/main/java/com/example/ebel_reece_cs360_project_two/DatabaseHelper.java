package com.example.ebel_reece_cs360_project_two;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database constants-----------------------------------------
    private static final String DATABASE_NAME = "myDatabase.db";
    // Bump version to trigger onUpgrade to drop old tables-------
    private static final int DATABASE_VERSION = 2;

    // Table names -----------------------------------------------
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ITEMS = "items";

    // Users table columns ---------------------------------------
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Items table columns----------------------------------------
    public static final String COLUMN_ITEM_ID = "_id";
    public static final String COLUMN_ITEM_NAME = "itemName";
    public static final String COLUMN_ITEM_DESC = "itemDescription";
    public static final String COLUMN_ITEM_QTY = "quantity";
    public static final String COLUMN_ITEM_PRICE = "price";

    // SQL --> create Users table
    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT" +
                    ");";

    // SQL --> create Items table with the price column
    private static final String CREATE_ITEMS_TABLE =
            "CREATE TABLE " + TABLE_ITEMS + " (" +
                    COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ITEM_NAME + " TEXT, " +
                    COLUMN_ITEM_DESC + " TEXT, " +
                    COLUMN_ITEM_QTY + " INTEGER, " +
                    COLUMN_ITEM_PRICE + " REAL" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is FIRST created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // Called when there's an update to the DB version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // WARNING: This will drop all data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // Insert new user when registering
    public long insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    // Return password for username
    public String getUserPassword(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_PASSWORD };
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { username };
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            cursor.close();
        }
        db.close();
        return password;
    }

    // Checks user exists
    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_USER_ID };
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { username };
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean exists = false;
        if (cursor != null) {
            exists = cursor.getCount() > 0;
            cursor.close();
        }
        db.close();
        return exists;
    }

    // Insert new item with pricing
    public long insertItem(String name, String desc, int qty, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, name);
        values.put(COLUMN_ITEM_DESC, desc);
        values.put(COLUMN_ITEM_QTY, qty);
        values.put(COLUMN_ITEM_PRICE, price);
        long result = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return result;
    }

    // Return all items from the DB
    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ITEMS, null, null, null, null, null, null);
    }

    // Update existing item w/ pricing
    public int updateItem(long id, String name, String desc, int qty, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, name);
        values.put(COLUMN_ITEM_DESC, desc);
        values.put(COLUMN_ITEM_QTY, qty);
        values.put(COLUMN_ITEM_PRICE, price);
        String whereClause = COLUMN_ITEM_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };
        int rowsAffected = db.update(TABLE_ITEMS, values, whereClause, whereArgs);
        db.close();
        return rowsAffected;
    }

    // Delete item from the DB
    public int deleteItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ITEM_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };
        int rowsDeleted = db.delete(TABLE_ITEMS, whereClause, whereArgs);
        db.close();
        return rowsDeleted;
    }
}
