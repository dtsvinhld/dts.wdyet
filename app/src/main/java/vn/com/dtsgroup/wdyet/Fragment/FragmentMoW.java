package vn.com.dtsgroup.wdyet.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.dtsgroup.wdyet.Activities.Eatings.EatingActivity;
import vn.com.dtsgroup.wdyet.Adapter.EatingAdapterforMoW;
import vn.com.dtsgroup.wdyet.Adapter.EatingOptionAdapter;
import vn.com.dtsgroup.wdyet.Class.Eating;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.MainActivity;
import vn.com.dtsgroup.wdyet.R;

/**
 * Created by Verdant on 4/13/2018.
 */

public class FragmentMoW extends Fragment implements EatingAdapterforMoW.ChangeItem{

    private ArrayList<Eating> eatings;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lv_only, container, false);

        final Bundle bundle = getArguments();
        listView = view.findViewById(R.id.lv_fragment);

        ArrayList<String> ids = bundle.getStringArrayList("ids"),
                names = bundle.getStringArrayList("names"),
                images = bundle.getStringArrayList("images");

        eatings = new ArrayList<Eating>();
        for(int i=0; i<ids.size(); i++){
            eatings.add(new Eating(Integer.parseInt(ids.get(i)), names.get(i), images.get(i)));
        }

        listView.setAdapter(new EatingAdapterforMoW(
                getContext(),
                android.R.layout.simple_list_item_1,
                eatings,
                this
        ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), EatingActivity.class);
                intent.putExtra("id", eatings.get(position).getId());
                intent.putExtra("name", eatings.get(position).getName());
                startActivity(intent);
            }
        });

        new OptionEating().execute(Module.DOMAINNAME + "option/1", Module.DOMAINNAME + "option/2");
        initDialog();

        return view;
    }

    @Override
    public void onChangeItem(int type, int position, int changeType) {
        positionItem = position;
        SharedPreferences sP = getContext().getSharedPreferences("menu", getContext().MODE_PRIVATE);
        String listId = sP.getString("listId", "");
        if(changeType == 0)
            new RandomEating().execute(Module.DOMAINNAME + "random/" + type + "/" + listId);
        else{
            if(type == 1){
                lv_option.setAdapter(new EatingOptionAdapter(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        eatings1
                ));
                eatingArrayList = eatings1;
                aes = eatings1;
            }
            else if(type == 2){
                lv_option.setAdapter(new EatingOptionAdapter(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        eatings2
                ));
                eatingArrayList = eatings2;
                aes = eatings2;
            }
            dialog.show();
        }
    }

    private void initDialog(){
        eatingArrayList = new ArrayList<Eating>();
        aes = new ArrayList<Eating>();
        dialog = new Dialog(getContext());
        dialog.setTitle("Chọn một món");
        dialog.setContentView(R.layout.dialog_eating_option);
        lv_option = (ListView) dialog.findViewById(R.id.lv_eating_option);
        searchView = (SearchView) dialog.findViewById(R.id.sv_eating_option);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String key = Module.removeAccent(newText);
                aes = new ArrayList<Eating>();
                for(int i = 0; i< eatingArrayList.size(); i++){
                    if(eatingArrayList.get(i).getKey().contains(key))
                        aes.add(eatingArrayList.get(i));
                }
                lv_option.setAdapter(new EatingOptionAdapter(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        aes
                ));
                return true;
            }
        });
        lv_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eatings.set(positionItem, aes.get(position));

                listView.setAdapter(new EatingAdapterforMoW(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        eatings,
                        FragmentMoW.this
                ));
                updateMenu.onUpdateMenu(positionItem, eatings.get(positionItem).getId());
                dialog.hide();
            }
        });
    }

    private int positionItem;
    private Dialog dialog;
    private ListView lv_option;
    private SearchView searchView;
    private ArrayList<Eating> eatings1, eatings2, eatingArrayList, aes;

    class OptionEating extends AsyncTask<String, Integer, ArrayList<String>>{
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(Module.ContentURL(strings[0]));
            arrayList.add(Module.ContentURL(strings[1]));
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s.get(0));
                if(jsonArray.length() > 0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if(jsonObject.getString("title").equals("Info")){
                        JSONArray array = jsonObject.getJSONArray("info");
                        eatings1 = new ArrayList<Eating>();
                        for(int i = 0; i < array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            eatings1.add(new Eating(object.getInt("id"), object.getString("name"), object.getString("image")));
                        }

                    }
                }

                jsonArray = new JSONArray(s.get(1));
                if(jsonArray.length() > 0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if(jsonObject.getString("title").equals("Info")){
                        JSONArray array = jsonObject.getJSONArray("info");
                        eatings2 = new ArrayList<Eating>();
                        for(int i = 0; i < array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            eatings2.add(new Eating(object.getInt("id"), object.getString("name"), object.getString("image")));
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class RandomEating extends AsyncTask<String, Integer, String> {

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
                        JSONObject object = jsonObject.getJSONObject("info");

                        eatings.get(positionItem).setId(object.getInt("id"));
                        eatings.get(positionItem).setName(object.getString("name"));
                        eatings.get(positionItem).setImage(object.getString("image"));

                        listView.setAdapter(new EatingAdapterforMoW(
                                getContext(),
                                android.R.layout.simple_list_item_1,
                                eatings,
                                FragmentMoW.this
                        ));
                        updateMenu.onUpdateMenu(positionItem, eatings.get(positionItem).getId());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface UpdateMenu{
        void onUpdateMenu(int meal, int eatingId);
    }

    private UpdateMenu updateMenu;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateMenu = (UpdateMenu) context;
    }
}
