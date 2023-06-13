package com.naqvi.biitquizandattendance.Quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

import es.dmoral.toasty.Toasty;

public class Show_All_Quiz_Activity extends AppCompatActivity {

    ActionBar actionBar;
    RecyclerView quizRecyclerView;
    SharedReference SharedRef;
    String TeacherId,Subject,Section,Course_No,Name;

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__quiz);


        SharedRef = new SharedReference(this);
        TeacherId = SharedRef.getUser().UserName;
        Name = SharedRef.getUser().Name;
        Subject = SharedRef.getSectionAndSubject().Subject;
        Section = SharedRef.getSectionAndSubject().Section;
        Course_No = SharedRef.getCourse_No();

        actionBar = getSupportActionBar();
        actionBar.setTitle(Name +"    ("+Section+"  |  "+Subject+")");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadQuiz.run();
    }

    Runnable loadQuiz = new Runnable() {
        @Override
        public void run() {
            loadQuizes();
        }
    };

    void loadQuizes(){
        String  URL = IP.BASE_URL+"QuizDetail/Select_Quiz_By_Teacher";
        String query = "?Teacher_Id="+ TeacherId +"&Couses_No="+Course_No;
        final ArrayList<Quiz_Detail> quizList = new ArrayList<>();

        StringRequest request = new StringRequest(
                Request.Method.GET, URL+query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i<arr.length();i++){
                                JSONObject obj = arr.getJSONObject(i);

                                String id = obj.getString("Quiz_Id");
                                String title = obj.getString("Quiz_Title");
                                String time = obj.getString("Total_Time");
                                String Total_Question =  obj.getString("Total_Question");
                                Quiz_Detail quiz = new Quiz_Detail();

                                quiz.QuizId = id;
                                quiz.Quiz_Title =title;
                                quiz.Time = time;
                                quiz.Total_Question = Total_Question;
                                if(quiz.Time.length()<2)
                                {
                                    quiz.Time = "0"+time;
                                }

                                if(quiz.Total_Question.length()<2)
                                {
                                    quiz.Total_Question = "0"+Total_Question;
                                }

                                quizList.add(quiz);
                            }
                            quizRecyclerView = findViewById(R.id.quizRecyclerView);
                            quizRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            Quiz_List_RecyclerAdapter adapter = new Quiz_List_RecyclerAdapter(Show_All_Quiz_Activity.this,quizList);
                            quizRecyclerView.setAdapter(adapter);
                        }catch (JSONException e){
                            String Msg = "No quiz available, Create quiz first";
                            Toasty.info(Show_All_Quiz_Activity.this,Msg,Toasty.LENGTH_LONG,true).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue = Volley.newRequestQueue(this);
        queue.add(request);
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
        Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
