package com.example.dominika.gestureapp.feature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    public GestureDB.DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.dbh = new GestureDB.DBHelper(getApplicationContext(), "GestureDB", null,
                GestureDB.DBHelper.DB_VERSION);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView iv = findViewById(R.id.imageView);
            EditText firstname = findViewById(R.id.name);
            EditText lastname = findViewById(R.id.lastNameTxt);
            iv.setImageBitmap(imageBitmap);
            Bitmap binaryImage = toBinary(imageBitmap);
            Bitmap cropped = minimalRectangle(removeNoise(toGrayscale(binaryImage)));
            Pair<Integer,Integer> binaryResult = getMetric(cropped);

            //add entry to database
            String encoded = B64coder.encode(cropped);
            GestureDB.Gesture gesture = new GestureDB.Gesture();
            gesture.setId(UUID.randomUUID().toString());
            gesture.setFirstname(firstname.getText().toString());
            gesture.setLastname(lastname.getText().toString());
            gesture.setGesture(encoded);
            gesture.setWhitePixels(binaryResult.first);
            gesture.setBlacksPixels(binaryResult.second);
            dbh.addGesture(gesture);
        }
    }

    public Bitmap toBinary(Bitmap bmpOriginal) {
        int width, height, threshold;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        threshold = 127;
        Bitmap bmpBinary = bmpOriginal.copy(Bitmap.Config.ARGB_8888, true);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get one pixel color
                int pixel = bmpOriginal.getPixel(x, y);
                int red = Color.red(pixel);

                //get binary value
                if (red < threshold) {
                    bmpBinary.setPixel(x, y, Color.BLACK);
                } else {
                    bmpBinary.setPixel(x, y, Color.WHITE);
                }

            }
        }
        return bmpBinary;
    }

    public Bitmap removeNoise(Bitmap bmpOrginal){
        Bitmap image = bmpOrginal.copy(Bitmap.Config.RGB_565, true);
        Bitmap orig = bmpOrginal.copy(Bitmap.Config.RGB_565, true);
        // matrix of coeffients for lowpass filter
        double multiplier[][] = {{1.0/9,1.0/9,1.0/9}, {1.0/9,1.0/9,1.0/9}, {1.0/9,1.0/9,1.0/9}};
        int width, height;
        height = image.getHeight();
        width = image.getWidth();
        for (int x = 1; x < width-1; ++x) {
            for (int y = 1; y < height-1; ++y) {
                // get one pixel color
                int pixel = orig.getPixel(x, y);
                double color = multiplier[0][0]*orig.getPixel(x-1, y-1)+
                        multiplier[0][1]*orig.getPixel(x-1, y) +
                        multiplier[0][2]*orig.getPixel(x-1, y+1) +
                        multiplier[1][0]*orig.getPixel(x, y-1) +
                        multiplier[1][1]*orig.getPixel(x, y) +
                        multiplier[1][2]*orig.getPixel(x, y+1) +
                        multiplier[2][0]*orig.getPixel(x+1, y-1) +
                        multiplier[2][1]*orig.getPixel(x+1, y) +
                        multiplier[2][2]*orig.getPixel(x+1, y+1);

                image.setPixel(x,y,(int)color);
            }
        }
        return toGrayscale(image);
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public Pair getMetric(Bitmap image) {
        int black = 0;
        int white = 0;
        int width, height;
        height = image.getHeight();
        width = image.getWidth();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get one pixel color
                int pixel = image.getPixel(x, y);

                //get binary value
                if (pixel == Color.WHITE) {
                    white++;
                } else {
                    black++;
                }
            }
        }
        return new Pair(white, black);
    }

    public Bitmap minimalRectangle(Bitmap bmpOriginal) {
        int width, height, color;
        int minx, maxx, miny, maxy;

        width = bmpOriginal.getWidth();
        height = bmpOriginal.getHeight();
        minx = width;
        miny = height;
        maxx = 0;
        maxy = 0;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                color = bmpOriginal.getPixel(x, y);
                if(color == Color.WHITE && maxx <= x) {
                    maxx = x;
                }
                if(color == Color.WHITE && minx >= x) {
                    minx = x;
                }
                if(color == Color.WHITE && maxy <= y) {
                    maxy = y;
                }
                if(color == Color.WHITE && miny >= y) {
                    miny = y;
                }
            }
        }
        Log.d("RECT", "Minx = " +minx + ", miny = " + miny+ ", maxx = " + maxx + ", maxy = " + maxy);
        Bitmap result = Bitmap.createBitmap(bmpOriginal, minx, miny, maxx-minx, maxy-miny);
        return result;
    }

}
