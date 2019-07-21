package com.kevin.newsdemo.utils;

import android.text.SpannableStringBuilder;
import android.widget.EditText;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kevin on 2019/07/21 13:44.
 */
@RunWith(AndroidJUnit4.class)
public class ViewUtilsTest {

    @Test
    public void should_getText() {
        EditText editText = mock(EditText.class);

        final String expected = "123";
        when(editText.getText()).thenReturn(new SpannableStringBuilder(expected));
        String out = ViewUtils.getText(editText);
        Assert.assertEquals(expected, out);
    }
}
