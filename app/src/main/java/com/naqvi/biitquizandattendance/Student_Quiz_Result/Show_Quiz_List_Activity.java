package com.naqvi.biitquizandattendance.Student_Quiz_Result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.naqvi.biitquizandattendance.Student_Main.StudentMainActivity;
import com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result.Quiz;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Show_Quiz_List_Activity extends AppCompatActivity {

    RecyclerView recycleView;
    SharedReference SharedRef;
    String Section, Name, Subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz_list);

        SharedRef = new SharedReference(this);
        Section = SharedRef.getSectionAndSubject().Section;
        Name = SharedRef.getUser().Name;
        Subject = SharedRef.getSectionAndSubject().Subject;
        String Title = Name + " (" + Section + " | " + Subject + ")";
        getSupportActionBar().setTitle(Title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycleView = findViewById(R.id.recycleView);

        Load_Result();
    }


    void Load_Result() {
        String Course_No = SharedRef.getCourse_No();
        String Reg_No = SharedRef.getUser().UserName;
        final ArrayList<Quiz> quiz_List = new ArrayList<>();
        String URL = IP.BASE_URL + "AttemptedQuizDetail/Select_Attempted_Quiz_By_Reg_No";
        String Query = "?Reg_No=" + Reg_No + "&Course_No=" + Course_No;
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
                                String Quiz_Detail_Id = obj.getString("Quiz_Detail_Id");
                                String Time = obj.getString("Time");
                                Quiz quiz = new Quiz(Quiz_Detail_Id, Quiz_Title, Date, Time);
                                quiz_List.add(quiz);

                            }
                            Quiz_RecycleAdapter adapter = new Quiz_RecycleAdapter(Show_Quiz_List_Activity.this, quiz_List);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recycleView.setLayoutManager(layoutManager);
                            recycleView.setAdapter(adapter);


                        } catch (JSONException e) {
                            String Msg = response.replace("\"", "");
                            Toast.makeText(Show_Quiz_List_Activity.this, Msg, Toast.LENGTH_LONG).show();

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
        Intent intent = new Intent(getApplicationContext(), StudentMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
