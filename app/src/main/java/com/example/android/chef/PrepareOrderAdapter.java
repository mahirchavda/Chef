package com.example.android.chef;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.chef.Entities.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Set;

public class PrepareOrderAdapter extends RecyclerView.Adapter<PrepareOrderAdapter.ViewHolder> {

      List<Order> mValues;

    public PrepareOrderAdapter(List<Order> items) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.ordernumber.setText(mValues.get(position).getOrdernumber().toString());
        holder.item.setText(mValues.get(position).getItem_name().toString());
        holder.quantity.setText(Integer.toString(mValues.get(position).getRemaining()));
        holder.prepare.setText("complete");
        holder.prepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int quantity = mValues.get(position).getRemaining();

                final String ordernumber=mValues.get(position).getOrdernumber();
                DatabaseReference dbr= FirebaseDatabase.getInstance().getReference("orders");
                Order origin=new Order();
                dbr.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if(dataSnapshot.getKey().compareTo(ordernumber)==0) {
                            Order o = dataSnapshot.getValue(Order.class);
                            if (o.getChefs().size() == 1 && quantity == 0)
                                FirebaseDatabase.getInstance().getReference("orders/" + ordernumber).child("status").setValue("completed");


                            for (String ssh : o.getChefs().keySet()) {
                                if (o.getChefs().get(ssh).compareTo(FirebaseAuth.getInstance().getCurrentUser().getUid()) == 0)
                                    FirebaseDatabase.getInstance().getReference("orders/" + ordernumber + "/chefs/" + ssh).removeValue();
                            }
                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

              /*  DatabaseReference apr=FirebaseDatabase.getInstance().getReference("orders/"+ordernumber)





                DatabaseReference qr= FirebaseDatabase.getInstance().getReference("orders/"+ordernumber).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        for(DataSnapshot a:dataSnapshot.getChildren())
                            if(a.getValue().toString()==FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                        FirebaseDatabase.getInstance().getReference("orders/"+ordernumber+"/chefs/"+dataSnapshot.getKey()).removeValue();

                        if(dataSnapshot.getChildrenCount()==1 && quantity==0)
                        {
                            FirebaseDatabase.getInstance().getReference("orders").child("status").setValue("completed");

                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
                })
                */

                mValues.remove(position);
                notifyDataSetChanged();
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
