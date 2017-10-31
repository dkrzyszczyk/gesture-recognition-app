package com.example.dominika.gestureapp.feature;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class GalleryActivity extends AppCompatActivity {


    public GestureDB.DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Log.d("cos", "dladominikiblah");
        this.dbh = new GestureDB.DBHelper(getApplicationContext(), "GestureDB", null,
                GestureDB.DBHelper.DB_VERSION);
        // get all gestures
        List<GestureDB.Gesture> gestures = dbh.getAllGestures();

        LinearLayout lay = findViewById(R.id.galleryLayout);
        for(GestureDB.Gesture g: gestures) {
            LinearLayout internalLayout = new LinearLayout(getApplicationContext());
            internalLayout.setOrientation(LinearLayout.HORIZONTAL);
            internalLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));

            TextView firstNameTv = new TextView(getApplicationContext());
            TextView lastNameTv = new TextView(getApplicationContext());
            ImageView imageIv = new ImageView(getApplicationContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

            firstNameTv.setLayoutParams(params);
            lastNameTv.setLayoutParams(params);
            imageIv.setLayoutParams(params);
            firstNameTv.setText(g.firstname);
            lastNameTv.setText(g.lastname);
            lastNameTv.setGravity(Gravity.CENTER_HORIZONTAL);
            imageIv.setImageBitmap(B64coder.decode(g.gesture));

            internalLayout.addView(firstNameTv);
            internalLayout.addView(lastNameTv);
            internalLayout.addView(imageIv);
            lay.addView(internalLayout);
        }
    }
}
