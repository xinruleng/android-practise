package com.kevin.newsdemo.base;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by kevin on 2019/07/17 13:48.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
