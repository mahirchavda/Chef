package com.example.android.chef;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.chef.Entities.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class WaitOrderFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private  RecyclerView rview;
    private  WaitOrderAdapter adapter;
    private ArrayList<Order> orders;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WaitOrderFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WaitOrderFragment newInstance(int columnCount) {
        WaitOrderFragment fragment = new WaitOrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
           rview = (RecyclerView) view;
            orders=new ArrayList<Order>();
            adapter=new WaitOrderAdapter(orders,getActivity().getSharedPreferences("myprefs",Context.MODE_PRIVATE));
            rview.setAdapter(adapter);

        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("orders");
                //.child("status").startAt("waiting").endAt("waiting").startAt("preparing").endAt("preparing");



                dbref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Order o=dataSnapshot.getValue(Order.class);
                        //oast.makeText(getContext(), o.getItem_name()+" "+o.getQuantity(), Toast.LENGTH_SHORT).show();
                        if(o.getStatus().compareTo("waiting")==0 || o.getStatus().compareTo("preparing")==0 && o.getRemaining()!=0) {
                            adapter.getmValues().add(dataSnapshot.getValue(Order.class));
                            adapter.notifyDataSetChanged();
                            rview.scheduleLayoutAnimation();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Order o=dataSnapshot.getValue(Order.class);
                        if(o.getStatus().compareTo("waiting")==0 || o.getStatus().compareTo("preparing")==0 && o.getRemaining()!=0) {
                            adapter.getmValues().remove(dataSnapshot.getValue(Order.class));
                            adapter.notifyDataSetChanged();
                            rview.scheduleLayoutAnimation();
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


           // recyclerView.setAdapter(new WaitOrderAdapter(DummyContent.ITEMS, mListener));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


}
