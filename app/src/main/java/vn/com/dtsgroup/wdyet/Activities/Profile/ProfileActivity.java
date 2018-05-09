package vn.com.dtsgroup.wdyet.Activities.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import vn.com.dtsgroup.wdyet.Activities.Profile.Account.LoginActivity;
import vn.com.dtsgroup.wdyet.Adapter.ViewPagerAdapter;
import vn.com.dtsgroup.wdyet.Fragment.NotifyFragment;
import vn.com.dtsgroup.wdyet.Fragment.ProfileFragment;
import vn.com.dtsgroup.wdyet.R;

public class ProfileActivity extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        checkLogin();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initFragment();
    }

    private void initFragment(){
        tabLayout = (TabLayout) findViewById(R.id.tl_profile);
        viewPager = (ViewPager) findViewById(R.id.vP_profile);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), ProfileActivity.this);

        int tab = getIntent().getIntExtra("tab", 1);

        switch (tab){
            case 0:
                viewPagerAdapter.addFragmentPager(new ProfileFragment(), "Thông tin");
                setTitle("Cá nhân");
                break;

            case 1:
                viewPagerAdapter.addFragmentPager(new NotifyFragment(), "Thông báo");
                setTitle("Thông báo");
                break;
        }
//        viewPagerAdapter.addFragmentPager(new SettingsFragment(), "Cài đặt");
//        viewPagerAdapter.addFragmentPager(new MyMenuFragment(), "Thực đơn của tôi");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFragment();
    }

    private void checkLogin(){
        SharedPreferences sP = getSharedPreferences("user", MODE_PRIVATE);
        int id = sP.getInt("id", 0);
        if(id == 0){
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
