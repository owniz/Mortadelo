package es.jmoral.mortadelo.modules.cbz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.models.Comic;
import es.jmoral.mortadelo.modules.BaseExtractor;

/**
 * Created by owniz on 27/03/17.
 */

public class CbzExtractor extends BaseExtractor {
    public CbzExtractor(ComicReceivedListener comicReceivedListener) {
        super(comicReceivedListener);
    }

    @Override
    public void extractComic(final String pathComic) {
        ArrayList<Bitmap> pages = new ArrayList<>();
        Comic comic = new Comic();
        boolean reverse = false;
        try {
            FileInputStream fis = new FileInputStream(pathComic);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze;

            while ((ze = zis.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    reverse = true;
                    continue;
                }

                pages.add(BitmapFactory.decodeStream(zis));
            }

            if (pages.size() == 0) {
                comicReceivedListener.onComicFailed("Empty comic.");
                return;
            }

            if (reverse)
                Collections.reverse(pages);

            comic.setPages(pages);

        } catch (Exception e) {
            comicReceivedListener.onComicFailed(e.getMessage());
            return;
        }

        comicReceivedListener.onComicReceived(comic);
    }
}
