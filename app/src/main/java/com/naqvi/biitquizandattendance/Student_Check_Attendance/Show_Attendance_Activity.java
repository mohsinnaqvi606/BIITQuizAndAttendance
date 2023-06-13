package com.naqvi.biitquizandattendance.Student_Check_Attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.naqvi.biitquizandattendance.Teacher_Check_Attendance.Attendance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Show_Attendance_Activity extends AppCompatActivity {

    RecyclerView attendance_recycleView;
    SharedReference SharedRef;
    String Section,Course,Course_No,Reg_No,Name,Teacher_Name;

    String URL;
    RequestQueue queue;

    ProgressBar progressBar;
    TextView tv_attendance,tv_teacher,tv_course,tv_percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendance);

        queue = Volley.newRequestQueue(this);
        SharedRef = new SharedReference(this);
        Name = SharedRef.getUser().Name;
        Section = SharedRef.getSectionAndSubject().Section;
        Course = "Subject : "+SharedRef.getSectionAndSubject().Subject;
        Reg_No = SharedRef.getUser().UserName;
        Teacher_Name = "Teacher : "+SharedRef.getTeacher();
        Course_No = SharedRef.getCourse_No();
        String Title = Name + "  |  " + Section;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(Title);

        tv_course = findViewById(R.id.tv_course);
        tv_teacher = findViewById(R.id.tv_teacher);
        tv_attendance = findViewById(R.id.tv_attendance);
        tv_percentage = findViewById(R.id.tv_percentage);
        progressBar = findViewById(R.id.progressBar);

        tv_course.setText(Course);
        tv_teacher.setText(Teacher_Name);

        attendance_recycleView = findViewById(R.id.attendance_recycleView);
        loadAttendance();
    }

    void loadAttendance() {

        URL = IP.BASE_URL + "StudentAttendance/Select_Specific_Student_Attendance";
        String query = "?Reg_No=" + Reg_No + "&Course_No=" + Course_No;
        final ArrayList<Attendance_Date> list = new ArrayList<>();
        StringRequest request = new StringRequest(
                Request.Method.GET, URL + query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            int count = arr.length();
                            String present = arr.getJSONObject(0).getString("Present");
                            String attendance = "Attendance : "+present + "/" +count;
                            tv_attendance.setText(attendance);

                            int percentage = (Integer.parseInt(present) * 100) / count;
                            progressBar.setProgress(percentage);
                            String per = percentage+"%";
                            tv_percentage.setText(per);

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String Date = obj.getString("Date");
                                String Time = obj.getString("Time");
                                String Attendance = obj.getString("Attendance");
                                String Image = obj.getString("Student_Image");
                                list.add(new Attendance_Date(Date,Time, Attendance,Image));
                            }
                            Attendance_RecycleView_Adapter adapter = new Attendance_RecycleView_Adapter(Show_Attendance_Activity.this, list);
                            LinearLayoutManager manager = new LinearLayoutManager(Show_Attendance_Activity.this);
                            attendance_recycleView.setLayoutManager(manager);
                            attendance_recycleView.setAdapter(adapter);
                        } catch (JSONException e) {
                            Toasty.info(Show_Attendance_Activity.this,"No Attendance Record Available",Toasty.LENGTH_LONG,true).show();
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
        Intent intent = new Intent(getApplicationContext(),StudentMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
