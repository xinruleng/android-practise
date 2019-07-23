package com.kevin.newsdemo.utils

import android.text.SpannableStringBuilder
import android.widget.EditText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Created by kevin on 2019/07/21 13:44.
 */
@RunWith(AndroidJUnit4::class)
class ViewUtilsTest {

    @Test
    fun should_getText() {
        val editText = mock(EditText::class.java)

        val expected = "123"
        `when`(editText.text).thenReturn(SpannableStringBuilder(expected))
        val out = ViewUtils.getText(editText)
        Assert.assertEquals(expected, out)
    }
}
