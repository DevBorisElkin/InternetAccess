package ru.startandroid.hw3_internetaccess;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {
    int catMeow1;
    int backgroundMusic;
    Context context;
    int maxVolume = 50;

    MediaPlayer mpBackgroundMusic;
    MediaPlayer mpCatMeow;

    public SoundPlayer(Context context, int catMeow, int backgroundMusic){
        this.catMeow1= catMeow;
        this.backgroundMusic= backgroundMusic;
        this.context=context;
        mpCatMeow=MediaPlayer.create(context, catMeow1);
    }

    public void startBackgroundMusic(){
        mpBackgroundMusic = MediaPlayer.create(context, backgroundMusic);
        mpBackgroundMusic.start();
        mpBackgroundMusic.setLooping(true);
    }
    public void stopBackgroundMusic(){
        mpBackgroundMusic.pause();
    }
    public void resumeBackgroundMusic(){
        mpBackgroundMusic.start();

    }
    public void backgroundMusicSetVolume(){
        //mpBackgroundMusic.setVolume(22f,44f);
    }
    public void makeMeowSound(){

        mpCatMeow.start();
    }
}