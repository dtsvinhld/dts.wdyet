package vn.com.dtsgroup.wdyet.Activities.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.com.dtsgroup.wdyet.Activities.Profile.Account.LoginActivity;
import vn.com.dtsgroup.wdyet.Activities.Profile.ProfileActivity;
import vn.com.dtsgroup.wdyet.Activities.Eatings.EatingTypeActivity;
import vn.com.dtsgroup.wdyet.Activities.Eatings.EatingsActivity;
import vn.com.dtsgroup.wdyet.Activities.Eatings.MaterialActivity;
import vn.com.dtsgroup.wdyet.Activities.News.NewsActivity;
import vn.com.dtsgroup.wdyet.Adapter.ViewPagerAdapter;
import vn.com.dtsgroup.wdyet.Class.Eating;
import vn.com.dtsgroup.wdyet.Class.MenuofDay;
import vn.com.dtsgroup.wdyet.Class.MenuofWeek;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.Fragment.FragmentMoW;
import vn.com.dtsgroup.wdyet.MainActivity;
import vn.com.dtsgroup.wdyet.R;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, FragmentMoW.UpdateMenu {

    private boolean login = false;
    private Button btn_login;
    private CircleImageView img_user;
    private TextView txt_fullname, txt_username;
    private LinearLayout layoutUser;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private LinearLayout layoutMoW;

    private MenuofWeek menuofWeek;
    private ViewPager vP_mow;
    private TabLayout tl_mow;
    private ViewPagerAdapter viewPagerAdapter;
    private Button btn_view_mat;

    private void hideAllLayout(){
        layoutMoW.setVisibility(View.GONE);
    }
    private void showMoW(){
        hideAllLayout();
        layoutMoW.setVisibility(View.VISIBLE);
        setTitle("Thực đơn tuần này");
    }

    private void init(){
        initActionBar();
        initDrawer();
        initNavigation();
        initMoW();
        initHeader();
    }
    private void initHeader(){
        btn_login = (Button) navigationView.getHeaderView(0).findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        img_user = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.img_user_header);
        txt_fullname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_fullname_header);
        txt_username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_username_header);
        layoutUser = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.layoutUser);
        showInfoUser();
    }
    private void showInfoUser(){
        SharedPreferences sP = getSharedPreferences("user", MODE_PRIVATE);
        int id = sP.getInt("id", 0);
        if(id != 0){
            login = true;
            txt_fullname.setText(sP.getString("fullname", ""));
            txt_username.setText(sP.getString("username", ""));
            setVisibilityUser(View.GONE, View.VISIBLE);
        }
        else{
            setVisibilityUser(View.VISIBLE, View.GONE);
        }
    }
    private void setVisibilityUser(int v, int v1){
        btn_login.setVisibility(v);
        layoutUser.setVisibility(v1);
    }
    private void initMoW(){
        layoutMoW = (LinearLayout) findViewById(R.id.layoutMoW);
        showMoW();
        vP_mow = (ViewPager) findViewById(R.id.vP_mow);
        tl_mow = (TabLayout) findViewById(R.id.tl_mow);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        btn_view_mat = (Button) findViewById(R.id.btn_view_mat);
        btn_view_mat.setOnClickListener(this);
        createMoW();
    }
    private void createMoW(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn_view_mat.setVisibility(View.GONE);
                if(login){
                    int uId = getSharedPreferences("user", MODE_PRIVATE).getInt("id", 1);
                    new FillMoW().execute(Module.DOMAINNAME + "mow/" + uId);
                }
                else{
                    new FillMoW().execute(Module.DOMAINNAME + "mow/1");
                }
            }
        });
    }
    private void initActionBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.titleTextColor));
        setSupportActionBar(toolbar);
    }
    private void initDrawer(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void initNavigation(){
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_view_mat:
                Intent intent = new Intent(this, MaterialofDayActivity.class);
                intent.putExtra("day", tl_mow.getSelectedTabPosition() + 1);
                intent.putExtra("id", menuofWeek.getId());
                startActivity(intent);
                break;

            case R.id.btn_login:
                intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, Module.LOGINACTIVITYCODE);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        init();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Module.LOGINACTIVITYCODE){
            if(data != null){
                boolean b = data.getBooleanExtra("res", false);
                if(b){
                    showInfoUser();
                }
            }
        }

        boolean b = getSharedPreferences("user", MODE_PRIVATE).getInt("id", 0) != 0;
        if(b != login){
            login = b;
            createMoW();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sP = getSharedPreferences("user", MODE_PRIVATE);
        boolean b = sP.getInt("id", 0) != 0;
        if(b){
            txt_fullname.setText(sP.getString("fullname", "Họ tên"));
            txt_username.setText(sP.getString("username", "@"));
        }
        else{
            login = false;
            setVisibilityUser(View.VISIBLE, View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_mow:
                showMoW();
                break;

            case R.id.nav_mat:
                Intent intent = new Intent(this, MaterialActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_eat:
                intent = new Intent(this, EatingTypeActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_fav:
                intent = new Intent(this, EatingsActivity.class);
                intent.putExtra("key", "fav");
                startActivity(intent);
                break;

            case R.id.nav_profile:
                if(login) {
                    Intent i = new Intent(this, ProfileActivity.class);
                    i.putExtra("tab", 0);
                    startActivity(i);
                }
                else{
                    intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, Module.LOGINACTIVITYCODE);
                }
                break;

            case R.id.nav_news:
                startActivity(new Intent(this, NewsActivity.class));
                break;

            case R.id.nav_noti:
                if(login){
                    Intent i = new Intent(this, ProfileActivity.class);
                    i.putExtra("tab", 1);
                    startActivity(i);
                }
                else{
                    intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, Module.LOGINACTIVITYCODE);
                }
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onUpdateMenu(int meal, int eatingId) {
//        Toast.makeText(this, Module.DOMAINNAME + "updatemenu/" +
//                getSharedPreferences("menu", MODE_PRIVATE).getInt("id", 0) + "/"
//                + (tl_mow.getSelectedTabPosition() + 1) + "/" + (meal + 1) + "/" + eatingId, Toast.LENGTH_LONG).show();
        new MainActivity.SubmitLink().execute(Module.DOMAINNAME + "updatemenu/" +
                getSharedPreferences("menu", MODE_PRIVATE).getInt("id", 0) + "/"
                + (tl_mow.getSelectedTabPosition() + 1) + "/" + (meal + 1) + "/" + eatingId);
    }

    class FillMoW extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            return Module.ContentURL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                if(jsonArray.length() > 0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if(jsonObject.getString("title").equals("Info")){
                        jsonObject = jsonObject.getJSONObject("info");
                        int days = jsonObject.getInt("days");
                        int meals = jsonObject.getInt("meals");

                        JSONArray array = jsonObject.getJSONArray("menu");
                        ArrayList<MenuofDay> menuofDays = new ArrayList<MenuofDay>();
                        String listId = "";
                        for(int i=0; i<days; i++){
                            ArrayList<Eating> eatings = new ArrayList<Eating>();
                            ArrayList<String> ids = new ArrayList<String>(), names = new ArrayList<String>(), images = new ArrayList<String>();
                            for(int j=0; j<meals; j++){
                                JSONObject object = array.getJSONObject(i*meals + j);
                                eatings.add(new Eating(object.getInt("eatingId"), object.getString("name"), object.getString("image")));
                                listId +=  object.getString("eatingId") + "_";
                                ids.add(object.getString("eatingId"));
                                names.add(object.getString("name"));
                                images.add(object.getString("image"));
                            }
                            menuofDays.add(new MenuofDay(eatings));

                            FragmentMoW fragmentMoW = new FragmentMoW();
                            Bundle bundle = new Bundle();
                            bundle.putInt("day", i+1);
                            bundle.putInt("menu", jsonObject.getInt("id"));
                            bundle.putStringArrayList("ids", ids);
                            bundle.putStringArrayList("names", names);
                            bundle.putStringArrayList("images", images);
                            fragmentMoW.setArguments(bundle);
                            viewPagerAdapter.addFragmentPager(fragmentMoW, DayofWeekbyNumber(i + 1));
                        }

                        menuofWeek = new MenuofWeek(jsonObject.getInt("id"), menuofDays);
//                        Toast.makeText(NavigationDrawerActivity.this, "" + jsonObject.getInt("id"), Toast.LENGTH_LONG).show();

                        listId = listId.substring(0, listId.length() - 1);
                        SharedPreferences sP = getSharedPreferences("menu", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sP.edit();
                        edit.putString("listId", listId);
                        edit.putInt("id", menuofWeek.getId());
                        edit.commit();

                        vP_mow.setAdapter(viewPagerAdapter);
                        tl_mow.setupWithViewPager(vP_mow);
                        vP_mow.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
                        btn_view_mat.setVisibility(View.VISIBLE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(NavigationDrawerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {

            }
        }
    }

    public static String DayofWeekbyNumber(int day){
        int d = day % 7;
        return d == 0 ? "Chủ nhật" :
                (d == 1 ? "Thứ hai" :
                        (d == 2 ? "Thứ ba" :
                                (d==3 ? "Thứ tư" :
                                    (d == 4 ? "Thứ năm" :
                                            (d == 5 ? "Thứ sáu" :
                                                    (d == 6 ? "Thứ bảy" : ""))))));
    }
}
