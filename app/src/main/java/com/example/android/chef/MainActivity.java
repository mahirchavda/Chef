package com.example.android.chef;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.chef.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(this);




        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {

            DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                      User user=dataSnapshot.getValue(User.class);
                        if(user!=null && user.getType().compareTo("C")==0)
                        {
                            startActivity(new Intent(MainActivity.this,WelcomeActivity.class));
                            finish();
                        }
                        else
                        {
                            loginpage(null);
                        }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else
        {
            FirebaseAuth.getInstance().signOut();
            loginpage(null);
        }

        setContentView(R.layout.activity_main);

    }

    public void loginpage(View v)
    {
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }

}
