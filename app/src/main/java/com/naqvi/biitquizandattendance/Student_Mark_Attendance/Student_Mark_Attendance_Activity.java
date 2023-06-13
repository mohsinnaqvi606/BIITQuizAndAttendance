package com.naqvi.biitquizandattendance.Student_Mark_Attendance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.Quiz_Pin_Activity;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.Student_Attempt_Quiz_Activity;
import com.naqvi.biitquizandattendance.Student_Main.StudentMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Student_Mark_Attendance_Activity extends AppCompatActivity {

    ActionBar actionBar;
    SharedReference SharedRef;
    String Subject;
    String Date;
    String PIN;
    TextView tvTimer;
    int timeInMilli, totalMilli;
    ProgressBar progressBar;
    Handler handler;
    TextView tvPIN;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuent__mark__attendance);


        SharedRef = new SharedReference(this);
        Intent intent = getIntent();
        Subject = SharedRef.getSectionAndSubject().Subject;
        PIN = intent.getStringExtra("PIN");
        String userName = SharedRef.getUser().Name;

        String title = userName + "    (" + Subject + ")";
        actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        tvTimer = findViewById(R.id.tvTimer);
        tvDate = findViewById(R.id.tvDate);
        tvPIN = findViewById(R.id.tvPin);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date = dateFormat.format(calendar.getTime());

        tvDate.setText(Date);
        tvDate.setEnabled(false);

        totalMilli = timeInMilli = 20;
        handler = new Handler();
        RunTimer.run();
    }

    private Runnable RunTimer = new Runnable() {
        @Override
        public void run() {

            setTime(timeInMilli);

            if (timeInMilli <= 1) {
                handler.removeCallbacks(this);
                tvTimer.setText("00:00");
                progressBar.setProgress(0);
                String MyPIN = tvPIN.getText().toString();
                if (MyPIN.equals(PIN)) {
                    //Insert Picture
                    Intent intent = new Intent(Student_Mark_Attendance_Activity.this, Student_Submit_Attendance_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Pin, Try Again", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Student_Mark_Attendance_Activity.this, StudentMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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

    public void buSubmit(View view) {

        String MyPIN = tvPIN.getText().toString();

        if (MyPIN.equals(PIN)) {
            //Insert Picture
            handler.removeCallbacks(RunTimer);
            Intent intent = new Intent(Student_Mark_Attendance_Activity.this, Student_Submit_Attendance_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        } else {
            Toast.makeText(getApplicationContext(), "Incorrect Pin, Try Again", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onBackPressed() {
    }
}
