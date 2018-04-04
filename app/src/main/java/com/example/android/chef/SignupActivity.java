package com.example.android.chef;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.media.session.MediaSessionCompat;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


public class SignupActivity extends AppCompatActivity {

    EditText username,password,repassword,regtoken;
    FirebaseAuth conn;
    String token=null;
    ProgressDialog mProgressDialog = null;
    String usrname,pasword;
    //FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

         username=(EditText)findViewById(R.id.reg_username);
         password=(EditText)findViewById(R.id.reg_password);
         repassword=(EditText)findViewById(R.id.reg_repassword);
        regtoken=(EditText)findViewById(R.id.reg_token);
        conn= FirebaseAuth.getInstance();

        DatabaseReference database=FirebaseDatabase.getInstance().getReference("token");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showProgress();
                token=(String)dataSnapshot.getValue();
                hideProgress();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignupActivity.this, "cancelled "+databaseError , Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void register(View v)
    {

        if(!isNetworkAvailable()) {
            showNetworkError();
            return;
                    }

        //Toast.makeText(this,token, Toast.LENGTH_SHORT).show();

          usrname=username.getText().toString().trim();
         pasword=password.getText().toString().trim();
        String repasword=repassword.getText().toString().trim();
        boolean b1=false,b2=false,b3=false;

        if(!token.equals(regtoken.getText().toString().trim())) {
            regtoken.setError("Token does not match");
            b3=true;
        }


        if(usrname.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(usrname).matches())
        {
            username.setError("email does not exist");
           // Toast.makeText(this, "email does not exist", Toast.LENGTH_SHORT).show();
            b1=true;
        }
        if(pasword.isEmpty() || password.length()<6)
        {
            password.setError("password length must be greater than 6");
            //Toast.makeText(this, "email does not exist", Toast.LENGTH_SHORT).show();
            b2=true;
        }

        if(b1 || b2 || b3)
            return;


        if(pasword.equals(repasword))
        {
            showProgress();
            conn.createUserWithEmailAndPassword(usrname,pasword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        final FirebaseUser userinfo=task.getResult().getUser();
                        if(userinfo!=null)
                        {
                            conn.signInWithEmailAndPassword(usrname,pasword).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        DatabaseReference database=FirebaseDatabase.getInstance().getReference("users");
                                        database=database.child(userinfo.getUid());
                                        DatabaseReference chefs=FirebaseDatabase.getInstance().getReference("chefs");
                                        chefs=chefs.child(userinfo.getUid());
                                        Date d=new Date();
                                        chefs.setValue(d.getTime());
                                        final User user=new User(usrname,pasword,"C",null);
                                        database.setValue(user);

                                        database.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                hideProgress();
                                                FirebaseUser user=conn.getCurrentUser();
                                                if(user!=null)
                                                {
                                                    SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
                                                    SharedPreferences.Editor ed=sh.edit();
                                                    ed.putString("username",usrname);
                                                    ed.commit();
                                                    Intent i=new Intent(SignupActivity.this,WelcomeActivity.class);
                                                    i.putExtra("username",usrname);
                                                    startActivity(i);

                                                    Toast.makeText(SignupActivity.this, "successfully registered", Toast.LENGTH_SHORT).show();
                                                    finish();
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
                                    }
                                }
                            });
                        }
                        else
                        {
                            hideProgress();
                        }
                    }
                    else
                    {
                        hideProgress();
                    }
                }
            });



        }
        else
        {

            Toast.makeText(this, "password and re-password does not match", Toast.LENGTH_SHORT).show();
        }
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

    public void loginpage(View v)
    {
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }

}
