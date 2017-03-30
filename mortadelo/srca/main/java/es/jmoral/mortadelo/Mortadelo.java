package es.jmoral.mortadelo;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.modules.BaseExtractor;
import es.jmoral.mortadelo.modules.cbr.CbrExtractor;
import es.jmoral.mortadelo.modules.cbz.CbzExtractor;

/**
 * Created by owniz on 27/03/17.
 */

public class Mortadelo {
    private enum ComicExt {
        CBR, CBZ, UNKNOWN;

        static ComicExt parseExt(String pathComic) {
            final String extension = pathComic.substring(pathComic.lastIndexOf(".") + 1);

            if (extension.length() == 0)
                return UNKNOWN;

            switch (extension) {
                case "cbr":
                case "rar":
                    return CBR;
                case "cbz":
                case "zip":
                    return CBZ;
                default:
                    return UNKNOWN;
            }
        }
    }

    private ComicReceivedListener comicReceivedListener;

    public Mortadelo(ComicReceivedListener comicReceivedListener) {
        this.comicReceivedListener = comicReceivedListener;
    }

    public void obtainComic(@NonNull String pathComic) {
        BaseExtractor extractor = null;

        switch (ComicExt.parseExt(pathComic)) {
            case CBR:
                extractor = new CbrExtractor(comicReceivedListener);
                break;
            case CBZ:
                extractor = new CbzExtractor(comicReceivedListener);
                break;
            case UNKNOWN:
                comicReceivedListener.onComicFailed();
                return;
        }

        extractor.extractComic(pathComic);
    }
}
