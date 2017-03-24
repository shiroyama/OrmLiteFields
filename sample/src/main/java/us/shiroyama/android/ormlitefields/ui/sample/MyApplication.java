package us.shiroyama.android.ormlitefields.ui.sample;

import android.app.Application;

import com.facebook.stetho.Stetho;

import timber.log.Timber;
import us.shiroyama.android.ormlitefields.BuildConfig;

/**
 * Custom {@link Application}
 *
 * @author Fumihiko Shiroyama
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

}
