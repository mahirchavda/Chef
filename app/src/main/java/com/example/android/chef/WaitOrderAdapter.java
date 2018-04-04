package com.example.android.chef;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.chef.Entities.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.*;

public class WaitOrderAdapter extends RecyclerView.Adapter<WaitOrderAdapter.ViewHolder> {

    public List<Order> getmValues() {
        return mValues;
    }
    private Order o;
    private  List<Order> mValues;

    public WaitOrderAdapter(List<Order> items ) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.ordernumber.setText(mValues.get(position).getOrdernumber().toString());
        holder.item.setText(mValues.get(position).getItem_name().toString());
        holder.quantity.setText(Integer.toString(mValues.get(position).getRemaining()));
        holder.prepare.setText("prepare");
        holder.prepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = mValues.get(position).getRemaining();
                quantity--;
                mValues.get(position).setRemaining(quantity);
                String ordernumber=mValues.get(position).getOrdernumber();
                final DatabaseReference dbr= FirebaseDatabase.getInstance().getReference("orders/"+ordernumber);
                DatabaseReference dre=FirebaseDatabase.getInstance().getReference("chefs/"+FirebaseAuth.getInstance().getCurrentUser().getUid());

                dre.setValue(new Date().getTime()+(long)mValues.get(position).getItem_waiting_time()*60*1000);

                dbr.child("status").setValue("preparing");
                dbr.child("remaining").setValue(quantity);
                dbr.child("chefs").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                if (quantity == 0) {
                    mValues.remove(position);
                    notifyDataSetChanged();
                }

                holder.quantity.setText(Integer.toString(quantity));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ordernumber,item,quantity;
        Button prepare;

        public ViewHolder(View view) {
            super(view);
            ordernumber=(TextView)view.findViewById(R.id.order_number);
            item=(TextView)view.findViewById(R.id.item_name);
            quantity=(TextView)view.findViewById(R.id.order_remaining);
            prepare=(Button) view.findViewById(R.id.order_prepare);

        }

        @Override
        public String toString() {
            return null;
        }
    }
}
