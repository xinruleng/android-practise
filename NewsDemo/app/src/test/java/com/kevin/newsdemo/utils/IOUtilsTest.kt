package com.kevin.newsdemo.utils

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream

/**
 * Created by kevin on 2019/07/21 13:42.
 */
class IOUtilsTest {
    @Test
    fun should_getContent() {
        val msg = "abc123"
        val `in` = ByteArrayInputStream(msg.toByteArray())
        val out = IOUtils.getContent(`in`)
        Assert.assertEquals(msg, out)
    }
}
