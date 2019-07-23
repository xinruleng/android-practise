package com.kevin.newsdemo.utils

import android.widget.EditText

/**
 * Created by kevin on 2019/07/17 10:48.
 */
object ViewUtils {
    fun getText(editText: EditText): String = editText.text.toString()
}
