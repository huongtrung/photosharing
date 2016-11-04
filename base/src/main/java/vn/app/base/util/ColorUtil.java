package vn.app.base.util;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.widget.SwitchCompat;

/**
 * Created by TuanDinh on 10/4/16.
 */

public class ColorUtil {
    public static void switchColor(boolean isChecked, SwitchCompat switchCompat) {
        if (Build.VERSION.SDK_INT >= 18) {
            int thumbColor;
            int trackColor;

            if(isChecked) {
                thumbColor = Color.rgb(74, 193, 254);
                trackColor = Color.rgb(74, 193, 254);
            } else {
                thumbColor = Color.rgb(74, 193, 254);
                trackColor = Color.rgb(74, 193, 254);
            }

            try {
                switchCompat.getThumbDrawable().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
                switchCompat.getTrackDrawable().setColorFilter(trackColor, PorterDuff.Mode.MULTIPLY);
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
