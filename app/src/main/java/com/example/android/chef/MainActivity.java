package com.example.android.chef;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(this);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent i=new Intent(this,WelcomeActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
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
