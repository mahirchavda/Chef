package com.example.android.chef;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class PrepareOrderFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private long prev=-1;
    private  RecyclerView rview;
    private  PrepareOrderAdapter adapter;
    private ArrayList<Order> orders;
    private SharedPreferences sharedPreferences;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PrepareOrderFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PrepareOrderFragment newInstance(int columnCount) {
        PrepareOrderFragment fragment = new PrepareOrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences("myprefs",Context.MODE_PRIVATE);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);


         rview = (RecyclerView) view;
        adapter=new PrepareOrderAdapter(new ArrayList<Order>());

        rview.setAdapter(adapter);
        Query dbref= FirebaseDatabase.getInstance().getReference("orders");
        //.child("status").startAt("waiting").endAt("waiting").startAt("preparing").endAt("preparing");



        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order o=dataSnapshot.getValue(Order.class);
                //Toast.makeText(getContext(), o.getItem_name()+" "+o.getQuantity()+" in other fragment", Toast.LENGTH_SHORT).show();
                if(o.getStatus().compareTo("preparing")==0 && o.getChefs().containsValue(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    adapter.mValues.add(dataSnapshot.getValue(Order.class));
                    adapter.notifyDataSetChanged();
                    rview.scheduleLayoutAnimation();
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putInt("prev",(int)o.getCompleted());
                    editor.commit();
                   // Toast.makeText(getActivity(), "added", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Order o=dataSnapshot.getValue(Order.class);
                //prev=sharedPreferences.getInt("prev",-1);
                //Toast.makeText(getActivity(), prev+" "+o.getCompleted(), Toast.LENGTH_SHORT).show();
                if(o.getStatus().compareTo("preparing")==0 && o.getChefs().containsValue(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    //Toast.makeText(getActivity(), "changed ", Toast.LENGTH_SHORT).show();
                    adapter.mValues.add(dataSnapshot.getValue(Order.class));
                    adapter.notifyDataSetChanged();
                    rview.scheduleLayoutAnimation();
                }


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
