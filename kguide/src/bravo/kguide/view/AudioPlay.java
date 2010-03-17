package bravo.kguide.view;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileDescriptor;
import java.io.FileInputStream;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.app.ListActivity;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.SeekBar;

class OggFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".ogg"));
    }
}

public class AudioPlay {
    
    private static final String MEDIA_PATH = new String("/sdcard/");
    private String audioFile;
    private MediaPlayer mp = new MediaPlayer();



    public int position;
    public int duration;

    public TextView timeDone;
    public TextView totalTime;

    
    public SeekBar progressBar;



    private final Handler handler = new Handler();
    

    public void myUpdate() {
	if (mp.isPlaying()) {
	    duration = mp.getDuration();
	    position = mp.getCurrentPosition();
	    float progress = ((float)position/(float)duration);
	    timeDone.setText( Integer.toString(position/1000));
	    progressBar.setProgress((int)(progress*progressBar.getMax()));
	    
	    Runnable update = new Runnable() {
		    public void run() {
			myUpdate();
		    }
		};
	    
	    
	    handler.postDelayed(update,1000);
    	}
    }
    
    public void playAudio(String audio,SeekBar progressBar,TextView timeDone, TextView totalTime) {
	try {
	    this.audioFile = audio;
	    this.progressBar = progressBar;
	    this.timeDone = timeDone;
	    this.totalTime = totalTime;
	    mp.reset();
	    FileInputStream fs = new FileInputStream(MEDIA_PATH + audio);
	    FileDescriptor fd = fs.getFD(); 
	    mp.setDataSource(fd);
	    mp.prepare();
	    
	    mp.start();
	    duration = mp.getDuration();
	    this.totalTime.setText( Integer.toString(this.duration/1000));
	    this.myUpdate();
		    
	} 
	catch(IOException e) {
	    Log.v("audio exception", e.getMessage());
	} 
    }
    
    
    public void togglePausePlay() {
	if (mp.isPlaying()) {
	    position = mp.getCurrentPosition();
	    mp.pause();
	} 
	else {
	    mp.start();
	}

    }

}