package vn.com.dtsgroup.wdyet.Activities.Rating;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import vn.com.dtsgroup.wdyet.Adapter.RatingAdapter;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class RatingActivity extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener{

    private MaterialRatingBar ratingBar;
    private TextView txt_rating;
    private EditText edt_rating;
    private Button btn_addComment, btn_rate, btn_ratingAgain;
    private LinearLayout layout_rating, layout_after_rating;
    private ListView lv_rating;
    private ArrayList<RatingAdapter.Rate> rates;
    private String comment;
    private int rat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("name"));

        rates = new ArrayList<>();

        layout_rating = findViewById(R.id.layout_rating);
        layout_after_rating = findViewById(R.id.layout_after_rating);
        layout_after_rating.setVisibility(View.GONE);
        btn_ratingAgain = findViewById(R.id.btn_rateAgain);
        btn_ratingAgain.setOnClickListener(this);

        ratingBar = findViewById(R.id.ratingBar);
        txt_rating = findViewById(R.id.txt_rating);
        txt_rating.setText("");
        btn_rate = findViewById(R.id.btn_rate);
        btn_rate.setEnabled(false);
        ratingBar.setOnRatingBarChangeListener(this);
        edt_rating = findViewById(R.id.edt_rating);
        edt_rating.setVisibility(View.GONE);
        btn_addComment = findViewById(R.id.btn_addComment);
        btn_addComment.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn_addComment.setOnClickListener(this);
        btn_rate.setOnClickListener(this);

        lv_rating = findViewById(R.id.lv_rating);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new FillRating().execute(Module.DOMAINNAME + "rats/" + getIntent().getIntExtra("id", 0));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_rateAgain:
                ratingAgainClick();
                break;

            case R.id.btn_addComment:
                addCommentClick();
                break;

            case R.id.btn_rate:
                rateClick();
                break;
        }
    }


    private void ratingAgainClick(){
        layout_rating.setVisibility(View.VISIBLE);
        layout_after_rating.setVisibility(View.GONE);
        ratingBar.setRating(0);
        txt_rating.setText("");
        edt_rating.setText("");
        edt_rating.setVisibility(View.GONE);
        btn_addComment.setVisibility(View.VISIBLE);
        btn_rate.setEnabled(false);
    }
    private void addCommentClick(){
        edt_rating.setVisibility(View.VISIBLE);
        btn_addComment.setVisibility(View.GONE);
        edt_rating.requestFocusFromTouch();
    }
    private void rateClick(){
        rat = (int) ratingBar.getRating();
        if(rat>0){
            btn_rate.setEnabled(false);
            btn_addComment.setEnabled(false);
            comment = edt_rating.getText().toString();
            if(comment.isEmpty())   comment = txt_rating.getText().toString();
            String url = Module.DOMAINNAME + "rating/" + getIntent().getIntExtra("id", 0) +
                    "/" + getSharedPreferences("user", MODE_PRIVATE).getInt("id", 0)
                    + "/" + rat + "/" + Module.EncodeBase64(comment);
            new AddRating().execute( url);
//            Log.e("HIHI", url);
//            Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if(ratingBar.getId() == R.id.ratingBar){
            int rat = (int) rating;
            switch (rat){
                case 0:
                    txt_rating.setText("");
                    break;

                case 1:
                    txt_rating.setText("Ghét");
                    break;

                case 2:
                    txt_rating.setText("Không thích");
                    break;

                case 3:
                    txt_rating.setText("OK");
                    break;

                case 4:
                    txt_rating.setText("Thích");
                    break;

                case 5:
                    txt_rating.setText("Rất thích");
                    break;
            }
            if(rat > 0){
                btn_rate.setEnabled(true);
            }
            else{
                btn_rate.setEnabled(false);
            }
        }
    }

    class AddRating extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            return Module.ContentURL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Toast.makeText(RatingActivity.this, s, Toast.LENGTH_SHORT).show();
            try {
                JSONArray jsonArray = new JSONArray(s);
                if(jsonArray.length()>0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if(jsonObject.getString("title").equals("Info")){
                        layout_rating.setVisibility(View.GONE);
                        layout_after_rating.setVisibility(View.VISIBLE);
                        Intent intent = new Intent();
                        intent.putExtra("rating", (int) ratingBar.getRating());
                        setResult(Module.RATINGACTIVITYCODE, intent);
//                        Date d = new Date();
//                        String cD = d.getYear() + "/" + d.getMonth() + "/" + d.getDay() + " "
//                                + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
                        String cD = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()).toString();
                        rates.add(0, new RatingAdapter.Rate(0,
                                getSharedPreferences("user", MODE_PRIVATE).getString("fullname", ""),
                                cD, comment, rat));
                        lv_rating.setAdapter(new RatingAdapter(
                                RatingActivity.this,
                                android.R.layout.simple_list_item_1,
                                rates
                        ));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                btn_rate.setEnabled(true);
                btn_addComment.setEnabled(true);
            }
        }
    }

    class FillRating extends AsyncTask<String, Integer, String>{
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
                        JSONArray array = jsonObject.getJSONArray("info");
                        rates = new ArrayList<>();
                        for(int i=0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            rates.add(new RatingAdapter.Rate(object.getInt("id"), object.getString("fullname"),
                                    object.getString("date"), object.getString("comment"),
                                    object.getInt("rate")));
                        }
                        lv_rating.setAdapter(new RatingAdapter(
                                RatingActivity.this,
                                android.R.layout.simple_list_item_1,
                                rates
                        ));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
//                Toast.makeText(RatingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
