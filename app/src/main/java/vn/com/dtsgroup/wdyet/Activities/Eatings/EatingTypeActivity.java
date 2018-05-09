package vn.com.dtsgroup.wdyet.Activities.Eatings;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.dtsgroup.wdyet.Adapter.ETAdapter;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class EatingTypeActivity extends AppCompatActivity {

    private ArrayList<ArrayList<ETAdapter.ET>> etsArray;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eating_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Món ngon các loại");

        listView = (ListView) findViewById(R.id.lv_et);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new FillET().execute(Module.DOMAINNAME + "et");
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

    class FillET extends AsyncTask<String, Integer, String>{
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
                        etsArray = new ArrayList<ArrayList<ETAdapter.ET>>();
                        for(int i=0; i<array.length(); i+=2){
                            ArrayList<ETAdapter.ET> ets = new ArrayList<ETAdapter.ET>();
                            JSONObject object = array.getJSONObject(i);
                            ets.add(new ETAdapter.ET(object.getInt("id"), object.getString("name"),
                                    object.getString("image")));
                            if(i+1<array.length()){
                                object = array.getJSONObject(i+1);
                                ets.add(new ETAdapter.ET(object.getInt("id"), object.getString("name"),
                                        object.getString("image")));
                            }
                            etsArray.add(ets);
                        }
                        listView.setAdapter(new ETAdapter(
                                EatingTypeActivity.this,
                                android.R.layout.simple_list_item_1,
                                etsArray
                        ));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(EatingTypeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
