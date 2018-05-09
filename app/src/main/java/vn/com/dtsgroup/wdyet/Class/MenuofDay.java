package vn.com.dtsgroup.wdyet.Class;

import java.util.ArrayList;

/**
 * Created by Verdant on 4/13/2018.
 */

public class MenuofDay {
    private ArrayList<Eating> eatings;

    public MenuofDay(ArrayList<Eating> eatings) {
        this.eatings = eatings;
    }

    public int size(){
        return eatings.size();
    }

    public ArrayList<Eating> getEatings() {
        return eatings;
    }

    public void setEatings(ArrayList<Eating> eatings) {
        this.eatings = eatings;
    }
}
