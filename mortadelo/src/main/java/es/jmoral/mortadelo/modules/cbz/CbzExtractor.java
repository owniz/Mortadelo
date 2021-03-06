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
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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
                int elapsed = 0;
                boolean exists = false;

                comic.setMD5hash(MD5.calculateMD5(new File(strings[0])));

                File comicFolder = new File(context.getFilesDir() + "/" + comic.getMD5hash());

                if (!comicFolder.exists())
                    comicFolder.mkdirs();
                else
                    exists = true;

                int pageNumber = 0;

                ZipFile zipFile = new ZipFile(strings[0]);
                Enumeration entries = zipFile.entries();

                while (entries.hasMoreElements()) {
                    ze = (ZipEntry) entries.nextElement();
                    zis.getNextEntry();
                    if (ze.isDirectory()) {
                        continue;
                    }

                    String[] pageName = ze.getName().split("/");
                    pages.add(context.getFilesDir() + "/" + comic.getMD5hash() + "/" + pageName[pageName.length - 1]);

                    if (!exists) {
                        FileOutputStream fos = new FileOutputStream(pages.get(pageNumber++));
                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                        byte[] data = new byte[4096];
                        int count;

                        while ((count = zis.read(data)) != -1) {
                            bos.write(data, 0, count);
                        }

                        bos.close();
                    }
                    elapsed += (int) (ze.getSize() / 1024);
                    publishProgress(elapsed); // KiB
                }

                Collections.sort(pages);
                comic.setPages(pages);

            } catch (Exception e) {
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