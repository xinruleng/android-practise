package com.kevin.newsdemo.utils

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by kevin on 2019/07/17 11:34.
 */
object IOUtils {
    fun getContent(inputStream: InputStream): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buff = ByteArray(4096)
        var len = 0

        try {
            while (true) {
                len = inputStream.read(buff)
                if (len <= 0) {
                    break
                }
                byteArrayOutputStream.write(buff, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return byteArrayOutputStream.toString()
    }
}
