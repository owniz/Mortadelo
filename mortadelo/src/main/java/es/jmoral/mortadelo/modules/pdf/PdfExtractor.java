package es.jmoral.mortadelo.modules.pdf;

import android.content.Context;
import android.support.annotation.NonNull;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.models.Comic;
import es.jmoral.mortadelo.modules.BaseExtractor;

/**
 * Created by owniz on 6/05/17.
 */

public class PdfExtractor extends BaseExtractor {
    public PdfExtractor(ComicReceivedListener comicReceivedListener) {
        super(comicReceivedListener);
    }

    @Override
    public void extractComic(@NonNull Context context, String pathComic, ComicExtractionUpdateListener comicExtractionUpdateListener) {

    }

    private class ExtractionTask extends BaseAsyncTask {

        public ExtractionTask(@NonNull Context context, ComicExtractionUpdateListener comicExtractionUpdateListener) {
            super(context, comicExtractionUpdateListener);
        }

        @Override
        protected Comic doInBackground(String... strings) {
            return null;
        }
    }
}
