package es.jmoral.mortadelo.modules.cbz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
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
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
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

                    if (reverse)
                        Collections.reverse(pages);

                    comic.setCover(pages.get(0));
                    comic.setPages(pages);

                } catch (IOException e) {
                    comicReceivedListener.onComicFailed();
                }

                comicReceivedListener.onComicReceived(comic);
            }
        });
    }
}
