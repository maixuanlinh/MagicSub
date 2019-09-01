package com.maixuanlinh.magicsub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private ArrayList<Integer> subTimeArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.workingmother);
        playbtn = findViewById(R.id.playbtn);
        resultTxv = findViewById(R.id.textResult);
        subBtn = findViewById(R.id.subbtn);
        cancelLastbtn = findViewById(R.id.cancelLastbtn);
        pauseBtn = findViewById(R.id.pausebtn);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.start();
            }
        });

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   int currentPositionInSec = mediaPlayer.getCurrentPosition()/1000;
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
                for (int i = 0; i < subTimeArrayList.size()-1;i++) {
                    textResult += subTimeArrayList.get(i)+",";
                }
                textResult+= subTimeArrayList.get(subTimeArrayList.size()-1);
                ClipData clipData = ClipData.newPlainText("Result", textResult);
                clipboardManager.setPrimaryClip(clipData);
            }
        });

        cancelLastbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    subTimeArrayList.remove(subTimeArrayList.size()-1);
                    mediaPlayer.seekTo(subTimeArrayList.get(subTimeArrayList.size()-1)*1000);
                    if (!mediaPlayer.isPlaying()) { mediaPlayer.start();}
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });

        pauseBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mediaPlayer.seekTo(0);

                return false;
            }
        });






    }
}
