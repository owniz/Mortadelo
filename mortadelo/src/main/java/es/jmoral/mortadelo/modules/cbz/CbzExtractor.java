package es.jmoral.mortadelo.modules.cbz;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedOutputStream;
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
            ZipInputStream zis = null;

            try {
                FileInputStream fis = new FileInputStream(strings[0]);
                zis = new ZipInputStream(fis);
                ZipEntry ze;
                boolean exists = false;

                comic.setMD5hash(MD5.calculateMD5(new File(strings[0])));

                File comicFolder = new File(context.getFilesDir() + "/" + comic.getMD5hash());

                if (!comicFolder.exists())
                    comicFolder.mkdirs();
                else
                    exists = true;

                int pageNumber = 0;

                while ((ze = zis.getNextEntry()) != null) {
                    if (ze.isDirectory()) {
                        continue;
                    }

                    String[] pageName = ze.getName().split("/");
                    pages.add(context.getFilesDir() + "/" + comic.getMD5hash() + "/" + pageName[pageName.length - 1]);

                    if (!exists) {
                        FileOutputStream fos = new FileOutputStream(pages.get(pageNumber++));
                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                        byte[] data = new byte[1024];
                        int count;

                        while ((count = zis.read(data)) != -1) {
                            bos.write(data, 0, count);
                        }

                        bos.close();
                    }

                    publishProgress((int) (ze.getSize() / 1024)); // KiB
                }

                Collections.sort(pages);
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
    }
}