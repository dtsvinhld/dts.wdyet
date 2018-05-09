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

import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

/**
 * Created by Verdant on 4/18/2018.
 */

public class NotifyAdapter extends ArrayAdapter<NotifyAdapter.Notify> {
    public NotifyAdapter(@NonNull Context context, int resource, @NonNull List<Notify> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null)    view = LayoutInflater.from(getContext()).inflate(R.layout.item_notify, null);

        Notify notify = getItem(position);
        if(notify != null){
            Picasso.with(getContext()).load(Module.IMAGELINK + notify.getIcon())
                    .into((ImageView) view.findViewById(R.id.img_notify));
            ((TextView) view.findViewById(R.id.txt_notify_title)).setText(notify.getTitle());
            ((TextView) view.findViewById(R.id.txt_notify_time)).setText(notify.getTime());
        }
        return view;
    }

    public static class Notify{
        private String title, time, icon, name;
        private int eatingId;

        public Notify(String title, String time, String icon, String name, int eatingId) {
            this.title = title;
            this.time = time;
            this.icon = icon;
            this.name = name;
            this.eatingId = eatingId;
        }

        public int getEatingId() {
            return eatingId;
        }

        public void setEatingId(int eatingId) {
            this.eatingId = eatingId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
