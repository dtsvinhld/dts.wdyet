package vn.com.dtsgroup.wdyet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.com.dtsgroup.wdyet.Activities.Eatings.EatingsActivity;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

/**
 * Created by Verdant on 4/16/2018.
 */

public class ETAdapter extends ArrayAdapter<ArrayList<ETAdapter.ET>> {

    private Context context;

    public ETAdapter(@NonNull Context context, int resource, @NonNull List<ArrayList<ETAdapter.ET>> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_et, null);

        ArrayList<ETAdapter.ET> ets = getItem(position);
        if(ets != null){
            if(ets.size() > 0){
                final ET et = ets.get(0);
                Picasso.with(getContext()).load(Module.IMAGELINK + et.getImage())
                        .into((ImageView)view.findViewById(R.id.img_item_et));
                ((TextView) view.findViewById(R.id.txt_item_et)).setText(et.getName());
                LinearLayout item = (LinearLayout) view.findViewById(R.id.item_et);
                final View finalView = view;
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), EatingsActivity.class);
                        intent.putExtra("key", "et");
                        intent.putExtra("type", et.getId());
                        intent.putExtra("name", et.getName());
                        context.startActivity(intent);
                    }
                });
            }

            if(ets.size() > 1){
                final ET et = ets.get(1);
                Picasso.with(getContext()).load(Module.IMAGELINK + et.getImage())
                        .into((ImageView)view.findViewById(R.id.img_item_et1));
                ((TextView) view.findViewById(R.id.txt_item_et1)).setText(et.getName());
                LinearLayout item = (LinearLayout) view.findViewById(R.id.item_et1);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), EatingsActivity.class);
                        intent.putExtra("key", "et");
                        intent.putExtra("type", et.getId());
                        intent.putExtra("name", et.getName());
                        getContext().startActivity(intent);
                    }
                });
            }
        }

        return view;
    }

    public static class ET{
        private int id;
        private String name, image;

        public ET(int id, String name, String image) {
            this.id = id;
            this.name = name;
            this.image = image;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
