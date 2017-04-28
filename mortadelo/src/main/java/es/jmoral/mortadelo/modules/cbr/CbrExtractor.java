package es.jmoral.mortadelo.modules.cbr;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.junrar.extract.ExtractArchive;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.models.Comic;
import es.jmoral.mortadelo.modules.BaseExtractor;
import es.jmoral.mortadelo.utils.MD5;

/**
 * Created by owniz on 27/03/17.
 */

public class CbrExtractor extends BaseExtractor {
    public CbrExtractor(ComicReceivedListener comicReceivedListener) {
        super(comicReceivedListener);
    }

    @Override
    public void extractComic(@NonNull Context context, String pathComic,
                             ComicExtractionUpdateListener comicExtractionUpdateListener) {
        new ExtractionTask(context, comicExtractionUpdateListener).execute(pathComic);
    }

    private class ExtractionTask extends AsyncTask<String, Integer, Comic> {
        private final Context context;
        private final ComicExtractionUpdateListener comicExtractionUpdateListener;

        ExtractionTask(@NonNull Context context, ComicExtractionUpdateListener comicExtractionUpdateListener) {
            this.context = context;
            this.comicExtractionUpdateListener = comicExtractionUpdateListener;
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        protected Comic doInBackground(String... strings) {
            ArrayList<String> pages = new ArrayList<>();
            Comic comic = new Comic();

            comic.setMD5hash(MD5.calculateMD5(new File(strings[0])));

            final File rar = new File(strings[0]);
            File comicFolder = new File(context.getFilesDir() + "/" + comic.getMD5hash());

            if (!comicFolder.exists())
                comicFolder.mkdirs();

            final File destinationFolder = new File(comicFolder.getAbsolutePath());
            ExtractArchive extractArchive = new ExtractArchive();
            extractArchive.extractArchive(rar, destinationFolder);

            File pagesNam = new File(comicFolder.getAbsolutePath());
            File[] listPages = pagesNam.listFiles();

            if (listPages[0].isDirectory()) {
                pagesNam = new File(listPages[0].getAbsolutePath());
                listPages = pagesNam.listFiles();
            }

            for (File file : listPages) {
                pages.add(file.getAbsolutePath());
            }

            Collections.sort(pages);
            comic.setPages(pages);

            return comic;
        }

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
