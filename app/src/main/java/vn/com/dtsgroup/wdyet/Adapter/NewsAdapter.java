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

public class NewsAdapter extends ArrayAdapter<NewsAdapter.News> {
    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_news, null);
        }
        News news = getItem(position);
        if(news != null){
            ((TextView) view.findViewById(R.id.txt_title)).setText(news.getTitle());
            ((TextView) view.findViewById(R.id.txt_date)).setText(news.getDate());
            Picasso.with(getContext()).load(news.getImage()).into((ImageView) view.findViewById(R.id.img_news));
        }
        return view;
    }

    public static class News{
        private int id;
        private String title, image, date, link;

        public News(int id, String title, String image, String date, String link) {
            this.id = id;
            this.title = title;
            this.image = image;
            this.date = date;
            this.link = link;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
