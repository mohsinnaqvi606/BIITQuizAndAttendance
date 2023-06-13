package com.naqvi.biitquizandattendance.Teacher_Check_Attendance;

import androidx.appcompat.app.ActionBar;
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
import com.naqvi.biitquizandattendance.TimeTable.TimeTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Show_Attendance_Activity extends AppCompatActivity {

    RequestQueue queue;
    String URL;
    SharedReference SharedRef;
    ActionBar actionBar;
    static final ArrayList<Attendance> attendances = new ArrayList<>();
    String TeacherId, Subject, Section, Course_No;

    RecyclerView attendanceRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__attendance);

        queue = Volley.newRequestQueue(this);

        SharedRef = new SharedReference(this);
        TeacherId = SharedRef.getUser().Name;
        Subject = SharedRef.getSectionAndSubject().Subject;
        Section = SharedRef.getSectionAndSubject().Section;
        Course_No = SharedRef.getCourse_No();

        actionBar = getSupportActionBar();
        actionBar.setTitle(TeacherId + "    (" + Section + "  |  " + Subject + ")");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView);
       // loadAttendance();
        LOAD_DATA.run();
    }

    Runnable LOAD_DATA = new Runnable() {
        @Override
        public void run() {
            loadAttendance();
        }
    };

    void loadAttendance() {
        URL = IP.BASE_URL + "StudentAttendance/Select_Attendance_By_Class";
        String query = "?Class=" + Section + "&Course_No=" + Course_No;
        StringRequest request = new StringRequest(
                Request.Method.GET, URL + query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            attendances.clear();
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                Attendance attendance = new Attendance();
                                JSONObject obj = arr.getJSONObject(i);
                                String AridNo = obj.getString("Reg_No").trim();
                                String Name = obj.getString("Student_Name").trim();
                                String Present = obj.getString("Present");
                                String Total_Attendance = obj.getString("Total_Attendance");
                                int percentage = (Integer.parseInt(Present) * 100) / Integer.parseInt(Total_Attendance) ;

                                attendance.Name = Name;
                                attendance.AridNo = AridNo;
                                attendance.Percentage = percentage;
                                attendances.add(attendance);
                            }
                            attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            Attendance_Recycler_View_Adapter adapter = new Attendance_Recycler_View_Adapter(Show_Attendance_Activity.this,attendances);
                            attendanceRecyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            String Msg = "No Record for Attendance";
                            Toasty.info(Show_Attendance_Activity.this,Msg,Toasty.LENGTH_LONG,true).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
