package com.example.dell.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random rand = new Random();
    int v1, v2 ,rightAnswer=0 , userAnswer=0, tQuestions=0;
    boolean star=true;

    long remainingTimeMillis = -1;

    public void nextQuestion(){
        int a = rand.nextInt(50);
        int b=rand.nextInt(50);
        v1=a;   v2=b;
        rightAnswer = v1 + v2;
        userAnswer=0;
        updateText();
    }

    public void updateText() {
        TextView ques = findViewById(R.id.ques);
        ques.setText(v1 + " + " + v2 +" = "+ (userAnswer == 0 ? "?" : userAnswer));

        TextView score = findViewById(R.id.score);
        score.setText(Integer.toString(tQuestions));
    }

    public void buttonPressed(View view){
        int t=0;
        int id=view.getId();
        if(id == R.id.b1) t=1;
        if(id == R.id.b2) t=2;
        if(id == R.id.b3) t=3;
        if(id == R.id.b4) t=4;
        if(id == R.id.b5) t=5;
        if(id == R.id.b6) t=6;
        if(id == R.id.b7) t=7;
        if(id == R.id.b8) t=8;
        if(id == R.id.b9) t=9;
        if(id == R.id.b0) t=0;
        userAnswer=userAnswer*10 + t;
        if(userAnswer==rightAnswer){
            nextQuestion();
            tQuestions++;
        }
        updateText();
    }

    public void backspace(View view){
        userAnswer/=10;
        updateText();
    }

    public void replay(View view){
        final TextView timer = (TextView)findViewById(R.id.timer);
        LinearLayout replayScreen = (LinearLayout)findViewById(R.id.replayScreen);
        replayScreen.setVisibility(View.INVISIBLE);

        if (remainingTimeMillis <= 0) {
            userAnswer=0;
            tQuestions=0;
            remainingTimeMillis = 30000;// Default playtime is 30 seconds
        }

        new CountDownTimer(remainingTimeMillis, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeMillis = millisUntilFinished;

                timer.setText(Integer.toString((int)millisUntilFinished/1000));
                if(star==true) {

                    nextQuestion();
                    star=false;
                }
            }
            @Override
            public void onFinish() {
                remainingTimeMillis = -1;

                onGameEnd();
            }
        }.start();
    }

    public void onGameEnd() {
        TextView fScore = (TextView)findViewById(R.id.fScore);
        fScore.setText(Integer.toString(tQuestions));
        LinearLayout replayScreen = (LinearLayout)findViewById(R.id.replayScreen);
        replayScreen.setVisibility(View.VISIBLE);
        TextView score = (TextView)findViewById(R.id.score);
        score.setText(Integer.toString(0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView score = (TextView) findViewById(R.id.score);

        if (savedInstanceState == null) {
            score.setText(Integer.toString(0));

            replay(score);
        } else {
            v1 = savedInstanceState.getInt("v1");
            v2 = savedInstanceState.getInt("v2");
            star = savedInstanceState.getBoolean("star");
            rightAnswer = savedInstanceState.getInt("rightAnswer");
            userAnswer = savedInstanceState.getInt("userAnswer");
            tQuestions = savedInstanceState.getInt("tQuestion");
            remainingTimeMillis = savedInstanceState.getLong("remainingTimeMillis");

            if (remainingTimeMillis > 0) {
                replay(score);
            } else {
                onGameEnd();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putInt("v1", v1);
        bundle.putInt("v2", v2);
        bundle.putInt("rightAnswer", rightAnswer);
        bundle.putInt("userAnswer", userAnswer);
        bundle.putInt("tQuestion", tQuestions);
        bundle.putLong("remainingTimeMillis", remainingTimeMillis);
    }
}
