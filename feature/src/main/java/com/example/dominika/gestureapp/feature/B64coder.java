package com.example.dominika.gestureapp.feature;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by dominika on 31.10.17.
 */

public class B64coder {
    public static String encode(Bitmap bm) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    public static Bitmap decode(String s) {
        byte[] byteArray = Base64.decode(s, Base64.DEFAULT);
        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bm;
    }
}
