package com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.TimeTable.TimeTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Quiz_Marks_Activity extends AppCompatActivity {

    RecyclerView recycleView;
    ArrayList<Quiz_Marks> marks_List = new ArrayList<>();
    RequestQueue queue;
    SharedReference SharedRef ;
    String Date;
    String Time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_marks);

        SharedRef = new SharedReference(this);
        String Title = SharedRef.getUser().Name+"  ("+SharedRef.getSectionAndSubject().Section+" | "+SharedRef.getSectionAndSubject().Subject+")";
        getSupportActionBar().setTitle(Title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Date = intent.getStringExtra("Date");
        Time = intent.getStringExtra("Time");

        queue = Volley.newRequestQueue(this);
        recycleView = findViewById(R.id.recycleView);

        load_Data();
    }

    void load_Data(){
        String Quiz_Id = SharedRef.getQuizPinDetail().QuizId;
        String Class = SharedRef.getSectionAndSubject().Section;
        String Course_No =  SharedRef.getCourse_No();

        Time = Time.replace(" ","-");
        String URL = IP.BASE_URL+"AttemptedQuizDetail/Select_Students_Mark";
        String query = "?Quiz_Id="+Quiz_Id+"&Course_No="+Course_No+"&Class="+Class+"&Time="+Time+"&Date="+Date;
        StringRequest request = new StringRequest(
                Request.Method.GET, URL + query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0;i<arr.length();i++){
                                JSONObject obj = arr.getJSONObject(i);
                                String Quiz_Detail_Id = obj.getString("Quiz_Detail_Id");
                                String Marks= obj.getString("Marks");
                                String Total_Marks = obj.getString("Total_Marks");
                                String Student_Name = obj.getString("Student_Name");
                                String Reg_No = obj.getString("Reg_No");
                                String Image = obj.getString("Student_Image");


                                Quiz_Marks marks = new Quiz_Marks();
                                marks.Quiz_Detail_Id = Quiz_Detail_Id;
                                marks.Reg_No = Reg_No;
                                marks.Name = Student_Name;
                                marks.Marks = Marks+"/"+Total_Marks;
                                marks.Image = Image;
                                marks_List.add(marks);

                            }
                            Quiz_Marks_RecycleAdapter adapter = new Quiz_Marks_RecycleAdapter(Quiz_Marks_Activity.this,marks_List);
                            LinearLayoutManager manager = new LinearLayoutManager(Quiz_Marks_Activity.this);
                            recycleView.setLayoutManager(manager);
                            recycleView.setAdapter(adapter);
                        }catch (JSONException e){

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

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
        Intent intent = new Intent(Quiz_Marks_Activity.this,Quiz_List_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
