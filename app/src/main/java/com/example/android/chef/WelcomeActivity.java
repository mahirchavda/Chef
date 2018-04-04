package com.example.android.chef;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        /*Bundle b=getIntent().getExtras();
        if(b!=null)
        {
            String extras=b.getString("username");
            ((TextView)findViewById(R.id.welcome_message)).setText(extras);
        }
        */
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(this);
        //((TextView)findViewById(R.id.welcome_message)).setText(sh.getString("username","abcd"));

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager=(ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
       // adapter.addFragment(new CatagoryFragment(),"Item");
       // adapter.addFragment(new CartFragment(), "Cart");
       // adapter.addFragment(new OrderFragment(),"Order");
        adapter.addFragment(new WaitOrderFragment(),"Waiting");
        adapter.addFragment(new PrepareOrderFragment(),"Preparing");
        viewPager.setAdapter(adapter);
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

}
