package es.jmoral.mortadelo.modules.cbz;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.models.Comic;
import es.jmoral.mortadelo.modules.BaseExtractor;
import es.jmoral.mortadelo.utils.MD5;

/**
 * Created by owniz on 27/03/17.
 */

public class CbzExtractor extends BaseExtractor {
    public CbzExtractor(ComicReceivedListener comicReceivedListener) {
        super(comicReceivedListener);
    }

    @Override
    public void extractComic(@NonNull Context context, final String pathComic,
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
            boolean reverse = false;
            ZipInputStream zis = null;

            try {
                FileInputStream fis = new FileInputStream(strings[0]);
                zis = new ZipInputStream(fis);
                ZipEntry ze;
                long totalSize = fis.available();
                long elapsed = 0;
                int progress;
                boolean exists = false;

                comic.setMD5hash(MD5.calculateMD5(new File(strings[0])));

                File comicFolder = new File(context.getFilesDir() + "/" + comic.getMD5hash());

                if (!comicFolder.exists())
                    comicFolder.mkdirs();
                else
                    exists = true;

                int pageName = 0;

                while ((ze = zis.getNextEntry()) != null) {
                    if (ze.isDirectory()) {
                        reverse = true;
                        continue;
                    }

                    pages.add(context.getFilesDir() + "/" + comic.getMD5hash() + "/" + (pageName) + ".png");
                    if (!exists) {
                        FileOutputStream fos = new FileOutputStream(pages.get(pageName++));

                        for (int i = zis.read(); i != -1; i = zis.read()) {
                            fos.write(i);
                        }

                        zis.closeEntry();
                        fos.close();
                    }

                    elapsed += ze.getSize();
                    progress = (int) ((elapsed * 100) / totalSize);
                    publishProgress(progress);
                }

                if (pages.size() == 0) {
                    comicReceivedListener.onComicFailed("Empty comic.");
                    return null;
                }

                if (reverse)
                    Collections.reverse(pages);

                comic.setPages(pages);

            } catch (Exception e) {
                comicReceivedListener.onComicFailed(e.getMessage());
                return null;
            } finally {
                if (zis != null)
                    try {
                        zis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

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