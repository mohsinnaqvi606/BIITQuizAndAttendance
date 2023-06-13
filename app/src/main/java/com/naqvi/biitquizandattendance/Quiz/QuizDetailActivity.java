package com.naqvi.biitquizandattendance.Quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.TimeTable.TimeTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class QuizDetailActivity extends AppCompatActivity {

    ListView questionListView;
    TextView tvQizTitle;
    TextView tvTotalQuestions;
    TextView tvTime;

    RequestQueue queue;

    ActionBar actionBar;
    String QuizId;
    SharedReference SharedRef;
    String TeacherId;
    String Name;
    String Subject;
    String Section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        SharedRef = new SharedReference(this);
        TeacherId = SharedRef.getUser().UserName;
        Name = SharedRef.getUser().Name;
        Subject = SharedRef.getSectionAndSubject().Subject;
        Section = SharedRef.getSectionAndSubject().Section;

        actionBar = getSupportActionBar();
        actionBar.setTitle(Name + "    (" + Section + "  |  " + Subject + ")");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        questionListView = findViewById(R.id.questionsListView);

        tvQizTitle = findViewById(R.id.tvQizTitle);
        tvTotalQuestions = findViewById(R.id.tvTotalQuestions);
        tvTime = findViewById(R.id.tvTime);

        queue = Volley.newRequestQueue(this);

        SharedRef = new SharedReference(this);
        QuizId = SharedRef.getCurrentQuizId();

        loadQuiz.run();

    }


    Runnable loadQuiz = new Runnable() {
        @Override
        public void run() {
            loadQuestions(QuizId);
        }
    };
    void loadQuestions(String QuizId) {

        final ArrayList<Quiz> questions = new ArrayList<Quiz>();
        questions.clear();
        String Query = "QuizQuestions/Select_Specific_Quiz?Quiz_Id=" + QuizId;
        StringRequest request = new StringRequest(
                Request.Method.GET, IP.BASE_URL + Query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String Question = obj.getString("Question").trim();
                                String Option1 = obj.getString("Option1").trim();
                                String Option2 = obj.getString("Option2").trim();
                                String Option3 = obj.getString("Option3").trim();
                                String title = obj.getString("Quiz_Title").trim();
                                String time = obj.getString("Total_Time").trim();

                                Quiz quiz = new Quiz();
                                quiz.Question = Question;
                                quiz.Option1 = Option1;
                                quiz.Option2 = Option2;
                                quiz.Option3 = Option3;
                                quiz.Quiz_Title = title;
                                quiz.Time = time;
                                questions.add(quiz);
                            }

                            String title = "Title : " + questions.get(0).Quiz_Title;
                            String totalQuestion = "Total Question : " + questions.size();
                            String time = "Time : " + questions.get(0).Time + " min";

                            tvQizTitle.setText(title);
                            tvTotalQuestions.setText(totalQuestion);
                            tvTime.setText(time);

                            QuizDetailListAdapter adapter = new QuizDetailListAdapter(getApplicationContext(), questions);
                            questionListView.setAdapter(adapter);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "ERROR" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "vOLLEY" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);
    }

    public void buClose(View view) {
        Intent intent = new Intent(this, TimeTableActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void bu_TakeNow(View view) {
        Intent intent = new Intent(this, Take_Now_Quiz_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

        if (SharedRef.getbackactivity().equalsIgnoreCase("Parent")) {
            SharedRef.savebackactivity("No");
            Intent intent = new Intent(QuizDetailActivity.this, TimeTableActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(), Show_All_Quiz_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);}
    }
}
