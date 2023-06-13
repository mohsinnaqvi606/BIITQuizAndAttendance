package com.naqvi.biitquizandattendance.Student_Attempt_Quiz;

import androidx.appcompat.app.AppCompatActivity;

import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Main.StudentMainActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class Quiz_Pin_Activity extends AppCompatActivity {

    SharedReference SharedRef;
    String Subject, Date, PIN;
    int timeInMilli, totalMilli;
    ProgressBar progressBar;
    Handler handler;
    TextView tvTimer, tv_subject, tv_title, tv_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_pin);


        SharedRef = new SharedReference(this);
        String userName = SharedRef.getUser().Name;
        Subject = SharedRef.getSectionAndSubject().Subject;

        String title = userName + "    (" + Subject + ")";

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        tvTimer = findViewById(R.id.tvTimer);
        tv_subject = findViewById(R.id.tv_subject);
        tv_title = findViewById(R.id.tv_title);
        tv_pin = findViewById(R.id.tv_pin);


        String Subject = "Subject : " + SharedRef.getQuizPinDetail().Subject;
        String Title = "Title : " + SharedRef.getQuizPinDetail().Quiz_Title;
        tv_title.setText(Title);
        tv_subject.setText(Subject);
        tv_title.setEnabled(false);
        tv_subject.setEnabled(false);

        totalMilli = timeInMilli = 20;
        handler = new Handler();
        RunTimer.run();
    }

    public void bu_Next_Click(View view) {

        String PIN = SharedRef.getQuizPinDetail().Quiz_Pin;
        String MyPIN = tv_pin.getText().toString();

        if (MyPIN.equals(PIN)) {
            //GO TO QUIZ
            handler.removeCallbacks(RunTimer);
            Intent intent = new Intent(Quiz_Pin_Activity.this, Student_Attempt_Quiz_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toasty.error(Quiz_Pin_Activity.this, "Incorrect Pin, Try Again", Toasty.LENGTH_LONG,true).show();
        }

    }

    private Runnable RunTimer = new Runnable() {
        @Override
        public void run() {

            setTime(timeInMilli);

            if (timeInMilli <= 1) {
                handler.removeCallbacks(this);
                tvTimer.setText("00:00");
                progressBar.setProgress(0);
                String PIN = SharedRef.getQuizPinDetail().Quiz_Pin;
                String MyPIN = tv_pin.getText().toString();
                if (MyPIN.equals(PIN)) {
                    Intent intent = new Intent(Quiz_Pin_Activity.this, Student_Attempt_Quiz_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                } else {
                    Intent intent = new Intent(Quiz_Pin_Activity.this, StudentMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toasty.error(Quiz_Pin_Activity.this, "Time Over! You took too much time", Toasty.LENGTH_LONG,true).show();
                    return;
                }
            }

            timeInMilli--;
            handler.postDelayed(this, 1000);
        }
    };

    void setTime(int Time) {
        String seconds = String.valueOf(Time);

        if (Time <= 10) {
            tvTimer.setTextColor(Color.RED);
        }


        if (Time < 10) {
            seconds = "0" + seconds;
        }
        String time = "00:" + seconds;
        tvTimer.setText(time);
        int progress = (Time * 100) / totalMilli;
        progressBar.setProgress(progress);
    }

    @Override
    public void onBackPressed() {

    }
}
