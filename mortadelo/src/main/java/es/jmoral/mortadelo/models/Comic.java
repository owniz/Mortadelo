package es.jmoral.mortadelo.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by owniz on 27/03/17.
 */

public class Comic {
    private ArrayList<Bitmap> pages;

    public ArrayList<Bitmap> getPages() {
        return pages;
    }

    public void setPages(ArrayList<Bitmap> pages) {
        this.pages = pages;
    }
}
