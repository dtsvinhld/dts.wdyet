package vn.com.dtsgroup.wdyet.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.dtsgroup.wdyet.Activities.Eatings.EatingActivity;
import vn.com.dtsgroup.wdyet.Activities.Result.ResultActivity;
import vn.com.dtsgroup.wdyet.Adapter.NotifyAdapter;
import vn.com.dtsgroup.wdyet.Class.Eating;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

/**
 * Created by Verdant on 4/18/2018.
 */

public class NotifyFragment extends Fragment {

    private ListView listView;
    private ArrayList<NotifyAdapter.Notify> notifyArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
        listView = (ListView) view.findViewById(R.id.lv_notify);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetNotify().execute(Module.DOMAINNAME + "notify/"
                        + getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getInt("id", 0));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), EatingActivity.class);
                intent.putExtra("id", notifyArrayList.get(position).getEatingId());
                intent.putExtra("name", notifyArrayList.get(position).getName());
                startActivity(intent);
            }
        });

        return view;
    }

    class GetNotify extends AsyncTask<String, Integer, String>{
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
                        notifyArrayList = new ArrayList<NotifyAdapter.Notify>();
                        for(int i=0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            notifyArrayList.add(new NotifyAdapter.Notify(object.getString("title"),
                                    object.getString("time"), object.getString("icon"),
                                    object.getString("name"), object.getInt("eatingId")));
                        }
                        listView.setAdapter(new NotifyAdapter(
                                getContext(),
                                android.R.layout.simple_list_item_1,
                                notifyArrayList
                        ));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
