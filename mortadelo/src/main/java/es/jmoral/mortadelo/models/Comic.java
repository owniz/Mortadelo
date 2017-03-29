package es.jmoral.mortadelo.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by owniz on 27/03/17.
 */

public class Comic {
    private Bitmap cover;
    private ArrayList<Bitmap> pages;

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public ArrayList<Bitmap> getPages() {
        return pages;
    }

    public void setPages(ArrayList<Bitmap> pages) {
        this.pages = pages;
    }
}
