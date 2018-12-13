package fr.budo.kungfupong;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import static android.content.Context.AUDIO_SERVICE;

public class PongView extends View implements View.OnTouchListener {

    Paint mpaint;
    Paint raquette;
    Paint ennemy;
    float mX;
    float mY;
    int vX = 16;
    int vY = 8;
    int screenW;
    int screenH;
    int radius=40;
    int soundID;
    int EnnemyX;
    int myScore=0;
    int botScore=0;
    float finger;
    Intent loose;
    MediaPlayer player;
    Vibrator v;
    AudioManager audioManager;
    SoundPool laser;
    SmsManager sms;


    public PongView(Context context, int w, int h) {
        super(context);
        mpaint = new Paint();
        raquette = new Paint();
        ennemy = new Paint();

        screenW=w;
        screenH=h-250;

        initBall();

        this.setBackgroundResource(R.drawable.panda);

        v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        audioManager = (AudioManager) getContext().getSystemService(AUDIO_SERVICE);

        laser = new SoundPool(10,AudioManager.STREAM_MUSIC, 01);

        laser.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            }
        });

        soundID = laser.load(getContext(), R.raw.laser, 1);

        sms = SmsManager.getDefault();



        this.setOnTouchListener(this);
        loose = new Intent(getContext(), LooseActivity.class);
        //Log.d("dimz",""+screenW+"----"+screenH);

        player = MediaPlayer.create(getContext(), R.raw.kfufightin);
        player.start();




    }

    @Override
    public void onDraw(Canvas canvas) {
        
        canvas.drawCircle(mX, mY, radius, mpaint);
        mpaint.setColor(Color.rgb( 231,240,13));
        raquette.setColor(Color.rgb(126, 88, 53));
        ennemy.setColor(Color.rgb(126, 88, 53));
        raquette.setStrokeWidth(30);
        ennemy.setStrokeWidth(30);
        canvas.drawRect(finger-200, screenH-80, finger+200, screenH-10, raquette);
        EnnemyX=checkBallside();
        canvas.drawRect(EnnemyX-200, 35, EnnemyX+200, 95, ennemy);
        updateBall();

    }

    public int checkBallside() {
        int middle = screenH/2;
        if( mY < middle) {
            if(EnnemyX < mX) {
                EnnemyX+=Math.abs(vX)-5;
            } else {
                EnnemyX-=Math.abs(vX)-5;
            }
        } else {
            EnnemyX=middle;
        }
        return EnnemyX;

    }

    public void initBall() {
        if(botScore == 3) {

            sms.sendTextMessage("",null,"YOU loose n00b!", null , null);

            v.vibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}, -1);


            getContext().startActivity(loose);

            player.stop();
            botScore = 0;
            return;
        }
        mX=(float) (screenW/2)-radius;
        mY=(float) (screenH/2)-radius;
        vY=10;
        EnnemyX=200;
    }

    public boolean checkCollisionMe() {
        boolean var= (Math.abs(finger - mX) < 200 && (mY+radius >= screenH-80 && mY+radius <= screenH-10)) ? true:false;
        Log.d("test",""+var);
        return var;
    }

    public boolean checkCollisionEnnemy() {
        return (Math.abs(mX-EnnemyX) < 200 && (mY-radius >= 35 && mY-radius <= 95)) ? true:false;
    }

    public void updateBall() {

        if(checkCollisionMe()) {
            laser.play(soundID,0.5f,0.5f,1,0,1.0f);
            //laser.release();
            int y=Math.abs(vY)+1;
            vY = mY < 0 ? y : y * -1;
        } else if (checkCollisionEnnemy()){
            laser.play(soundID,0.5f,0.5f,1,0,1.0f);
            //laser.release();
            int y=Math.abs(vY)+2;
            vY = y;//mY < 0 ? y : y * -1;
        } else {
            if (mX < 0 || mX > screenW) {
                vX *= -1;
            }
            if(mY <0) {
                //vY*=-1;
                myScore++;
                initBall();
            }
            if(mY > screenH) {

                //getContext().startActivity(loose);
                //player.stop();
                //return;
                //vY*=-1;
                botScore++;
                initBall();

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
