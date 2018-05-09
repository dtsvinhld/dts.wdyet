package vn.com.dtsgroup.wdyet.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.com.dtsgroup.wdyet.Class.Eating;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

/**
 * Created by Verdant on 4/13/2018.
 */

public class EatingAdapterforMoW extends ArrayAdapter<Eating> {

    public interface ChangeItem{
        void onChangeItem(int type, int position, int changeType);
    }

    private ChangeItem changeItem;

    public EatingAdapterforMoW(@NonNull Context context, int resource, @NonNull List<Eating> objects, ChangeItem changeItem) {
        super(context, resource, objects);
        this.changeItem = changeItem;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_mow, null);
        }

        eating = getItem(position);
        if(eating != null){
            imageView = (ImageView) view.findViewById(R.id.img_item_mow);
            Picasso.with(getContext()).load(Module.IMAGELINK + eating.getImage()).into(imageView);
            textView = (TextView) view.findViewById(R.id.txt_name_item_mow);
            textView.setText(eating.getName());
            item = (LinearLayout) view.findViewById(R.id.item_mow_random);
            final int type = position == 0 ? 1 : 2;
            final boolean login = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getInt("id", 0) != 0;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(login){
                        item.setEnabled(false);
                        changeItem.onChangeItem(type, position, 0);
                    }
                    else{
                        Toast.makeText(getContext(), "Bạn phải đăng nhập để chỉnh sửa menu theo ý muốn", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            item1 = (LinearLayout) view.findViewById(R.id.item_mow_option);
            item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(login){
                        changeItem.onChangeItem(type, position, 1);
                    }
                    else{
                        Toast.makeText(getContext(), "Bạn phải đăng nhập để chỉnh sửa menu theo ý muốn", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return view;
    }

    private LinearLayout item, item1;
    private TextView textView;
    private Eating eating;
    private ImageView imageView;


//    class OptionEating extends AsyncTask<String, Integer, String>{
//        @Override
//        protected String doInBackground(String... strings) {
//            return Module.ContentURL(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//                JSONArray jsonArray = new JSONArray(s);
//                if(jsonArray.length() > 0){
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    if(jsonObject.getString("title").equals("Info")){
//                        JSONArray array = jsonObject.getJSONArray("info");
//                        eatings = new ArrayList<Eating>();
//                        eatingArrayList = new ArrayList<Eating>();
//                        for(int i = 0; i < array.length(); i++){
//                            JSONObject object = array.getJSONObject(i);
//                            eatings.add(new Eating(object.getInt("id"), object.getString("name"), object.getString("image")));
//                            eatingArrayList.add(new Eating(object.getInt("id"), object.getString("name"), object.getString("image")));
//                        }
//                        lv_option.setAdapter(new EatingOptionAdapter(
//                                getContext(),
//                                android.R.layout.simple_list_item_1,
//                                eatings
//                        ));
//
//                        dialog.show();
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            finally {
//                item1.setEnabled(true);
//            }
//        }
//    }

//    class RandomEating extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            return Module.ContentURL(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Toast.makeText(getContext(), "" + pos, Toast.LENGTH_SHORT).show();
//            try {
//                JSONArray jsonArray = new JSONArray(s);
//                if(jsonArray.length() > 0){
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    if(jsonObject.getString("title").equals("Info")){
//                        JSONObject object = jsonObject.getJSONObject("info");
//
//                        eating.setId(object.getInt("id"));
//                        eating.setName(object.getString("name"));
//                        eating.setImage(object.getString("image"));
//
//                        Picasso.with(getContext()).load(Module.IMAGELINK + eating.getImage()).into(imageView);
//                        textView.setText(eating.getName());
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//            finally {
//                item.setEnabled(true);
//            }
//        }
//    }
}
