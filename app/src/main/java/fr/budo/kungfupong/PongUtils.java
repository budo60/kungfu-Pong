package fr.budo.kungfupong;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class PongUtils {
    public PongUtils(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
    }


}
