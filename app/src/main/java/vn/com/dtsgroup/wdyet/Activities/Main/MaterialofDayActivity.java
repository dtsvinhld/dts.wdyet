package vn.com.dtsgroup.wdyet.Activities.Main;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.dtsgroup.wdyet.Activities.Main.NavigationDrawerActivity;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class MaterialofDayActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materialof_day);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.txt_mfd);

        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", 0);
        final int day = intent.getIntExtra("day", 0);
        setTitle("Nguyên liệu cho " + NavigationDrawerActivity.DayofWeekbyNumber(day));

//        getSupportActionBar().setTitle(Html.fromHtml("<font color='red'> Hihi con chó </font>"));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    new MfD().execute(Module.DOMAINNAME + "mfd/" + id + "/" + day);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MfD extends AsyncTask<String, Integer, String>{
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
                        JSONArray array = jsonObject.getJSONArray("info");
                        String string = "Chuẩn bị những nguyên liệu sau:\n\n";
                        for(int i=0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            string += object.getString("name") + ": " + object.getString("soluong") + "\n";
                        }
                        string += "\n Nếu nguyên liệu nào vẫn còn trong bếp nhà bạn thì đừng mua thêm nhé :D";
                        textView.setText(string);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
