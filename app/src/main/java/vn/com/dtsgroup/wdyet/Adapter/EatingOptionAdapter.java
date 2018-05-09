package vn.com.dtsgroup.wdyet.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.com.dtsgroup.wdyet.Class.Eating;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

/**
 * Created by Verdant on 4/16/2018.
 */

public class EatingOptionAdapter extends ArrayAdapter<Eating>{
    public EatingOptionAdapter(@NonNull Context context, int resource, @NonNull List<Eating> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_option, null);
        }
        Eating eating = getItem(position);
        if(eating != null){
            Picasso.with(getContext()).load(Module.IMAGELINK + eating.getImage()).into((ImageView) view.findViewById(R.id.img_eating_option));
            ((TextView) view.findViewById(R.id.txt_eating_option)).setText(eating.getName());
        }
        return view;
    }
}
