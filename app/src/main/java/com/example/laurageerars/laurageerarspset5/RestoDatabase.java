package com.example.laurageerars.laurageerarspset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Objects;

/**
 * Created by laurageerars on 28-11-17.
 */

public class RestoDatabase extends SQLiteOpenHelper {
    private static RestoDatabase instance;
    private static String DB = "orders";
    private static int version = 1;

    private RestoDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //fix instances
    public static RestoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new RestoDatabase(context.getApplicationContext(), DB, null, version);
        }

        return instance;

    }

    //create table orders with id, name, price and amount of dishes
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table orders (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, price INTEGER, amount INTEGER);");

    }

    //upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "orders");
        onCreate(db);

    }

    //select all from created table
    public Cursor selectAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders", null);
        return cursor;
    }

    //insert name and price in database
    public void insert(String name, int price) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders", null);
        ContentValues values = new ContentValues();
        Boolean New = true;
        //if dish is already in order, increase amount
        while (cursor.moveToNext()) {
            if (Objects.equals(cursor.getString(cursor.getColumnIndex("name")), name)) {
                New = false;
                Integer amountnumber = cursor.getInt(cursor.getColumnIndex("amount"));
                int newamountnumber = 1 + amountnumber;
                values.put("amount", newamountnumber);
                db.update("orders", values, "name = ?", new String[]{name});
            }
        }

        //if dish is not in order already, add new dish to order
        if (New) {
            addItem(name, price);
        }

    }

    //add dish to order with name, price and amount as values
    public void addItem(String name, int price) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("amount", 1);
        db.insert("orders",null, values);
    }

    //function for removing all items from the database
    public void clear(){
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db,1,2);
    }

}
