package com.example.laurageerars.laurageerarspset5;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends DialogFragment {
    private RestoDatabase db;
    private RestoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        RestoDatabase db = RestoDatabase.getInstance(getContext());
        Cursor cursor = db.selectAll();
        adapter = new RestoAdapter(getActivity(), cursor);
        ListView ListView = view.findViewById(R.id.order_list);
        ListView.setAdapter(adapter);





        return view;
    }

    private void updateData() {
        db = RestoDatabase.getInstance(getContext());
        Cursor cursor = db.selectAll();
        adapter.swapCursor(cursor);
        ListView ListView = getView().findViewById(R.id.order_list);
        ListView.setAdapter(adapter);

    }

}
