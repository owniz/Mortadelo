package es.jmoral.mortadelo.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;

/**
 * Created by owniz on 27/03/17.
 */

public abstract class BaseExtractor {
    protected ComicReceivedListener comicReceivedListener;

    public BaseExtractor(ComicReceivedListener comicReceivedListener) {
        this.comicReceivedListener = comicReceivedListener;
    }

    public abstract void extractComic(@NonNull Context context, String pathComic,
                                      ComicExtractionUpdateListener comicExtractionUpdateListener);
}
