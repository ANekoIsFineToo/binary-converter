package es.ikerperez.binaryconverter;

import android.app.Application;

import timber.log.Timber;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 01/10/2016.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
