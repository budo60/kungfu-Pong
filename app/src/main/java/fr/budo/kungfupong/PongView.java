package fr.budo.kungfupong;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class PongView extends View implements View.OnTouchListener {

    Paint mpaint;
    Paint raquette;
    int mX = 200;
    int mY = 300;
    int vX = 4;
    int vY = 10;
    int screenW;
    int screenH;
    float finger;
    Intent loose;
    MediaPlayer player;
    ImageView bg_view;

    public PongView(Context context, int w, int h) {
        super(context);
        mpaint = new Paint();
        raquette = new Paint();

        screenW=w;
        screenH=h-250;

        this.setBackgroundResource(R.drawable.panda);

        this.setOnTouchListener(this);
        loose = new Intent(getContext(), LooseActivity.class);
        //Log.d("dimz",""+screenW+"----"+screenH);

        player = MediaPlayer.create(getContext(), R.raw.kfufightin);
        player.start();




    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawCircle(mX, mY, 35, mpaint);
        mpaint.setColor(Color.rgb( 255,255,107));
        raquette.setColor(Color.rgb(222, 184, 135));
        raquette.setStrokeWidth(10);
        canvas.drawRect(finger-200, screenH-80, finger+200, screenH-10, raquette);
        updateBall();

    }

    public boolean checkCollision() {
        if(Math.abs(finger - mX) < 200 && (mY+35 >= screenH-80 && mY+35 <= screenH-10)) {
            return true;
        } else {
            return false;
        }
    }

    public void updateBall() {

        //finger-ball < abs(raquette/2)
        //y > ymin

        if(checkCollision()) {
           int y=Math.abs(vY)+2;
           vY = mY < 0 ? y : y * -1;
        } else {
            if (mX < 0 || mX > screenW) {
                vX *= -1;
                //vY+=5;
            }

            if(mY <0) {
                vY*=-1;
                //vX+=5;
            }
            if(mY > screenH) {

                getContext().startActivity(loose);
                player.stop();
                return;
            }
        }

        mX += vX;
        mY += vY;
        invalidate();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        finger = event.getX();
        return true;
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            //onResume called }
                player.start();
        } else {
                player.pause();
            }
        }



}
