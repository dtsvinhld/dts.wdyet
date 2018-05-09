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

public class EatingsAdapter extends ArrayAdapter<Eating> {
    public EatingsAdapter(@NonNull Context context, int resource, @NonNull List<Eating> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_eatings, null);

        final Eating eating = getItem(position);
        if(eating != null){
            Picasso.with(getContext()).load(Module.IMAGELINK + eating.getImage())
                    .into((ImageView) view.findViewById(R.id.img_ies));
            ((TextView) view.findViewById(R.id.txt_ies_name)).setText(eating.getName());
            ((TextView) view.findViewById(R.id.jT_ies_info)).setText(eating.getInfo());
        }
        return view;
    }
}
