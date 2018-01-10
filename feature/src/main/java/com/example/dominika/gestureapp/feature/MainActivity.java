package com.example.dominika.gestureapp.feature;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public GestureDB.DBHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.dbh = new GestureDB.DBHelper(getApplicationContext(), "GestureDB", null,
                GestureDB.DBHelper.DB_VERSION);
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToGallery(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    public void clearDB(View view) {
        dbh.deleteAll();
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Wyczyszczono bazę danych!");
        dlgAlert.setTitle("KOMUNIKAT");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void goToAuthor(View view) {
        Intent intent = new Intent(this, AuthorActivity.class);
        startActivity(intent);
    }

    public void goToRules(View view) {
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }
}
