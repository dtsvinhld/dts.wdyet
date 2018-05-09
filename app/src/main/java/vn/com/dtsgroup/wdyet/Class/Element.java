package vn.com.dtsgroup.wdyet.Class;

/**
 * Created by Verdant on 4/16/2018.
 */

public class Element {
    private String name, donvi;
    private int soluong;

    public Element(String name, int soluong, String donvi) {
        this.name = name;
        this.soluong = soluong;
        this.donvi = donvi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
