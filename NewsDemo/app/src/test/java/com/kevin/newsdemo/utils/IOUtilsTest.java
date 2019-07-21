package com.kevin.newsdemo.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by kevin on 2019/07/21 13:42.
 */
public class IOUtilsTest {
    @Test
    public void should_getContent() {
        String msg = "abc123";
        InputStream in = new ByteArrayInputStream(msg.getBytes());
        String out = IOUtils.getContent(in);
        Assert.assertEquals(msg, out);
    }
}
