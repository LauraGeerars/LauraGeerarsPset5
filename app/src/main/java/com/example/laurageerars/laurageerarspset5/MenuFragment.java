package com.example.laurageerars.laurageerarspset5;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {
    public ArrayList<String> menucategory = new ArrayList<String>();
    //public ArrayList<Integer> menuprice = new ArrayList<>();
    String CategoryMenu;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = this.getArguments();
        CategoryMenu = arguments.getString("category");
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://resto.mprog.nl/menu";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {

                                JSONObject newObject = (JSONObject) new JSONTokener(response).nextValue();
                                JSONArray menuArray = newObject.getJSONArray("items");
                                for (int i = 0; i < menuArray.length(); i++) {
                                    //listmenu.add(menuArray.getJSONObject(i));
                                    //mTextView.setText(menuArray.getJSONObject(i).getString("name"));
                                    if (Objects.equals(menuArray.getJSONObject(i).getString("category"), CategoryMenu)) {
                                        //addItem(menuArray.getJSONObject(i).getString("name"));
                                        //String temp = menuArray.getJSONObject(i).getString("name") + " - € " + menuArray.getJSONObject(i).getString("price");
                                        addItem(menuArray.getJSONObject(i).getString("name"));
                                        //menucategory.add(menuArray.getJSONObject(i).getString("name"));
                                        //menucategory.add(menuArray.getJSONObject(i).getString("price"));
                                        //addPrice(menuArray.getJSONObject(i).getString("name"), menuArray.getJSONObject(i).getString("price"));

                                    }
                                }

                                Adapter(menucategory);


                            } catch (JSONException e) {
                                e.printStackTrace();


                            }
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void addItem(String Item) {

        menucategory.add(Item);
    }
/*
    public void addPrice(String Item, String price) {

        SharedPreferences yourOrderPrefs = getContext().getSharedPreferences("PriceStore", getContext().MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = yourOrderPrefs.edit();
        prefsEditor.putString(Item, price);
        prefsEditor.commit();

    }*/

    public void Adapter(ArrayList<String> menucategory) {
        this.setListAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, menucategory));

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        RestoDatabase db = RestoDatabase.getInstance(getContext());
        String item = String.valueOf(l.getItemAtPosition(position));
        String text = item + " has been added to your order!";
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        //db.insert(String.valueOf(l.getItemAtPosition(position)), price);
        //db.insert(item, price);



        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_menu, container, false);
        }

    }

