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
import android.widget.SeekBar.OnSeekBarChangeListener;

class OggFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".ogg"));
    }
}

public class AudioPlay {

    public boolean playing;
    
    private String audioFile;
    public MediaPlayer mp = new MediaPlayer();

    public int position;
    public int duration;

    public TextView timeDonee;
    public TextView totalTime;

    
    public SeekBar progressBar;



    private final Handler handler = new Handler();
    


    
    public void myUpdate() {
	if (mp.isPlaying()) {
	    duration = mp.getDuration();
	    position = mp.getCurrentPosition();
	    float progress = ((float)position/(float)duration);
	    timeDonee.setText( padTime(position/1000));
	    progressBar.setProgress((int)(progress*progressBar.getMax()));
	    
	    Runnable update = new Runnable() {
		    public void run() {
			myUpdate();
		    }
		};
	    handler.postDelayed(update,1000);
    	}
    }

    public void updateTime() {
	
    }


    public String padTime(int pad) {
	int totalSeconds =  pad;
	String minutes = Integer.toString(totalSeconds/60);
	String seconds = Integer.toString(totalSeconds % 60);
	if (minutes.length()<2) {minutes = "0" + minutes;}
	if (seconds.length()<2) {seconds = "0" + seconds;}
	return minutes +":"+ seconds;
    }
    
    public void playAudio(File audio,SeekBar progressBar,TextView timeDone, TextView totalTime) {
	
	try {
	    this.progressBar = progressBar;
	    this.timeDonee = timeDone;
	    this.totalTime = totalTime;
	    progressBar.setOnSeekBarChangeListener(
		        new OnSeekBarChangeListener() {
			    long duration;
			    public void onStartTrackingTouch(SeekBar bar) {
				myPause();
				duration = mp.getDuration();
			    }
			    public void onProgressChanged(SeekBar bar, int progress, boolean fromtouch) {
				if (fromtouch) {
				    long newposition = (duration * progress) / 1000L;
				    mp.seekTo( (int) newposition);

				    timeDonee.setText(padTime( mp.getCurrentPosition()/1000));
				}
			    }
			    public void onStopTrackingTouch(SeekBar bar) {
				myPlay();
			    }
			});
	    


	    mp.reset();
	    
	    FileInputStream fs = new FileInputStream(audio);
	    FileDescriptor fd = fs.getFD(); 
	    mp.setDataSource(fd);
	    mp.prepare();
	    
	    mp.start();
	    playing = true;
	    duration = mp.getDuration();
	    this.totalTime.setText(padTime(this.duration/1000));
	    this.myUpdate();
		    
	} 
	catch(IOException e) {
	    Log.v("audio exception", e.getMessage());
	} 
    }
    


    public void myPause() {
	    mp.pause();
	    playing = false;
    }
    
    public void myPlay() {
	playing = true;
	mp.start();
	this.myUpdate();
    }


    public void togglePausePlay() {
	if (mp.isPlaying()) {
	    myPause();
	} 
	else {
	    myPlay();
	}
    }

}