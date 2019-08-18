package com.rightteam.accountbook.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by JasonWu on 28/12/2017
 */

public class AssetsHelper {

    private AssetsHelper() {
        throw new AssertionError();
    }

    public static String readData(Context context, String fileName) {
        InputStream inputStream;
        String data = null;
        try {
            inputStream = context.getAssets().open(fileName);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            data = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}