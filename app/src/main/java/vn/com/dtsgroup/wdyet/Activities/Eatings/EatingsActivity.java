package vn.com.dtsgroup.wdyet.Activities.Eatings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.dtsgroup.wdyet.Adapter.EatingsAdapter;
import vn.com.dtsgroup.wdyet.Class.Eating;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class EatingsActivity extends AppCompatActivity {

    private ListView lv;
    private ArrayList<Eating> eatings, es;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eatings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        lv = (ListView) findViewById(R.id.lv_es);

        switch (key){
            case "et":
                init(intent.getStringExtra("name"),
                        Module.DOMAINNAME + "es/" + intent.getIntExtra("type", 0));
                break;

            case "fav":
                SharedPreferences sP = getSharedPreferences("user", MODE_PRIVATE);
                int id = sP.getInt("id", 0);
                if(id == 0){
                    Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    init("Món ăn yêu thích", Module.DOMAINNAME + "fav/" + id);
                }
                break;
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(EatingsActivity.this, EatingActivity.class);
                i.putExtra("id", es.get(position).getId());
                i.putExtra("name", es.get(position).getName());
                startActivity(i);
            }
        });
    }

    private void init(String title, final String url){
        setTitle(title);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new FillES().execute(url);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_search, menu);

        MenuItem menuItem = menu.findItem(R.id.itemSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String key = Module.removeAccent(newText);
                es = new ArrayList<Eating>();
                for(int i=0; i<eatings.size(); i++){
                    if(eatings.get(i).getKey().contains(key)){
                        es.add(eatings.get(i));
                    }
                }
                lv.setAdapter(new EatingsAdapter(
                        EatingsActivity.this,
                        android.R.layout.simple_list_item_1,
                        es
                ));
                return true;
            }
        });
        searchView.setFocusable(false);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        return true;
    }

    class FillES extends AsyncTask<String, Integer, String>{

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
                        eatings = new ArrayList<Eating>();
                        es = new ArrayList<Eating>();
                        for(int i=0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            Eating e = new Eating(object.getInt("id"), object.getString("name"),
                                    object.getString("image"), object.getString("info"));
                            eatings.add(e);
                            es.add(e);
                        }
                        lv.setAdapter(new EatingsAdapter(
                                EatingsActivity.this,
                                android.R.layout.simple_list_item_1,
                                eatings
                        ));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(EatingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
