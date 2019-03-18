package es.jmoral.mortadelo;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.modules.BaseExtractor;
import es.jmoral.mortadelo.modules.cbr.CbrExtractor;
import es.jmoral.mortadelo.modules.cbz.CbzExtractor;

/**
 * Created by owniz on 27/03/17.
 */

public class Mortadelo {
    private final Context context;
    private ComicReceivedListener comicReceivedListener;
    private final ComicExtractionUpdateListener comicExtractionUpdateListener;

    private enum ComicExt {
        CBR, CBZ, UNKNOWN;

        static ComicExt parseMagicNumber(String pathComic) {
            byte[] buffer = new byte[7];
            InputStream is = null;
            ComicExt comicExt = UNKNOWN;
            try {
                is = new FileInputStream(pathComic);

                if (is.read(buffer) >= buffer.length) {
                    String stringHex = bytesToHex(buffer).toUpperCase();
                    if (stringHex.startsWith("504B0304"))
                        comicExt = CBZ;
                    else if (stringHex.startsWith("526172211A0700"))
                        comicExt = CBR;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return comicExt.equals(UNKNOWN) ? parseExt(pathComic) : comicExt;
        }

        private static String bytesToHex(byte[] in) {
            final StringBuilder builder = new StringBuilder();
            for(byte b : in) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        }

        static ComicExt parseExt(String pathComic) {

            final String extension = pathComic.substring(pathComic.lastIndexOf(".") + 1).toLowerCase();

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

    public Mortadelo(@NonNull Context context, ComicReceivedListener comicReceivedListener,
                     ComicExtractionUpdateListener comicExtractionUpdateListener) {
        this.context = context;
        this.comicReceivedListener = comicReceivedListener;
        this.comicExtractionUpdateListener = comicExtractionUpdateListener;
    }

    public void obtainComic(@NonNull String pathComic) {
        BaseExtractor extractor;

        switch (ComicExt.parseMagicNumber(pathComic)) {
            case CBR:
                extractor = new CbrExtractor(comicReceivedListener);
                break;
            case CBZ:
                extractor = new CbzExtractor(comicReceivedListener);
                break;
            case UNKNOWN:
                comicReceivedListener.onComicFailed("Unknown file type.");
                return;
            default:
                return;
        }

        extractor.extractComic(context, pathComic, comicExtractionUpdateListener);
    }
}
