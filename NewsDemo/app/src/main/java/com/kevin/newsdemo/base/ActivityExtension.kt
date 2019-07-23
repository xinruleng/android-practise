package com.kevin.newsdemo.base

import android.app.Activity
import android.widget.Toast

/**
 * Created by kevin on 2019/07/22 19:16.
 */
fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
