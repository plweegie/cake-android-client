package com.waracle.androidtest;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Riad on 20/05/2015.
 */
public class StreamUtils {
    private static final String TAG = StreamUtils.class.getSimpleName();

    // Can you see what's wrong with this???
    // Extremely inefficient - reads one byte at a time
    public static byte[] readUnknownFully(InputStream stream) throws IOException {

        //Better approach: use output stream with a buffer of predetermined size
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = stream.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();
        return bos.toByteArray();
    }

    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    public static String parseCharset(String contentType) {
        if (contentType != null) {
            String[] params = contentType.split(",");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }
        return "UTF-8";
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
