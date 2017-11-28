package com.example.laurageerars.laurageerarspset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by laurageerars on 28-11-17.
 */

public class RestoDatabase extends SQLiteOpenHelper {
    private static RestoDatabase instance;
    private static String DB = "restaurant";
    private static int version = 1;

    private RestoDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static RestoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new RestoDatabase(context.getApplicationContext(), DB, null, version);
        }

        return instance;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table orders (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, price INTEGER, amount INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "orders");
        onCreate(db);

    }

    public Cursor selectAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders", null);
        return cursor;
    }

    public void insert(String name, int price, int amount){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE name = '" + name.toString() + "'", null);
        if (cursor.getCount() != 0) {
            update(cursor.getColumnIndex("_id"),cursor.getColumnIndex("amount") + amount);
        }

        else {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("price", price);
            values.put("amount", amount);
            db.insert("orders", null, values);
        }
    }

    public void update(long id, int amount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", amount);
        db.update("orders",values,"_id=" + id, null);

    }

    public void delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("orders","_id = " + id,null);

    }

    public void clear(){
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db,1,2);
    }
}