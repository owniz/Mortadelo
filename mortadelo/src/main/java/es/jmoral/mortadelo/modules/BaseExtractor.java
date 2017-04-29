package es.jmoral.mortadelo.modules;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.models.Comic;
import es.jmoral.mortadelo.utils.MD5;

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

    public abstract class BaseAsyncTask extends AsyncTask<String, Integer, Comic> {
        protected final Context context;
        private final ComicExtractionUpdateListener comicExtractionUpdateListener;

        public BaseAsyncTask(@NonNull Context context, ComicExtractionUpdateListener comicExtractionUpdateListener) {
            this.context = context;
            this.comicExtractionUpdateListener = comicExtractionUpdateListener;
        }

        @Override
        protected abstract Comic doInBackground(String... strings);

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            comicExtractionUpdateListener.onExtractionUpdate(values[0]);
        }

        @Override
        protected void onPostExecute(Comic comic) {
            super.onPostExecute(comic);
            comicReceivedListener.onComicReceived(comic);
        }
    }
}
