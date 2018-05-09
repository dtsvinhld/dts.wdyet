package vn.com.dtsgroup.wdyet.Activities.Eatings;

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

import vn.com.dtsgroup.wdyet.Adapter.MaterialAdapter;
import vn.com.dtsgroup.wdyet.Class.Element;
import vn.com.dtsgroup.wdyet.Class.Material;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

public class MaterialActivity extends AppCompatActivity {


    private ListView lv_mat;
    private ArrayList<ArrayList<Material>> materials;
    private ArrayList<Material> materialArrayList;

    private void initMat(){
        lv_mat = (ListView) findViewById(R.id.lv_mat);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new FillMat().execute(Module.DOMAINNAME + "mat");
            }
        });

        lv_mat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MaterialActivity.this, materials.get(0).get(0).getElements().get(0).getName() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Thành phần dinh dưỡng");
        initMat();
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
                ArrayList<Material> ms = new ArrayList<Material>();
                ArrayList<ArrayList<Material>> msa = new ArrayList<ArrayList<Material>>();
                for(int i=0; i<materialArrayList.size(); i++){
                    if(materialArrayList.get(i).getKey().contains(key)){
                        ms.add(materialArrayList.get(i));
                        if(ms.size() == 3 || i == materialArrayList.size() - 1){
                            msa.add(ms);
                            ms = new ArrayList<Material>();
                        }
                    }
                }
                lv_mat.setAdapter(new MaterialAdapter(
                        MaterialActivity.this,
                        android.R.layout.simple_list_item_1,
                        msa
                ));
                return true;
            }
        });
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        return true;
    }

    class FillMat extends AsyncTask<String, Integer, String> {
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
                        materials = new ArrayList<ArrayList<Material>>();
                        ArrayList<Material> mats = new ArrayList<Material>();
                        materialArrayList = new ArrayList<Material>();
                        for(int i=0; i<array.length(); i+=3){
                            mats = new ArrayList<Material>();

                            JSONObject object = array.getJSONObject(i);
                            JSONArray element = object.getJSONArray("element");
                            ArrayList<Element> elements = new ArrayList<Element>();
                            for(int j=0; j<element.length(); j++){
                                JSONObject eo = element.getJSONObject(j);
                                elements.add(new Element(eo.getString("name"), eo.getInt("soluong"), eo.getString("donvi")));
                            }
                            Material m = new Material(object.getInt("id"), object.getString("name"),
                                    object.getString("image"), elements);
                            mats.add(m);
                            materialArrayList.add(m);

                            if(i+1<array.length()){
                                object = array.getJSONObject(i+1);
                                element = object.getJSONArray("element");
                                elements = new ArrayList<Element>();
                                for(int j=0; j<element.length(); j++){
                                    JSONObject eo = element.getJSONObject(j);
                                    elements.add(new Element(eo.getString("name"), eo.getInt("soluong"), eo.getString("donvi")));
                                }
                                m = new Material(object.getInt("id"), object.getString("name"),
                                        object.getString("image"), elements);
                                mats.add(m);
                                materialArrayList.add(m);
                            }

                            if(i+2<array.length()){
                                object = array.getJSONObject(i+2);
                                element = object.getJSONArray("element");
                                elements = new ArrayList<Element>();
                                for(int j=0; j<element.length(); j++){
                                    JSONObject eo = element.getJSONObject(j);
                                    elements.add(new Element(eo.getString("name"), eo.getInt("soluong"), eo.getString("donvi")));
                                }
                                m = new Material(object.getInt("id"), object.getString("name"),
                                        object.getString("image"), elements);
                                mats.add(m);
                                materialArrayList.add(m);
                            }

                            materials.add(mats);
                        }

                        lv_mat.setAdapter(new MaterialAdapter(
                                MaterialActivity.this,
                                android.R.layout.simple_list_item_1,
                                materials
                        ));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MaterialActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
