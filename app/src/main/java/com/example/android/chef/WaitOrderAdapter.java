package com.example.android.chef;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private Context context;
    private SharedPreferences sharedPreferences;
    public WaitOrderAdapter(List<Order> items, Context context) {
        mValues = items;
        this.context=context;
    }

    public WaitOrderAdapter(List<Order> items,SharedPreferences sharedPreferences ) {
        mValues = items;
      this.sharedPreferences=sharedPreferences;
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
        Glide.with(context).load(mValues.get(position).getItem_image()).placeholder(R.drawable.food).into(holder.image);
        holder.prepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int quantity = mValues.get(position).getRemaining();
                quantity--;
                mValues.get(position).setRemaining(quantity);
                String ordernumber=mValues.get(position).getOrdernumber();
                final DatabaseReference dbr= FirebaseDatabase.getInstance().getReference("orders/"+ordernumber);
                DatabaseReference dre=FirebaseDatabase.getInstance().getReference("chefs/"+FirebaseAuth.getInstance().getCurrentUser().getUid());

                dre.setValue(new Date().getTime()+(long)mValues.get(position).getItem_waiting_time()*60*1000);

                dbr.child("status").setValue("preparing");
                dbr.child("remaining").setValue(quantity);

                if(quantity==mValues.get(position).getQuantity()-1)
                {
                    FirebaseDatabase.getInstance().getReference("orders/"+mValues.get(position).getOrdernumber()+"/waiting_time").setValue(mValues.get(position).getQuantity()*mValues.get(position).getItem_waiting_time());
                }

                dbr.child("chefs").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
               // mValues.get(position).setStatus("preparing");
                //mValues.get(position).setRemaining(quantity);
                //mValues.get(position).getChefs().put(key,);
                //dbr.setValue(mValues.get(position));
                /*DatabaseReference lref=FirebaseDatabase.getInstance().getReference("orders/"+ordernumber+"/completed");

                lref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Toast.makeText(v.getContext(), "set as "+val, Toast.LENGTH_SHORT).show();
                        editor.commit();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                */





                if (quantity == 0) {
                    mValues.remove(position);
                    //notifyDataSetChanged();
                }
                notifyDataSetChanged();
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
        ImageView image;
        Button prepare;

        public ViewHolder(View view) {
            super(view);
            ordernumber=(TextView)view.findViewById(R.id.order_number);
            item=(TextView)view.findViewById(R.id.item_name);
            quantity=(TextView)view.findViewById(R.id.order_remaining);
            prepare=(Button) view.findViewById(R.id.order_prepare);
            image=(ImageView) view.findViewById(R.id.item_image);

        }

        @Override
        public String toString() {
            return null;
        }
    }
}
