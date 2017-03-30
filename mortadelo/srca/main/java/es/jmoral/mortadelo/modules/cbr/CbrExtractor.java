package es.jmoral.mortadelo.modules.cbr;

import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.modules.BaseExtractor;

/**
 * Created by owniz on 27/03/17.
 */

public class CbrExtractor extends BaseExtractor {
    public CbrExtractor(ComicReceivedListener comicReceivedListener) {
        super(comicReceivedListener);
    }

    @Override
    public void extractComic(String pathComic) {
        
    }
}
