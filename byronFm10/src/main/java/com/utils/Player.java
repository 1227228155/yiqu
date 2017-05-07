package com.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Player implements OnBufferingUpdateListener, OnCompletionListener, MediaPlayer.OnPreparedListener {
    public MediaPlayer mediaPlayer;
    private SeekBar skbProgress;
    private TextView now_time;
    private TextView all_time;
    private ImageView video_play;
    private boolean isfirst = true;

    private Timer mTimer = new Timer();
    private int duration;
    MediaPlayerProxy proxy;
    Context context;
    private boolean USE_PROXY = true;
   // private String oldUrl ;
    private onPlayCompletion onPlayCompletion;
    private onPreparedCompletion mListener;
    public Player(Context context, SeekBar skbProgress, ImageView video_play,
                  TextView now_time, TextView all_time,onPlayCompletion onPlayCompletion) {
        this.skbProgress = skbProgress;
        this.now_time = now_time;
        this.all_time = all_time;
        this.context = context;
        this.video_play = video_play;
        this.onPlayCompletion = onPlayCompletion;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }

        mTimer.schedule(mTimerTask, 0, 1000);
        proxy = new MediaPlayerProxy(context);
        proxy.init();
        proxy.start();
    }

    /*******************************************************
     * 通过定时器和Handler来更新进度条
     ******************************************************/
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (skbProgress == null)
                return;
//            if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
//                handleProgress.sendEmptyMessage(0);
//            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handleProgress = new Handler() {


        public void handleMessage(Message msg) {
            if (mediaPlayer == null)
                return;
            int position = mediaPlayer.getCurrentPosition();
            duration = mediaPlayer.getDuration();

            if (duration > 0) {
                if (skbProgress != null) {
                    long pos = skbProgress.getMax() * position / duration;
                    skbProgress.setProgress((int) pos);
                }
                if (now_time != null) {

                    now_time.setText("" + changeNum(position / (60 * 1000)) + ":" + changeNum(position % (60 * 1000) / 1000));
                }
                if (all_time != null) {

                    all_time.setText("" + changeNum(duration / (60 * 1000)) + ":" + changeNum(duration % (60 * 1000) / 1000));
                }
            }
        }

        ;
    };

    public int getCurrentPosition(){
        int position = mediaPlayer.getCurrentPosition();
        return position;
    }
    public int getDuration(){
        int position = mediaPlayer.getDuration();
        return position;
    }

    public void rePlay() {
        mediaPlayer.start();
    }
    public void seekTo(int msec) {
        mediaPlayer.seekTo(msec);
    }

    public boolean isPlaying() {
        if (mediaPlayer==null){
            return false;
        }
        return mediaPlayer.isPlaying();
    }

//    public String getUrl() {
//        return oldUrl;
//    }

    public void playUrl(String url) {
//        if (oldUrl!=null && oldUrl.equals(url)) {
//
//        } else {
            if (USE_PROXY) {
                startProxy();
             //   oldUrl = url;
                url = proxy.getProxyURL(url);
//                LogUtils.LOGE("mediaPlayer",url);
            }
            try {
//                if (mediaPlayer.isPlaying()){
//                    mediaPlayer.pause();
//                }
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
                //mediaPlayer.start();
//
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//    }


    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
       //     mediaPlayer = null;
        }
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onPrepared");
        arg0.start();
        if(mListener != null) mListener.onPreparedCompletion();
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");
        //now_time.setText(""+changeNum(position/(60*1000))+":"+changeNum(position%(60*1000)/1000));
        if (now_time != null) {

            now_time.setText("" + changeNum(duration / (60 * 1000)) + ":" + changeNum(duration % (60 * 1000) / 1000));
        }
        if (skbProgress != null) {

            skbProgress.setProgress(100);
        }
        isfirst = true;

        if (video_play != null) {
           // video_play.setImageResource(R.mipmap.video_play);
        }
        onPlayCompletion.completion();


    }
    public interface onPlayCompletion {
        public void completion();
    }


    public void setOnPreparedCompletion(onPreparedCompletion l) {
        mListener = l;
    }
    public interface onPreparedCompletion {
        public void onPreparedCompletion();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        if (skbProgress != null) {

            skbProgress.setSecondaryProgress(bufferingProgress);
        }


        //  int currentProgress = skbProgress.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
        // Log.e(currentProgress + "% play", bufferingProgress + "% buffer");
    }

    private void startProxy() {
        if (proxy == null) {
            proxy = new MediaPlayerProxy(context);
            proxy.init();
            proxy.start();
        }
    }

    public String changeNum(int num) {
        if (num == 0) {
            return "00";
        }
        if (num == 1) {
            return "01";
        }
        if (num == 2) {
            return "02";
        }
        if (num == 3) {
            return "03";
        }
        if (num == 4) {
            return "04";
        }
        if (num == 5) {
            return "05";
        }
        if (num == 6) {
            return "06";
        }
        if (num == 7) {
            return "07";
        }
        if (num == 8) {
            return "08";
        }
        if (num == 9) {
            return "09";
        }

        return "" + num;
    }
}
