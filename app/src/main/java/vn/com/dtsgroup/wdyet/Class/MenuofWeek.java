package vn.com.dtsgroup.wdyet.Class;

import java.util.ArrayList;

/**
 * Created by Verdant on 4/13/2018.
 */

public class MenuofWeek {
    private int id;
    private ArrayList<MenuofDay> menuofDays;

    public MenuofWeek(int id, ArrayList<MenuofDay> menuofDays) {
        this.menuofDays = menuofDays;
        this.id = id;
    }

    public int days(){
        return menuofDays.size();
    }

    public int meals(){
        return menuofDays.get(0).size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<MenuofDay> getMenuofDays() {
        return menuofDays;
    }

    public void setMenuofDays(ArrayList<MenuofDay> menuofDays) {
        this.menuofDays = menuofDays;
    }
}
