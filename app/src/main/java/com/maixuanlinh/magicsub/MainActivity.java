package com.maixuanlinh.magicsub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ClipboardManager clipboardManager;
    private String textResult ="0,";
    private TextView resultTxv;
    private Button playbtn;
    private Button subBtn;
    private Button cancelLastbtn;
    private Button pauseBtn;
    private float speed = 1f;
    private Button speedbtn;
    private ArrayList<Integer> subTimeArrayList = new ArrayList<>();
    private EditText editText;
    private Button getAudio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        playbtn = findViewById(R.id.playbtn);
        resultTxv = findViewById(R.id.textResult);
        subBtn = findViewById(R.id.subbtn);
        cancelLastbtn = findViewById(R.id.cancelLastbtn);
        pauseBtn = findViewById(R.id.pausebtn);
        speedbtn = findViewById(R.id.speed);
        getAudio = findViewById(R.id.getaudiobtn);
        editText = findViewById(R.id.editText);



        getAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String url = editText.getText().toString();
                    Log.i("Text",url);
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(final MediaPlayer mp) {
                            Toast.makeText(getApplicationContext(),"Ready",Toast.LENGTH_SHORT).show();


                            playbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    mp.start();
                                }
                            });

                            subBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        int currentPositionInSec = mp.getCurrentPosition()/1000;
                                        subTimeArrayList.add(currentPositionInSec);
                                        String currentArrayList = subTimeArrayList.toString();
                                        resultTxv.setText(currentArrayList);


                                    } catch (Exception e) {

                                    }
                                }
                            });

                            resultTxv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    for (int i = 0; i < subTimeArrayList.size();i++) {
                                        textResult += subTimeArrayList.get(i)+",";
                                    }
                                    textResult+= mp.getDuration()/1000;
                                    ClipData clipData = ClipData.newPlainText("Result", textResult);
                                    clipboardManager.setPrimaryClip(clipData);
                                }
                            });

                            cancelLastbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    subTimeArrayList.remove(subTimeArrayList.size()-1);
                                    mp.seekTo(subTimeArrayList.get(subTimeArrayList.size()-1)*1000);
                                    if (!mp.isPlaying()) { mp.start();}
                                    String currentArrayList = subTimeArrayList.toString();
                                    resultTxv.setText(currentArrayList);
                                }
                            });

                            pauseBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mp.pause();
                                }
                            });

                            pauseBtn.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    mp.seekTo(0);

                                    return false;
                                }
                            });

                            speedbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (speed == 2f) {
                                        speed = 1f;

                                    } else {
                                        speed += 0.5f;
                                    }
                                    mp.setPlaybackParams(new PlaybackParams().setSpeed(speed));
                                }
                            });

                        }
                    });







                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("IO Exception", "caught "+e.getMessage());
                }
            }
        });





    }
}
