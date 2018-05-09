package vn.com.dtsgroup.wdyet.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Verdant on 4/13/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private ArrayList<Integer> mIcons;
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitles;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        mContext = context;
        mIcons = new ArrayList<>();
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mIcons.get(position) != 0){
            Drawable image = ContextCompat.getDrawable(mContext, mIcons.get(position));
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
        return mTitles.get(position);
    }

    public void addFragmentPager(Fragment newFragmentPager, String title/*, int icon*/) {
        mFragments.add(newFragmentPager);
        mTitles.add(title);
        mIcons.add(0);
    }

    public void addFragmentPager(Fragment newFragmentPager, String title, int icon) {
        mFragments.add(newFragmentPager);
        mTitles.add(title);
        mIcons.add(icon);
    }

    public String getTitle(int position) {
        return mTitles.get(position);
    }
}
