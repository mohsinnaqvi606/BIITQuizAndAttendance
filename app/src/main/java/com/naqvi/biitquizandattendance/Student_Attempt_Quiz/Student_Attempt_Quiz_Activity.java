package com.naqvi.biitquizandattendance.Student_Attempt_Quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.Quiz.Quiz;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Main.StudentMainActivity;
import com.naqvi.biitquizandattendance.TimeTable.TimeTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Student_Attempt_Quiz_Activity extends AppCompatActivity {

    TextView tvTimer;
    TextView tvTitle;
    TextView tvQuestion;
    TextView tvQuestion_No;
    RadioButton rdOption1, rdOption2, rdOption3;
    Button buNext, buBack;
    Handler handler;
    int timeInMilli, totalMilli;
    int TotalTime;
    ProgressBar progressBar;
    ActionBar actionBar;
    SharedReference SharedRef;
    RadioGroup radioGroup;
    final ArrayList<Quiz> questions = new ArrayList<>();

    int index = 0;
    String QuizId;
    String Subject;
    String Reg_No;
    String Course_No;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__attempt__quiz);
        SharedRef = new SharedReference(this);
        String userName = SharedRef.getUser().Name;
        Subject = SharedRef.getQuizPinDetail().Subject;
        Reg_No = SharedRef.getUser().UserName;
        Course_No = SharedRef.getCourse_No();

        String title = userName + " (" + Subject + ")";
        actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);


        progressBar = findViewById(R.id.progressBar);
        tvTitle = findViewById(R.id.tvTitle);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestion_No = findViewById(R.id.tvQuestion_No);
        radioGroup = findViewById(R.id.radioGroup);
        rdOption1 = findViewById(R.id.rdOption1);
        rdOption2 = findViewById(R.id.rdOption2);
        rdOption3 = findViewById(R.id.rdOption3);

        buNext = findViewById(R.id.buNext);
        buBack = findViewById(R.id.buBack);
        buBack.setVisibility(View.INVISIBLE);

        handler = new Handler();


        QuizId = SharedRef.getQuizPinDetail().QuizId;
        loadQuiz();
        tvTitle.setText("Quiz Title : " + SharedRef.getQuizPinDetail().Quiz_Title);
    }

    private Runnable RunTimer = new Runnable() {
        @Override
        public void run() {
            timeInMilli -= 1000;
            setTime(timeInMilli);

            if (timeInMilli <= 1000) {
                handler.removeCallbacks(this);
                tvTimer.setText("00:00");
                progressBar.setProgress(0);

                Intent intent = new Intent(getApplicationContext(), Student_Submit_Quiz_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }

            handler.postDelayed(this, 1000);
        }
    };

    void setTime(int Time) {
        String minute = String.valueOf(Time / 1000 / 60);
        String seconds = String.valueOf(Time / 1000 % 60);

        if ((Time / 1000 / 60) == 0 && (Time / 1000 % 60) <= 30) {
            tvTimer.setTextColor(Color.RED);
        }

        if (Time / 1000 / 60 < 10) {
            minute = "0" + minute;
        }
        if (Time / 1000 % 60 < 10) {
            seconds = "0" + seconds;
        }
        String time = minute + ":" + seconds;
        tvTimer.setText(time);
        int progress = (Time * 100) / totalMilli;
        progressBar.setProgress(progress);
    }

    void loadQuiz() {
        String URL = IP.BASE_URL + "QuizQuestions/Select_Specific_Quiz";
        String query = "?Quiz_Id=" + QuizId;
        StringRequest request = new StringRequest(
                Request.Method.GET, URL + query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            if (arr.length() < 2) {
                                buNext.setText("Finish");
                            }
                            for (int i = 0; i < arr.length(); i++) {

                                JSONObject obj = arr.getJSONObject(i);

                                TotalTime = obj.getInt("Total_Time");
                                Quiz q = new Quiz();
                                q.Question_No = obj.getString("Question_No");
                                q.Question = obj.getString("Question");
                                q.Option1 = obj.getString("Option1");
                                q.Option2 = obj.getString("Option2");
                                q.Option3 = obj.getString("Option3");
                                q.Answer = obj.getString("Answer");
                                questions.add(q);
                            }
                            load_Question();
                            handler = new Handler();
                            totalMilli = TotalTime * 60 * 1000;
                            timeInMilli = totalMilli;
                            RunTimer.run();

                        } catch (JSONException e) {
                            //  Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    void load_Question() {

        radioGroup.clearCheck();

        Quiz q = questions.get(index);
        tvQuestion_No.setText("Question : " + q.Question_No + "/" + questions.size());
        tvQuestion.setText("Q) "+q.Question);
        rdOption1.setText(q.Option1);
        rdOption2.setText(q.Option2);
        rdOption3.setText(q.Option3);

        if (q.MyAnswer != null) {
            if (q.MyAnswer.equals(q.Option1)) {
                rdOption1.setChecked(true);
            }
            if (q.MyAnswer.equals(q.Option2)) {
                rdOption2.setChecked(true);
            }
            if (q.MyAnswer.equals(q.Option3)) {
                rdOption3.setChecked(true);
            }
        }
    }

    void save_Answer() {
        Quiz q = questions.get(index);
        if (rdOption1.isChecked()) {
            q.MyAnswer = q.Option1;
        } else if (rdOption2.isChecked()) {
            q.MyAnswer = q.Option2;
        } else if (rdOption3.isChecked()) {
            q.MyAnswer = q.Option3;
        }
        questions.set(index, q);
    }

    public void buNext_Click(View view) {

        if (buNext.getText().equals("Finish")) {
            handler.removeCallbacks(RunTimer);
        }
        if (index < questions.size() - 1) {
            save_Answer();
            index++;
            load_Question();

            if (index == questions.size() - 1) {
                buNext.setText("Finish");
            }
        } else if (index == questions.size() - 1) {
            save_Answer();
            handler.removeCallbacks(RunTimer);

            for (int i = 0; i < questions.size(); i++) {
                Insert_Attempted_quiz_Question(i);
            }
            finish();
            Intent intent = new Intent(getApplicationContext(), Student_Submit_Quiz_Activity.class);
            startActivity(intent);
        }
        if (index > 0) {
            buBack.setVisibility(View.VISIBLE);
        }
    }

    void Insert_Attempted_quiz_Question(int Question_No) {
        int Mark = 0;
        Quiz q = questions.get(Question_No);
        try {
            if (q.MyAnswer.equals(q.Answer)) {
                Mark = 1;
            }
        }catch (Exception e){}

        try {
            JSONObject obj = new JSONObject();
            obj.put("Reg_No", Reg_No);
            obj.put("Course_No", Course_No);
            obj.put("Question_No", q.Question_No);
            obj.put("Answer", q.MyAnswer);
            obj.put("Mark", Mark);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, IP.BASE_URL + "AttemptedQuizAnswers/Insert_Quiz_Answer", obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String Msg = response.optString("Msg");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        } catch (JSONException e) {
        }
    }

    public void buBack_Click(View view) {
        buNext.setText("Next");
        save_Answer();
        index--;
        if (index < 1) {
            buBack.setVisibility(View.INVISIBLE);
        }
        load_Question();
    }

    @Override
    public void onBackPressed() {
        go_Back();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            go_Back();
        }
        return super.onOptionsItemSelected(item);
    }

    void go_Back() {
    }

}
