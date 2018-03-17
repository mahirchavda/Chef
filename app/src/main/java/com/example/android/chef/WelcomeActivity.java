package com.example.android.chef;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        /*Bundle b=getIntent().getExtras();
        if(b!=null)
        {
            String extras=b.getString("username");
            ((TextView)findViewById(R.id.welcome_message)).setText(extras);
        }
        */
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(this);
        ((TextView)findViewById(R.id.welcome_message)).setText(sh.getString("username","abcd"));

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_options,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(this);
                sh.edit().putString("username","1111");
                Intent i=new Intent(this,LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return true;
    }



}
