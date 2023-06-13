package com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result;

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

public class Quiz_List_Activity extends AppCompatActivity {

    RecyclerView recycleView;
    SharedReference SharedRef;
    String Section,Course_No,Name,Subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        SharedRef = new SharedReference(this);
        Section = SharedRef.getSectionAndSubject().Section;
        Course_No = SharedRef.getCourse_No();
        Name = SharedRef.getUser().Name;
        Subject = SharedRef.getSectionAndSubject().Subject;
        String Title = Name+"   ("+Section+" | "+Subject+")";
        getSupportActionBar().setTitle(Title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycleView = findViewById(R.id.recycleView);

        loadData.run();
    }

    Runnable loadData = new Runnable() {
        @Override
        public void run() {
            Load_Result();
        }
    };

    void Load_Result() {
        final ArrayList<Quiz> quiz_List = new ArrayList<>();
        String URL = IP.BASE_URL + "AttemptedQuizDetail/Select_Attempted_Quiz_By_Class";
        String Query = "?Class="+Section+"&Course_No="+Course_No;
        StringRequest request = new StringRequest(
                Request.Method.GET, URL + Query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);

                                String Quiz_Id = obj.getString("Quiz_Id");
                                String Quiz_Title = obj.getString("Quiz_Title");
                                String Date = obj.getString("Date");
                                String Time = obj.getString("Time");
                                Quiz quiz = new Quiz(Quiz_Id,Quiz_Title,Date,Time);
                                quiz_List.add(quiz);

                            }
                                Quiz_List_RecycleAdapter adapter = new Quiz_List_RecycleAdapter(Quiz_List_Activity.this, quiz_List);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recycleView.setLayoutManager(layoutManager);
                                recycleView.setAdapter(adapter);


                        } catch (JSONException e) {
                            String Msg = "No Record for Quiz Result";
                            Toasty.info(Quiz_List_Activity.this,Msg,Toasty.LENGTH_LONG,true).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

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
