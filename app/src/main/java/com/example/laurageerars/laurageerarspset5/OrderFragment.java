package com.example.laurageerars.laurageerarspset5;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends DialogFragment implements View.OnClickListener {
    private RestoDatabase db;
    private RestoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        RestoDatabase db = RestoDatabase.getInstance(getContext());
        Cursor cursor = db.selectAll();
        adapter = new RestoAdapter(getContext(), cursor);
        ListView ListView = view.findViewById(R.id.order_list);
        ListView.setAdapter(adapter);
        TextView Textview = view.findViewById(R.id.order_total);
        String total = totalPrice(cursor);
        Textview.setText(total);
        Button sendbutton = view.findViewById(R.id.sendOrder);
        Button cancelbutton = view.findViewById(R.id.cancelOrder);
        sendbutton.setOnClickListener(this);
        cancelbutton.setOnClickListener(this);
        
        return view;
    }

    @Override
    public void onClick(View v) {
        RestoDatabase db = RestoDatabase.getInstance(getContext());
        switch (v.getId()) {
            case R.id.sendOrder:
                db.clear();
                submitOrder();
                updateData();
            case R.id.cancelOrder:
                db.clear();
                updateData();
        }
    }

    private void updateData() {
        db = RestoDatabase.getInstance(getContext());
        Cursor cursor = db.selectAll();
        adapter.swapCursor(cursor);
        ListView ListView = getView().findViewById(R.id.order_list);
        TextView Textview = getView().findViewById(R.id.order_total);
        String total = totalPrice(cursor);
        ListView.setAdapter(adapter);
        Textview.setText(total);

    }

    public void submitOrder(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://resto.mprog.nl/order";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject newObject = (JSONObject) new JSONObject(response);
                            Context context = getContext();
                            CharSequence text = "The preparation time of your order is: " + newObject.getString("preparation_time");
                            int toastduration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, toastduration);
                            toast.show();
                        }

                        catch (JSONException e) {
                            e.printStackTrace();


                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public String totalPrice(Cursor cursor) {
        int totalcount = 0;

        while(cursor.moveToNext()) {

            int totalsum = cursor.getInt(cursor.getColumnIndex("price")) * cursor.getInt(cursor.getColumnIndex("amount"));
            totalcount += totalsum;
        }

        String totaloutput = "Total price of order is €" + totalcount;
        return totaloutput;


    }

    public void clear() {
        // Clear the database and close the dialog window
        db.clear();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

}
