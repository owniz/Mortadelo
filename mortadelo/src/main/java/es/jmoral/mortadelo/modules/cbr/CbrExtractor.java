package es.jmoral.mortadelo.modules.cbr;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.models.Comic;
import es.jmoral.mortadelo.modules.BaseExtractor;
import es.jmoral.mortadelo.utils.MD5;
import junrar.UnrarCallback;
import junrar.Volume;
import junrar.extract.RarExtractor;

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

    private class ExtractionTask extends BaseAsyncTask {

        ExtractionTask(@NonNull Context context, ComicExtractionUpdateListener comicExtractionUpdateListener) {
            super(context, comicExtractionUpdateListener);
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
            try {
                new RarExtractor().extractArchive(rar, destinationFolder, new UnrarCallback() {
                    @Override
                    public boolean isNextVolumeReady(Volume volume) {
                        return false;
                    }

                    @Override
                    public void volumeProgressChanged(long current, long total) {
                        publishProgress((int) (current / 1024)); // KiB
                    }
                });
            } catch (Exception e) {
                return null;
            }

            File[] listPages = new File(comicFolder.getAbsolutePath()).listFiles();

            if (listPages[0].isDirectory())
                listPages = new File(listPages[0].getAbsolutePath()).listFiles();

            for (File file : listPages) {
                pages.add(file.getAbsolutePath());
            }

            Collections.sort(pages);
            comic.setPages(pages);

            return comic;
        }
    }
}
