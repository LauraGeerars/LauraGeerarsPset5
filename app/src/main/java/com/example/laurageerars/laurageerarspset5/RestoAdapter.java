package com.example.laurageerars.laurageerarspset5;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by laurageerars on 28-11-17.
 */

public class RestoAdapter extends CursorAdapter {
    public RestoAdapter(Context context, Cursor cursor) {
        super(context, cursor, R.layout.row_layout);

    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView item = view.findViewById(R.id.OrderItem);
        item.setText(cursor.getString(cursor.getColumnIndex("name")));

    }
}