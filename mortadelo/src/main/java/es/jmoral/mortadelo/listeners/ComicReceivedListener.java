package es.jmoral.mortadelo.listeners;

import es.jmoral.mortadelo.models.Comic;

/**
 * Created by owniz on 27/03/17.
 */

public interface ComicReceivedListener {
    void onComicReceived(Comic comic);
    void onComicFailed();
}
