package gg.rich.damienspots;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
//sound test code

import android.content.res.AssetFileDescriptor;
import android.content.Context;
import android.app.Activity;

//sound test

import android.media.SoundPool;
import android.media.AudioManager;

import gg.rich.damienspots.sketch.Dot;
import processing.core.*;

/**
 * Created by Rich on 09/03/2016.
 * WTF ?
 */

public class DamienDots_Main extends PApplet {
    //sound test code
    SoundPool soundPool;
    HashMap<Object, Object> soundPoolMap;
    Activity act;
    Context cont;
    AssetFileDescriptor afd1, afd2, afd3;
    int s1, s2, s3;
    int streamId1, streamId2, streamId3;
    int stopPlay3; //stop playing sound3 4217ms


    /*
    Minim minim;
    //AudioSample chomp;
    AudioPlayer player1; // chomp
    AudioPlayer player2; // intro
    AudioPlayer player3; // siren
    */
    boolean doOnce = false;

    int bgColor = color(255);

    float angleBouche = 0f;

    int startTime;
    int stopTime;
    int duration = 200;

    boolean ON;
    boolean ON2;
    boolean up;
    boolean soundOn;
    //states
    int INTRO = 0;
    int SPOTS = 1;
    int PACMAN = 2;
    int state = 0;

    int col;
    int [] colarray = new int [36];

    int pSize ;
    int pointNum;

    //create arrayList
    ArrayList<Dot> dots;

    public float pacX = 0f;
    public float pacY = pSize;
    float pacSpeed ;
    float alpha = 0f;
    boolean finished = false;
    //
    int pacButtonIndex = 0;
    int touchDotIndex = -1;

    @Override
    public void settings() {
        fullScreen();
    }

    @Override
    public void setup() {

        dots = new ArrayList<Dot>();
        //sound test code
        act = this.getActivity();
        cont = act.getApplicationContext();

        // load up the files
        try {

            afd1 = cont.getAssets().openFd("chomp.mp3");
            afd2 = cont.getAssets().openFd("siren.mp3");
            afd3 = cont.getAssets().openFd("beginning.mp3");


        }
        catch(IOException e) {
            println("error loading files:" + e);
        }

        // create the soundPool HashMap - apparently this constructor is now depracated?
        soundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap = new HashMap<Object, Object>(3);
        soundPoolMap.put(s1, soundPool.load(afd1, 1));
        soundPoolMap.put(s2, soundPool.load(afd2, 1));
        soundPoolMap.put(s3, soundPool.load(afd3, 1));
        /*
        minim = new Minim(this);
        player1 = minim.loadFile("chomp.wav");
        player2 = minim.loadFile("beginning.wav");
        player3 = minim.loadFile("siren.wav");
        //player3.loop();
        */

        noStroke();
        background(bgColor);

        colarray[0] = color(212, 145, 56);
        colarray[1] = color(241, 120, 47);
        colarray[2] = color(149, 178, 36);
        colarray[3] = color(204, 201, 122);
        colarray[4] = color(91, 157, 1);
        colarray[5] = color(207, 230, 220);
        colarray[6] = color(237, 225, 213);
        colarray[7] = color(141, 3, 0);
        colarray[8] = color(86, 141, 180);
        colarray[9] = color(79, 155, 81);

        colarray[10] = color(246, 189, 182);
        colarray[11] = color(185, 48, 6);
        colarray[12] = color(240, 139, 129);
        colarray[13] = color(231, 235, 198);
        colarray[14] = color(98, 55, 21);
        colarray[15] = color(44, 125, 174);
        colarray[16] = color(202, 153, 136);
        colarray[17] = color(0, 68, 125);
        colarray[18] = color(183, 212, 218);
        colarray[19] = color(239, 237, 188);

        colarray[20] = color(90, 194, 134);
        colarray[21] = color(246, 197, 0);
        colarray[22] = color(157, 136, 117);
        colarray[23] = color(244, 235, 178);
        colarray[24] = color(223, 237, 201);
        colarray[25] = color(36, 101, 159);
        colarray[26] = color(224, 112, 124);
        colarray[27] = color(247, 181, 107);
        colarray[28] = color(133, 153, 190);
        colarray[29] = color(247, 228, 134);

        colarray[30] = color(255, 168, 0);
        colarray[31] = color(143, 53, 0);
        colarray[32] = color(148, 174, 163);
        colarray[33] = color(232, 105, 36);
        colarray[34] = color(151, 137, 136);
        colarray[35] = color(240, 213, 206);
    }

    @Override
    public void draw() {

        background(bgColor);
        if (state == 0) {
            intro();
        } else if (state == 1) {
            soundOn = false;
            //reset pacman
            pacX = 0f;
            pacY = pSize;
            pacSpeed = 6 - (pointNum/5);     //pacSpeed depend du nombre de dots
            alpha = 0;
            //display dots grid
            for (int i = 0; i < dots.size(); i++) {
                Dot part = dots.get(i);
                part.run();
            }
            pacButton();
        } else if (state == 2) {
            soundOn = true;
            //display dots grid
            for (int i = 0; i < dots.size(); i++) {
                Dot part = dots.get(i);
                part.run();
            }

            pacMan();
            if (finished) {
                finished = false;
                state = 0;
                ON2 = false;
            }
        }
    }


    public void onPause(){
        super.onPause();
        if (soundOn) {
            pauseSound(1);
            pauseSound(2);
            pauseSound(3);
        }
           // println("je pause");

    }

    public void onResume() {
        super.onResume();
        if (soundOn) {
            resumeSound(1);
            resumeSound(2);
            resumeSound(3);
        }
           // println("again");

    }
    public void mousePressed() {
        //Log.e("state:", "onClick");
        //println("state:" + state);

        if (state == 0) {
            state = 1;
            hirst();

        }

        //if click on dot
        if (state == 1) {
            if (onDot()) {
                //Dot dot = dots.get(touchDotIndex);

                if (touchDotIndex != pacButtonIndex) {
                    state = 1;
                    hirst();
                    pacButtonIndex = (int) random(dots.size());
                } else {
                    state = 2;

                    ON = true ;
                    stopPlay3  =  millis()+ 4217 ;
                    playSound(3);

                    pacY = pSize;
                    clock();
                }
            }
        }

    }

    boolean onDot() {
        //create mouseX mouseY Vector
        //
        for (int i = 0; i < dots.size(); i++) {
            Dot dot = dots.get(i);
            if (dist (mouseX, mouseY, dot.getPos().x,dot.getPos().y ) < pSize/2 ) {
                touchDotIndex = i;
                return true;
            }
        }
        return false;
    }

    void intro() {
        background(255);
        textAlign(CENTER);

        textSize(248);
        fill(0, 20);
        text("DamienDots", width / 2, height / 2 - 100);

        textSize(48);
        fill(0,20);
        text("by rich.gg", width / 2 + 560, height / 2 - 100);

        textSize(48);
        fill(108);
        text("touch me", width / 2, height / 2 + 50);
    }

    void hirst() {
        //clear
        if (dots != null) {
            for (int i = dots.size() - 1; i >= 0; i--) {
                dots.remove(i);
            }
        }

        //random Dots size
        pSize = Math.round(random(100, 250));
        //Math.round() replaces int()

        //calculate x padding
        int canvas = (width - (2* pSize));
        pointNum = canvas / pSize;
        int actualSize = pSize * pointNum;
        int padding =  (canvas - actualSize)/2;

        //building dots array
        for (int i = pSize; i <= width-pSize; i += pSize) {
            for (int j = pSize; j <= height-pSize; j += pSize) {
                col = color(colarray[(int)random(0, 35)]);
                // fill array list with dots
                dots.add(new Dot(this, i+padding, j, pSize/2, col, i+j));
            }
        }
        //pacSpeed = (6 / pointNum);
        //println("pointNum = " + pointNum + "| pacSpeed = " + pacSpeed);
        //println("dots.size() = " + dots.size() );
    }

    void pacButton(){

        //get x and y of dot at pacButtonIndex
        Dot dot = dots.get(pacButtonIndex);
        // draw pacButton there
        //rectMode(CENTER);
        fill(bgColor);
        //rect(dot.getPos().x, dot.getPos().y, pSize / 2, pSize / 2);
        arc(dot.getPos().x,dot.getPos().y,(pSize / 2)+2, (pSize / 2)+2, radians(-25), radians(25) );
    }

    void clock() {
        startTime = millis();
        stopTime = startTime + (duration - (1/ pSize ));
    }

    void pacMan() {

        if (ON) {
            //play the song, THEN run
            bgColor = color((int)random(255));
            
            //beginning.wav is 4217 millisec long

            if ( millis()  >= stopPlay3) {

                ON2 = true;
                bgColor = color(0);
                ON = false;
            }

        }



        if (ON2) {
            if (!doOnce) {
                /*
                //player3.rewind();
                player3.setGain(-5f);
                //player3.rewind();
                player3.loop();
                */
                playSound(2);
                doOnce = true;
            }

            //run
            //pacSpeed = pacSpeed - (pointNum/4);
            pacX = pacX + pacSpeed ;
            //println(pacSpeed);
            if (pacX > width + pSize) {
                if (dots.size()>0) {
                    pacY = pacY + pSize;
                    pacSpeed = pacSpeed * -1;
                    alpha = PI;
                }
            }
            if (pacX < 0 - pSize) {
                if (dots.size()>0) {
                    pacY = pacY + pSize;
                    pacSpeed = pacSpeed * -1;
                    alpha = 0;
                }
            }
            //fin run

            //bouche
            if (!up) {
                if  (millis () < stopTime) {
                    angleBouche = map(millis(), startTime, stopTime, 0.0f, 0.8f);
                } else {
                    up =true;
                    clock();
                }
            }

            if (up) {
                if  (millis () < stopTime) {
                    angleBouche = map(millis(), startTime, stopTime, 0.8f, 0.0f);
                } else {
                    up =false;
                    clock();
                }
            }
            //fin bouche

            //eat
            for (int i = 0; i < dots.size(); i++) {
                Dot dot = dots.get(i);
                //the dot itself checks if pacman gets very close to it
                if (dot.isEaten()) {
                    /*
                    player1.setGain(-10.0f);
                    player1.play(50); //50 for what I don't know ...
                    */
                    playSound(1);
                    //(how many milliseconds from the beginning of the file to begin playback from)
                    dots.remove(i);
                }
            }
            //fin eat

            //finished
            if (pacX < 0 - pSize*4 || pacX > width + pSize*4) {
                finished = true;

                //stop siren
                stopSound(2);
                bgColor = color(255);
                pacButtonIndex = (int) random(dots.size());
                /*
                player2.rewind();
                player3.pause();
                */
                doOnce = false;
            }
            //fin finished

            //draw
            fill(255, 255, 0);
            pushMatrix();
            translate(pacX, pacY);
            rotate(alpha);
            arc(0, 0, pSize*1.3f, pSize*1.3f, QUARTER_PI-angleBouche, PI+ 3*QUARTER_PI+angleBouche, PIE);
            popMatrix();
        }
    }

    void playSound(int soundID)
    {
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)

        //soundPool.stop(streamId); // kill previous sound - quick hack to void mousePressed triggering twice
        //streamId = soundPool.play(soundID, 1.0f, 1.0f, 1, 0, 1f);

        if (soundID == 1){
            //chomp
            streamId1 = soundPool.play(1, 1.0f, 1.0f, 10, 0, 1f);
        }
        if (soundID == 2){
            //siren (looping)
            streamId2 = soundPool.play(2, 1.0f, 1.0f, 10, -1, 1f);
        }
        if (soundID == 3){
            //beginning
            streamId3 = soundPool.play(3, 1.0f, 1.0f, 10, 0, 1f);
        }

    }

    void stopSound(int soundID) {
        if (soundID == 1) {
            soundPool.stop(streamId1);
        }
        if (soundID == 2) {
            soundPool.stop(streamId2);
        }
        if (soundID == 3) {
            soundPool.stop(streamId3);
        }
    }

    void pauseSound(int soundID) {
        if (soundID == 1) {
            soundPool.pause(streamId1);
        }
        if (soundID == 2) {
            soundPool.pause(streamId2);
        }
        if (soundID == 3) {
            soundPool.pause(streamId3);
        }
    }

    void resumeSound(int soundID) {
        if (soundID == 1) {
            soundPool.resume(streamId1);
        }
        if (soundID == 2) {
            soundPool.resume(streamId2);
        }
        if (soundID == 3) {
            soundPool.resume(streamId3);
        }
    }


}
