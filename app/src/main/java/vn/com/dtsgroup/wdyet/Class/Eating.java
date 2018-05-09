package vn.com.dtsgroup.wdyet.Class;

/**
 * Created by Verdant on 4/13/2018.
 */

public class Eating {
    private int id;
    private String name, image, key, info;

    public Eating(int id, String name, String image, String info) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.info = info;
        this.key = Module.removeAccent(name);
    }

    public Eating(Eating eating){
        this.id = eating.id;
        this.name = eating.name;
        this.image = eating.image;
        this.key = eating.key;
        this.info = eating.info;
    }

    public Eating(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.key = Module.removeAccent(name);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getKey(){
        return key;
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
