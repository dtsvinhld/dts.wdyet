package vn.com.dtsgroup.wdyet.Activities.Result;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        Intent intent = getIntent();
        int id = getSharedPreferences("user", MODE_PRIVATE).getInt("id", 0);
        int day = intent.getIntExtra("day", 0);
        int meal = intent.getIntExtra("meal", 0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
//        Toast.makeText(this, getIntent().getStringExtra("message"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class GetEID extends AsyncTask<String, Integer, String>{
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

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
