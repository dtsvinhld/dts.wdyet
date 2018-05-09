package vn.com.dtsgroup.wdyet.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import vn.com.dtsgroup.wdyet.R;

/**
 * Created by Verdant on 4/20/2018.
 */

public class RatingAdapter extends ArrayAdapter<RatingAdapter.Rate> {

    public RatingAdapter(@NonNull Context context, int resource, @NonNull List<Rate> objects) {
        super(context, resource, objects);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_rating, null);

        Rate rate = getItem(position);
        if(rate != null){
            ((TextView) view.findViewById(R.id.txt_fullname_rate)).setText(rate.getFullname());
            ((TextView) view.findViewById(R.id.txt_date_rate)).setText(rate.getDate());
            ((TextView) view.findViewById(R.id.txt_comment_rate)).setText(rate.getComment());

            MaterialRatingBar ratingBar = view.findViewById(R.id.ratingBar1);
            final float rat = (float) rate.getRating();
            ratingBar.setRating(rat);
            ratingBar.setFocusable(false);
        }
        return view;
    }

    public static class Rate{
        private int id;
        private String fullname,date, comment;
        private int rating;

        public Rate(int id, String fullname, String date, String comment, int rating) {
            this.id = id;
            this.fullname = fullname;
            this.date = date;
            this.comment = comment;
            this.rating = rating;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }
    }
}
