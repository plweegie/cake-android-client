package com.waracle.androidtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    /**
     * Simple function for loading a bitmap image from the web
     *
     * @param url image url
     */
    public static Bitmap loadBitmapFromUrl(String url) {

        if (TextUtils.isEmpty(url)) {
            throw new InvalidParameterException("URL is empty!");
        }

        final Bitmap targetBitmap = getBitmapFromCache(url);

        if (targetBitmap != null) {
            return targetBitmap;
        } else {
            byte[] data = null;

            try {
                data = loadImageData(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap output = convertToBitmap(data);

            if (output != null) {
                addBitmapToCache(url, output);
            }
            return output;
        }
    }

    private static byte[] loadImageData(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cache-Control", "private, max-age=86400");
        InputStream inputStream = null;
        try {
            try {
                // Read data from workstation
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                // Read the error from the workstation
                inputStream = connection.getErrorStream();
            }

            // Can you think of a way to make the entire
            // HTTP more efficient using HTTP headers??

            return StreamUtils.readUnknownFully(inputStream);
        } finally {
            // Close the input stream if it exists.
            StreamUtils.close(inputStream);

            // Disconnect the connection
            connection.disconnect();
        }
    }

    private static Bitmap convertToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private static void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            BitmapCache.INSTANCE.put(key, bitmap);
        }
    }

    private static Bitmap getBitmapFromCache(String key) {
        return BitmapCache.INSTANCE.get(key);
    }

}

