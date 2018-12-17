package fr.budo.kungfupong;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

public class PongView extends View implements View.OnTouchListener, SensorEventListener {

    Paint mpaint;
    Paint raquette;
    Paint ennemy;
    Paint sbot,sme;
    float mX;
    float mY;
    int vX = 16;
    int vY = 8;
    int screenW;
    int screenH;
    int radius=40;
    int soundID;
    int EnnemyX;
    int myScore;
    int botScore;
    float finger;
    Intent loose;
    MediaPlayer player;
    Vibrator v;
    AudioManager audioManager;
    SoundPool laser;
    SmsManager sms;
    Bitmap bambooBas,bambooBasResized,bambooHaut,bambooHautResized, maBalle, maBalleResized;

    SensorManager sensorManager;
    //Sensor sensor;



    public PongView(Context context, int w, int h) {
        super(context);
        mpaint = new Paint();
        raquette = new Paint();
        ennemy = new Paint();
        sbot = new Paint();
        sme = new Paint();

        screenW=w;
        screenH=h-250;

        initBall();

        botScore = 0;
        myScore = 0;



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

        bambooBas = BitmapFactory.decodeResource(getResources(),R.drawable.bamboo4);
        bambooBasResized = Bitmap.createScaledBitmap(bambooBas, 400, 200, false);

        bambooHaut = BitmapFactory.decodeResource(getResources(),R.drawable.bamboo3);
        bambooHautResized = Bitmap.createScaledBitmap(bambooHaut, 400, 200, false);

        maBalle=BitmapFactory.decodeResource(getResources(), R.drawable.etoile);
        maBalleResized = Bitmap.createScaledBitmap(maBalle, 240, 240, false);



        this.setOnTouchListener(this);
        loose = new Intent(getContext(), LooseActivity.class);
        //Log.d("dimz",""+screenW+"----"+screenH);

        player = MediaPlayer.create(getContext(), R.raw.kfufightin);
        player.start();

        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);



        //Log.e("sensorTest", ""+sensor);




    }

    @Override
    public void onDraw(Canvas canvas) {
        
        //canvas.drawCircle(mX, mY, radius, mpaint);
        //mpaint.setColor(Color.rgb( 204,85,0));
        canvas.drawBitmap(maBalleResized,mX,mY,mpaint);

        raquette.setColor(Color.rgb(126, 88, 53));
        ennemy.setColor(Color.rgb(126, 88, 53));
        raquette.setStrokeWidth(30);
        ennemy.setStrokeWidth(30);

        sme.setColor(Color.rgb(255,255,255));
        sbot.setColor(Color.rgb(255,255,255));

        sme.setTextSize(100);
        sbot.setTextSize(100);
        canvas.drawText(""+myScore, 10, (screenH/2), sme);
        canvas.drawText(""+botScore, screenW-70, (screenH/2), sbot);
        //canvas.drawRect(finger-200, screenH-80, finger+200, screenH-10, raquette);
        //virtualBamboosBas = new Rect((int)finger-200, screenH-138, (int)finger+200, screenH-10);

        canvas.drawBitmap(bambooBasResized,finger-200, screenH-100, raquette);

        //canvas.drawBitmap(bambooBasResized,virtualBamboosBas,virtualBamboosBas,raquette);
        EnnemyX=checkBallside();
        canvas.drawBitmap(bambooHautResized,EnnemyX-200, -30, ennemy);
        //canvas.drawRect(EnnemyX-200, 35, EnnemyX+200, 95, ennemy);
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
        } else if (!((EnnemyX >= (screenW/2)-5) && (EnnemyX <= (screenW/2)+5))){
            if(EnnemyX < (screenW/2)) {
                EnnemyX+=Math.abs(vX)-5;
            } else {
                EnnemyX-=Math.abs(vX)-5;
            }

            //EnnemyX=(EnnemyX < (screenW/2)) ? EnnemyX++:EnnemyX--;

        }
        return EnnemyX;

    }

    public void initBall() {
        if(botScore == 3) {

            //sms.sendTextMessage("",null,"YOU loose n00b!", null , null);

            Toast.makeText(getContext(),"You Loose n00b!!",Toast.LENGTH_LONG).show();

            v.vibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}, -1);


            getContext().startActivity(loose);

            player.stop();
            botScore = 0;
            myScore = 0;
            return;
        } else if(myScore == 3) {
           // sms.sendTextMessage("",null,"YOU WIN Pr0!", null , null);

            Toast.makeText(getContext(),"WINNER WINNER CHICKEN DINNER",Toast.LENGTH_LONG).show();

            v.vibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}, -1);


            getContext().startActivity(loose);

            player.stop();
            botScore = 0;
            myScore = 0;
        }
        mX=(float) (screenW/2)-radius;
        mY=(float) (screenH/2)-radius;
        vY=10;
        EnnemyX=screenW/2;
    }

    public boolean checkCollisionMe() {
        boolean var= (Math.abs(finger - mX) < 200 && (mY+radius >= screenH-40 && mY+radius <= screenH-10)) ? true:false;
        //Log.d("test",""+var);
        return var;
    }

    public boolean checkCollisionEnnemy() {
        return (Math.abs(mX-EnnemyX) < 200 && (mY-radius >= 35 && mY-radius <= 95)) ? true:false;
    }

    public void updateBall() {

        if(checkCollisionMe()) {
            laser.play(soundID,0.8f,0.8f,1,0,1.0f);
            //laser.release();
            int y=Math.abs(vY)+1;
            vY = mY < 0 ? y : y * -1;
        } else if (checkCollisionEnnemy()){
            laser.play(soundID,0.8f,0.8f,1,0,1.0f);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            // Movement
            float x = values[0];
            //Log.e("testZzz", ""+x);

            if((x < -1) && ((finger + 200) < screenW)) {
                finger+=25;
            } else if ((x > 1) && ((finger-200) >= 0)) {
                finger -=25;
            }

            //invalidate();

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
