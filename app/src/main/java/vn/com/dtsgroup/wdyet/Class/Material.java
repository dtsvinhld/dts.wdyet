package vn.com.dtsgroup.wdyet.Class;

import java.util.ArrayList;

/**
 * Created by Verdant on 4/16/2018.
 */

public class Material {
    private int id;
    private String name, image, key;
    private ArrayList<Element> elements;

    public Material(int id, String name, String image, ArrayList<Element> elements) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.elements = elements;
        this.key = Module.removeAccent(name);
    }

    public String getKey() {
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

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }
}
