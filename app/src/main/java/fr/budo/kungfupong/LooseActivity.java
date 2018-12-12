package fr.budo.kungfupong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LooseActivity extends Activity {

    TextView tv;
    Intent retry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        retry = new Intent(this, MainActivity.class);
       // setContentView(Game);
        tv = new TextView(this);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tv.setText("Game Over");
        setContentView(tv);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseContext().startActivity(retry);
                return;

            }
        });
    }
}