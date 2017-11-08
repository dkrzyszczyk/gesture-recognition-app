package com.example.dominika.gestureapp.feature;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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
            TextView result = new TextView(getApplicationContext());
            CustomImageView blurredImage = new CustomImageView(getApplicationContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

            firstNameTv.setLayoutParams(params);
            lastNameTv.setLayoutParams(params);
            firstNameTv.setText(g.firstname);
            lastNameTv.setText(g.lastname);
            lastNameTv.setGravity(Gravity.CENTER_HORIZONTAL);
            Bitmap image = (B64coder.decode(g.gesture));

            blurredImage.setImageBitmap(image);
            blurredImage.setUuid(g.id);
//            blurredImage.setOnClickListener(new View.OnClickListener() {
//                boolean delete = false;
//                @Override
//                public void onClick(View view) {
//                    new AlertDialog.Builder(this)
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setTitle("KOMUNIKAT")
//                            .setMessage("Czy na pewno chcesz usunąć zdjęcie z bazy danych?")
//                            .setPositiveButton("Tak", new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    this.delete = true;
//                                }
//
//                            })
//                            .setNegativeButton("Nie", null)
//                            .show();
//                }
//            });
            Float first = (float)g.whitePixels;
            Float second = (float)g.blackPixels;
            float ratio = first/second;
            result.setText(""+ratio);

            internalLayout.addView(firstNameTv);
            internalLayout.addView(lastNameTv);
            internalLayout.addView(result);
            internalLayout.addView(blurredImage);
            lay.addView(internalLayout);
        }
    }


}
