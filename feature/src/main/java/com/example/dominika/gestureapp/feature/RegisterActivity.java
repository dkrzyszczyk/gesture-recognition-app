package com.example.dominika.gestureapp.feature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

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

            //add entry to database
            String encoded = B64coder.encode(imageBitmap);
            GestureDB.Gesture gesture = new GestureDB.Gesture();
            gesture.setId(1);
            gesture.setFirstname(firstname.getText().toString());
            gesture.setLastname(lastname.getText().toString());
            gesture.setGesture(encoded);
            dbh.addGesture(gesture);
        }
    }
}
