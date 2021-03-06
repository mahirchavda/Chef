package com.example.android.chef;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.opengl.Visibility;
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
      Context context;
    long val=0;
    public PrepareOrderAdapter(List<Order> items,Context context) {
        mValues = items;
        this.context=context;

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
        holder.quantity.setVisibility(View.GONE);
        Glide.with(context).load(mValues.get(position).getItem_image()).placeholder(R.drawable.food).into(holder.image);
        holder.prepare.setText("complete");
        holder.prepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int quantity = mValues.get(position).getRemaining();

                final String ordernumber = mValues.get(position).getOrdernumber();

                final Order o = mValues.get(position);
                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("orders/" + ordernumber);
                if (o.getChefs().size() == 1 && quantity == 0) {
                    {
                        FirebaseDatabase.getInstance().getReference("orders/" + ordernumber).child("status").setValue("completed");
                        final DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("busytime");
                        dbref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long busytime=(long)dataSnapshot.getValue()-o.getBusytime();
                                dbref.setValue(busytime);
                                dbref.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
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
                dbr.child("waiting_time").setValue(o.getRemaining()*o.getItem_waiting_time());
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
        ImageView image;
        Button prepare;

        public ViewHolder(View view) {
            super(view);
            ordernumber=(TextView)view.findViewById(R.id.order_number);
            item=(TextView)view.findViewById(R.id.item_name);
            quantity=(TextView)view.findViewById(R.id.order_remaining);
            prepare=(Button) view.findViewById(R.id.order_prepare);
            image = (ImageView) view.findViewById(R.id.item_image);

        }

        @Override
        public String toString() {
            return null;
        }
    }
}
