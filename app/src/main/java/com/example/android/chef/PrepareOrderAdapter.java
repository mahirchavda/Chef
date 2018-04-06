package com.example.android.chef;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;

public class PrepareOrderAdapter extends RecyclerView.Adapter<PrepareOrderAdapter.ViewHolder> {

      List<Order> mValues;
      SharedPreferences sharedPreferences;
    long val=0;
    public PrepareOrderAdapter(List<Order> items) {
        mValues = items;

    }

    public PrepareOrderAdapter(List<Order> items, SharedPreferences sharedPreferences) {
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
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.ordernumber.setText(mValues.get(position).getOrdernumber().toString());
        holder.item.setText(mValues.get(position).getItem_name().toString());
        holder.quantity.setText(Integer.toString(mValues.get(position).getRemaining()));
        holder.prepare.setText("complete");
        holder.prepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int quantity = mValues.get(position).getRemaining();

                final String ordernumber = mValues.get(position).getOrdernumber();

                Order o = mValues.get(position);
                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("orders/" + ordernumber);
                if (o.getChefs().size() == 1 && quantity == 0) {
                    FirebaseDatabase.getInstance().getReference("orders/" + ordernumber).child("status").setValue("completed");
                    //FirebaseDatabase.getInstance().getReference("orders/"+ordernumber).child("completed").setValue(o.getCompleted()+1);
                    //SharedPreferences.Editor editor=sharedPreferences.edit();
                    //editor.putInt("prev",0);
                }
                    FirebaseDatabase.getInstance().getReference("chefs/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new Date().getTime());

                for (String ssh : o.getChefs().keySet()){
                    if (o.getChefs().get(ssh).compareTo(FirebaseAuth.getInstance().getCurrentUser().getUid()) == 0) {
                        FirebaseDatabase.getInstance().getReference("orders/" + ordernumber + "/chefs/" + ssh).removeValue();
                        break;
                    }
                }
                dbr.child("completed").setValue(o.getCompleted()+1);

                mValues.remove(position);
                notifyDataSetChanged();
                
                final DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("chefs/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                
                
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        val=(long)dataSnapshot.getValue()-new Date().getTime();
                        Toast.makeText(v.getContext(), "settled", Toast.LENGTH_SHORT).show();
                        dbref.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                
                
                
                
                
                

                
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
