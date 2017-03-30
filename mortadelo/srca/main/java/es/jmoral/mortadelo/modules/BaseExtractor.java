package es.jmoral.mortadelo.modules;

import es.jmoral.mortadelo.listeners.ComicReceivedListener;

/**
 * Created by owniz on 27/03/17.
 */

public abstract class BaseExtractor {
    protected ComicReceivedListener comicReceivedListener;

    public BaseExtractor(ComicReceivedListener comicReceivedListener) {
        this.comicReceivedListener = comicReceivedListener;
    }

    public abstract void extractComic(String pathComic);
}
