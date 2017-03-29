package es.jmoral.mortadelo_sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import es.jmoral.mortadelo.Mortadelo;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.models.Comic;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Mortadelo(new ComicReceivedListener() {
            @Override
            public void onComicReceived(Comic comic) {
                ((ImageView) findViewById(R.id.testCover)).setImageBitmap(comic.getPages().get(0));
            }

            @Override
            public void onComicFailed() {

            }
        }).obtainComic("/sdcard/Download/test.cbz");
    }
}
