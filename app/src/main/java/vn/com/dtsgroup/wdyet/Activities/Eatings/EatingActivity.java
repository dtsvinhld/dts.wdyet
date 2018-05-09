package vn.com.dtsgroup.wdyet.Activities.Eatings;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.dtsgroup.wdyet.Activities.Rating.RatingActivity;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class EatingActivity extends AppCompatActivity {

    private int id, userId;
    private LinearLayout layout_noti, layout_rate;
    private TextView txt_noti;
    private Button btn_noti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout_rate = findViewById(R.id.layout_rate);
        layout_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EatingActivity.this, "1", Toast.LENGTH_SHORT).show();
            }
        });
        layout_noti = findViewById(R.id.layout_noti_like);
        layout_noti.setVisibility(View.GONE);
        txt_noti = findViewById(R.id.txt_noti_like);
        btn_noti = findViewById(R.id.btn_noti_like);
        btn_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EatingActivity.this, EatingsActivity.class);
                intent.putExtra("key", "fav");
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
//        String message = intent.getStringExtra("message");
//        if(message.isEmpty()){
//            Log.e("EOE", "E");
//        }
        boolean message = intent.getBooleanExtra("message", false);
        userId = getSharedPreferences("user", MODE_PRIVATE).getInt("id", 0);
        if(message){
            setTitle("");
            final int day = intent.getIntExtra("day", 0);
            final int meal = intent.getIntExtra("meal", 0);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new GetEating().execute(Module.DOMAINNAME + "eat/" + userId + "/" + day + "/" + meal);
                }
            });
        }
        else{
            setTitle(intent.getStringExtra("name"));

            id = intent.getIntExtra("id", 0);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new GetEating().execute(Module.DOMAINNAME + "eat/" + id + "/" + userId);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if(item.getItemId() == R.id.action_favorite){
            if(userId != 0){
//                int value = menuItem.getIcon() == getDrawable(R.drawable.ic_fav_gray) ? 1 : 0;
                int l = like == 1 ? 0 : 1;
                new LikeEating().execute(Module.DOMAINNAME + "likeeating/" + userId + "/" + id + "/" + l);
            }
            return true;
        }
        if(item.getItemId() == R.id.action_rate){
            Intent intent = new Intent(EatingActivity.this, RatingActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("name", getIntent().getStringExtra("name"));
            startActivityForResult(intent, Module.RATINGACTIVITYCODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Module.RATINGACTIVITYCODE){
            if(data != null){
                if(rated == 0){
                    rating = (rating * rates + data.getIntExtra("rating", 0)) / (rates + 1);
                    rates += 1;
                }
                else{
                    int r = data.getIntExtra("rating", 0);
                    rating = (rating * rates + r - rated) / rates ;
                    rated = r;
                }
                ((TextView) findViewById(R.id.txt_eating_rate)).setText("(" + rating + ")");
                ((TextView) findViewById(R.id.txt_eating_rated)).setText(rates + " người đánh giá");
            }
        }
    }

    private MenuItem menuItem, itemRate;
    private int like = 0, liked = 0, rates = 0, rated = 0;
    private double rating = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_heart, menu);
        menuItem = menu.findItem(R.id.action_favorite);
        itemRate = menu.findItem(R.id.action_rate);
        menuItem.setVisible(false);
        itemRate.setVisible(false);
        if(userId != 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout_noti.setVisibility(View.GONE);
    }

    public class GetEating extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return Module.ContentURL(strings[0]);
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            try {
                JSONArray jsonArray = new JSONArray(string);
                if(jsonArray.length() > 0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0).getJSONObject("info");

                    Picasso.with(getApplicationContext())
                            .load(Module.IMAGELINK +  jsonObject.getString("image"))
                            .into((ImageView)findViewById(R.id.img_eating));
                    ((TextView) findViewById(R.id.txt_eating_name)).setText(jsonObject.getString("name"));
                    setTitle(jsonObject.getString("name"));
                    rating = jsonObject.getDouble("rating");
                    ((TextView) findViewById(R.id.txt_eating_rate)).setText("(" + rating + ")");
                    rates = jsonObject.getInt("rates");
                    ((TextView) findViewById(R.id.txt_eating_rated)).setText(rates + " người đánh giá");
                    ((TextView) findViewById(R.id.txt_eating_views)).setText(jsonObject.getString("views") + " views");
                    ((JustifiedTextView) findViewById(R.id.jT_eating_info)).setText(jsonObject.getString("info"));

                    JSONArray jsonArray1 = jsonObject.getJSONArray("elements");
                    String elements = "";
                    for(int i=0; i< jsonArray1.length(); i++){
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        elements += jsonObject1.getString("name") + ": " + jsonObject1.getString("soluong");
                        if(i != jsonArray1.length() - 1)
                            elements += "\n\n";
                    }
                    ((TextView) findViewById(R.id.txt_eating_mats)).setText(jsonArray1.length() + " loại");
                    ((JustifiedTextView) findViewById(R.id.jT_eating_element)).setText(elements);

                    JSONArray jsonArray2 = jsonObject.getJSONArray("recipe");
                    String stringRecipe = "";
                    for(int i=0; i<jsonArray2.length(); i++){
                        JSONObject jsonObject1 = jsonArray2.getJSONObject(i);
                        stringRecipe += "Bước " + jsonObject1.getString("step") + ".\n" + jsonObject1.getString("recipe");
                        if(i != jsonArray2.length() - 1)
                            stringRecipe += "\n\n";
                    }
                    ((JustifiedTextView) findViewById(R.id.jT_eating_recipe)).setText(stringRecipe);

                    like = jsonObject.getInt("like");
                    if(like == 1){
                        menuItem.setIcon(R.drawable.ic_fav_red_24dp);
                    }
                    else{
                        menuItem.setIcon(R.drawable.ic_fav_gray_24dp);
                    }

                    liked = jsonObject.getInt("liked");
                    ((TextView) findViewById(R.id.txt_eating_liked)).setText(liked + " liked");
                    rated = jsonObject.getInt("rated");

                    if(userId != 0){
                        menuItem.setVisible(true);
                        itemRate.setVisible(true);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Không phản hồi", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally {
            }
        }
    }

    public class LikeEating extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            return Module.ContentURL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                if(jsonArray.length()>0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if(jsonObject.getString("title").equals("Info")){
                        if(like == 1){
                            menuItem.setIcon(R.drawable.ic_fav_gray);
                            txt_noti.setText("Đã xóa khỏi yêu thích của bạn");
                            layout_noti.setVisibility(View.VISIBLE);
                            like = 0;
                            liked -= 1;
                            ((TextView) findViewById(R.id.txt_eating_liked)).setText(liked + " liked");
                        }
                        else{
                            menuItem.setIcon(R.drawable.ic_fav_red);
                            txt_noti.setText("Đã thêm vào yêu thích của bạn");
                            layout_noti.setVisibility(View.VISIBLE);
                            like = 1;
                            liked += 1;
                            ((TextView) findViewById(R.id.txt_eating_liked)).setText(liked + " liked");
                        }
                    }
                    else{
                        Toast.makeText(EatingActivity.this, jsonObject.getString("notify") +
                                jsonObject.getString("info"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(EatingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
