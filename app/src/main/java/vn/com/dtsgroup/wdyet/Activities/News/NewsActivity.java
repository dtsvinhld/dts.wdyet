package vn.com.dtsgroup.wdyet.Activities.News;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.dtsgroup.wdyet.Adapter.NewsAdapter;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class NewsActivity extends AppCompatActivity {

    ListView lv_news;
    ArrayList<NewsAdapter.News> newsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tin tức");

        lv_news = findViewById(R.id.lv_news);
        newsArrayList = new ArrayList<NewsAdapter.News>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetNews().execute(Module.DOMAINNAME + "news");
            }
        });
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsActivity.this, WebViewActivity.class);
                intent.putExtra("title", newsArrayList.get(position).getTitle());
                intent.putExtra("link", newsArrayList.get(position).getLink());
                startActivity(intent);
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

    class GetNews extends AsyncTask<String, Integer, String>{

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
                        newsArrayList = new ArrayList<NewsAdapter.News>();
                        for(int i=0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            newsArrayList.add(new NewsAdapter.News(object.getInt("id"),
                                    object.getString("title"),
                                    object.getString("image"),
                                    object.getString("createdDate"),
                                    object.getString("link")));
                        }

                        lv_news.setAdapter(new NewsAdapter(
                                NewsActivity.this,
                                android.R.layout.simple_list_item_1,
                                newsArrayList
                        ));
                    }
                    else{
                        Toast.makeText(NewsActivity.this,
                                jsonObject.getString("notify") + ": " + jsonObject.getString("info"),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(NewsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
