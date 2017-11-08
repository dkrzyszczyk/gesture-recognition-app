package com.example.dominika.gestureapp.feature;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by dominika on 08.11.17.
 */

public class CustomImageView extends ImageView {
    public String uuid;
    public void setUuid(String uuid) {
        this.uuid = uuid;
    };
    public CustomImageView(Context context) {
        super(context);
    }

}
