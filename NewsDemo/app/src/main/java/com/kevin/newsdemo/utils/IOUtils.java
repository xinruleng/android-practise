package com.kevin.newsdemo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kevin on 2019/07/17 11:34.
 */
public class IOUtils {
    public static String getContent(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len = 0;

        try {
            while ((len = inputStream.read(buff)) > 0) {
                byteArrayOutputStream.write(buff, 0, len);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return byteArrayOutputStream.toString();
    }
}
