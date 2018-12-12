package fr.budo.kungfupong;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int width = display.getWidth();
        int height = display.getHeight();
        PongView Game=new PongView(this,width ,height );
        super.onCreate(savedInstanceState);
        setContentView(Game);



        //Log.d("test" ,""+height);


    }
}


