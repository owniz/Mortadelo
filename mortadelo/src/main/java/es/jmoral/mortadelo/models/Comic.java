package es.jmoral.mortadelo.models;

import java.util.ArrayList;

/**
 * Created by owniz on 27/03/17.
 */

public class Comic {
    private ArrayList<String> pages;
    private String MD5hash;

    public ArrayList<String> getPages() {
        return pages;
    }

    public void setPages(ArrayList<String> pages) {
        this.pages = pages;
    }

    public String getMD5hash() {
        return MD5hash;
    }

    public void setMD5hash(String MD5hash) {
        this.MD5hash = MD5hash;
    }
}
