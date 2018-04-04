package com.example.android.chef;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.chef.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
     EditText username,password;
     String usrname,pasword;
      FirebaseAuth conn;
     ProgressDialog mProgressDialog = null;

     /*FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myref;
    boolean checkconnectivity=false,validate=false;
   */ @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

         username=(EditText)findViewById(R.id.username);
         password=(EditText)findViewById(R.id.password);


        conn= FirebaseAuth.getInstance();

        /*DatabaseReference connectedRef =database.getReference(".info/connected");

        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    checkconnectivity=true;

                } else {
                    checkconnectivity=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

    */
    }

    public void verify(View v)
    {
        boolean b1=false,b2=false;

        usrname=username.getText().toString();
        pasword=password.getText().toString();


        if(usrname.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(usrname).matches())
        {
            username.setError("email does not exist");
            //Toast.makeText(this, "email does not exist", Toast.LENGTH_SHORT).show();
            b1=true;
        }
         if(pasword.isEmpty() || password.length()<6)
        {
            password.setError("password length must be greater than 6");
            //Toast.makeText(this, "email does not exist", Toast.LENGTH_SHORT).show();
            b2=true;
        }
        if(b1 || b2)
            return;

        if(isNetworkAvailable())
        {
            showProgress();
            conn.signInWithEmailAndPassword(usrname,pasword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                        dbref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                hideProgress();
                                User user=dataSnapshot.getValue(User.class);
                                if(user!=null && user.getType().compareTo("C")==0) {
                                    SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                    SharedPreferences.Editor ed = sh.edit();
                                    ed.putString("username", username.getText().toString());
                                    ed.commit();
                                    Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
                                    i.putExtra("username", username.getText().toString());
                                    startActivity(i);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "please log in from user app ", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        hideProgress();
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            showNetworkError();
        }




       /* if(username.getText().toString().equals(password.getText().toString()))
        {
            SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor ed=sh.edit();
            ed.putString("username",username.getText().toString());
            ed.commit();
            Intent i=new Intent(this,WelcomeActivity.class);
            i.putExtra("username",username.getText().toString());
            startActivity(i);
        }
        */
    }

    public void redirecttoregisterpage(View v)
    {
        Intent i=new Intent(this,SignupActivity.class);
        startActivity(i);
        finish();
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.style.Theme_Material_Light_Dialog_Alert : -1);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void hideProgress() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNetworkError() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.style.Theme_Material_Light_Dialog_Alert : -1);
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.app_name));
        alertDialogBuilder
                .setTitle("Network Error")
                .setMessage("Please check your internet connection.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
