package com.naqvi.biitquizandattendance.Student_Main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.LoginActivity;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SectiomAndSubject;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.Quiz_Pin_Activity;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.Quiz_Pin_Detail;
import com.naqvi.biitquizandattendance.Student_Mark_Attendance.Student_Mark_Attendance_Activity;
import com.naqvi.biitquizandattendance.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentMainActivity extends AppCompatActivity {

    SharedReference SharedRef;
    ListView coursesListView;
    RequestQueue queue;
    String Section;
    String URL;
    RequestQueue myqueue;
    Handler handler = new Handler();
    boolean Attendance_Result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        myqueue = Volley.newRequestQueue(getApplicationContext());
        SharedRef = new SharedReference(this);
        URL = IP.BASE_URL;
        coursesListView = findViewById(R.id.coursesListView);
        getCourses();
    }

    void getCourses() {
        queue = Volley.newRequestQueue(this);
        String Reg_No = SharedRef.getUser().UserName;
        String Query = "Student/Select_Current_Courses?Reg_No=" + Reg_No;
        final ArrayList<Course> coursesList = new ArrayList<>();

        StringRequest request = new StringRequest(
                Request.Method.GET, URL + Query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            Section = arr.getJSONObject(0).getString("Section");
                            SharedRef.saveSectionAndSubject(new SectiomAndSubject(Section, ""));

                            String Name = SharedRef.getUser().Name;
                            String title = Name + "   (" + Section + ")";
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setTitle(title);

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String Course = obj.getString("Course");
                                String Course_No = obj.getString("Course_No");
                                String Teacher_Name = obj.getString("Teacher_Name");

                                Course s = new Course();
                                s.Course = Course;
                                s.Course_No = Course_No;
                                s.Teacher_Name = Teacher_Name;
                                coursesList.add(s);
                            }
                            Student_Courses_List_Adapter adapter = new Student_Courses_List_Adapter(StudentMainActivity.this, coursesList);
                            coursesListView.setAdapter(adapter);
                            check_For_Quiz.run();
                            check_For_Attendance.run();

                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private Runnable check_For_Quiz = new Runnable() {
        @Override
        public void run() {
            String Section = SharedRef.getSectionAndSubject().Section;
            String Reg_No = SharedRef.getUser().UserName;
            String URL = IP.BASE_URL + "CurrentQuiz/Select_Current_Quiz";
            String query = "?Reg_No=" + Reg_No + "&Class=" + Section;
            StringRequest request = new StringRequest(
                    Request.Method.GET, URL + query,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray arr = new JSONArray(response);
                                JSONObject obj = arr.getJSONObject(0);
                                String[] code = obj.getString("Quiz_Pin").split("/");
                                String QuizId = obj.getString("Quiz_Id");
                                String Subject = obj.getString("Course");
                                String Quiz_Title = obj.getString("Quiz_Title");
                                String Course_No = obj.getString("Course_No");
                                String Quiz_Detail_Id = obj.getString("Quiz_Detail_Id");

                                String PIN = code[2];
                                SharedRef.saveQuizPinDetail(new Quiz_Pin_Detail(Quiz_Detail_Id, QuizId, PIN, Subject, Quiz_Title));
                                SharedRef.saveCourse_No(Course_No);
                                String Section = SharedRef.getSectionAndSubject().Section;
                                SharedRef.saveSectionAndSubject(new SectiomAndSubject(Subject, Section));
                                handler.removeCallbacks(check_For_Attendance);
                                handler.removeCallbacks(check_For_Quiz);


                                Intent intent = new Intent(StudentMainActivity.this, Quiz_Pin_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } catch (JSONException e) {
                                //    Toast.makeText(getApplicationContext(), "ERROR : "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //   Toast.makeText(getApplicationContext(), "Volley Error : "+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            myqueue.add(request);

            handler.postDelayed(this, 1000);
        }
    };


    private Runnable check_For_Attendance = new Runnable() {
        @Override
        public void run() {
            Check_For_Attendance();
            handler.postDelayed(this, 1000);
        }
    };

    void Check_For_Attendance() {
        String Reg_No = SharedRef.getUser().UserName;
        String URL = IP.BASE_URL + "CurrentAttendance/Select_Current_Attendance";
        String query = "?Reg_No=" + Reg_No;

        StringRequest request = new StringRequest(
                Request.Method.GET, URL + query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            JSONObject obj = arr.getJSONObject(0);
                            String[] code = obj.getString("Attendance_Pin").split("/");
                            String Subject = obj.getString("Course");
                            String Course_No = obj.getString("Course_No");
                            String PIN = code[2];

                            SharedRef.saveCourse_No(Course_No);
                            String Section = SharedRef.getSectionAndSubject().Section;
                            SharedRef.saveSectionAndSubject(new SectiomAndSubject(Subject, Section));

                            handler.removeCallbacks(check_For_Attendance);
                            handler.removeCallbacks(check_For_Quiz);

                            Intent intent = new Intent(getApplicationContext(), Student_Mark_Attendance_Activity.class);
                            intent.putExtra("PIN", PIN);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } catch (JSONException e) {
                            Attendance_Result = true;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        myqueue.add(request);
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
        if (item.getItemId() == R.id.logout) {
            logout_button_click();
        }
        return super.onOptionsItemSelected(item);
    }

    void go_Back() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit from Application?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do No thing.... Continue...
            }
        });

        builder.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void logout_button_click() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to Logout?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  finish();
                handler.removeCallbacks(check_For_Attendance);
                handler.removeCallbacks(check_For_Quiz);
                SharedRef.saveUser(new User("", "", "", ""));
                Intent intent = new Intent(StudentMainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do No thing.... Continue...
            }
        });

        builder.show();
    }
}
