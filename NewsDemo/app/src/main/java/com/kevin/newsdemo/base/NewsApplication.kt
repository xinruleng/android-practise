package com.kevin.newsdemo.base

import android.app.Application
import android.util.Log
import com.kevin.newsdemo.BuildConfig

/**
 * Created by kevin on 2019/07/17 10:52.
 */
class NewsApplication : Application() {

    override fun onCreate() {
        Log.d(TAG, "onCreate: DEVELOPER_MODE=$DEVELOPER_MODE")
        // TODO: 2019-07-21
        //[Robolectric] com.kevin.newsdemo.user.ui.LoginActivityJvmTest.should_start_UserInfoActivity_when_login_succeed: sdk=28; resources=BINARY
        //StrictMode VmPolicy violation with POLICY_DEATH; shutting down.
        //        if (DEVELOPER_MODE) {
        //
        //            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        //              .detectDiskReads()
        //              .detectDiskWrites()
        //              .detectNetwork()   // or .detectAll() for all detectable problems
        //              .penaltyLog()
        //              .build());
        //            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        //              .detectLeakedSqlLiteObjects()
        //              .detectLeakedClosableObjects()
        //              .penaltyLog()
        //              .penaltyDeath()
        //              .build());
        //        }
        super.onCreate()
    }

    companion object {
        private val TAG = "NewsApplication"

        private val DEVELOPER_MODE = BuildConfig.DEBUG
    }
}

