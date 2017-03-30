package es.jmoral.mortadelo_sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import es.jmoral.mortadelo.Mortadelo;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.models.Comic;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1337;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (verifyStoragePermissions())
            readComic();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean verifyStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }

        return true;
    }

    public void readComic() {
        new Mortadelo(new ComicReceivedListener() {
            @Override
            public void onComicReceived(Comic comic) {
                ((ImageView) findViewById(R.id.testCover)).setImageBitmap(comic.getPages().get(0));
            }

            @Override
            public void onComicFailed(String message) {
                Log.d("hola", message);
            }
        }).obtainComic(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/test.cbz");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readComic();
                }
                break;
        }
    }
}
