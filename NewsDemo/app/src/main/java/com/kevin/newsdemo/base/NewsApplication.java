package com.kevin.newsdemo.base;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;
import com.kevin.newsdemo.BuildConfig;

/**
 * Created by kevin on 2019/07/17 10:52.
 */
public class NewsApplication extends Application {
    private static final String TAG = "NewsApplication";

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: DEVELOPER_MODE=" + DEVELOPER_MODE);
        if (DEVELOPER_MODE) {

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
              .detectDiskReads()
              .detectDiskWrites()
              .detectNetwork()   // or .detectAll() for all detectable problems
              .penaltyLog()
              .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
              .detectLeakedSqlLiteObjects()
              .detectLeakedClosableObjects()
              .penaltyLog()
              .penaltyDeath()
              .build());
        }
        super.onCreate();
    }

    private static final boolean DEVELOPER_MODE = BuildConfig.DEBUG;
}

