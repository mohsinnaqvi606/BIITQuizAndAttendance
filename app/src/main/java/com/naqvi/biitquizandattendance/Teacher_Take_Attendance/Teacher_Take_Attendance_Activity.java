package com.naqvi.biitquizandattendance.Teacher_Take_Attendance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
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
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SectiomAndSubject;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.TimeTable.TimeTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Teacher_Take_Attendance_Activity extends AppCompatActivity {
    TextView tvDate;
    TextView tvPin;
    String Attendance_Pin;

    SharedReference SharedRef;
    String Course_No, Subject;
    String Section;
    String Date;
    String Time;
    String Pin;
    String Lt_No;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__take__attendance);

        tvDate = findViewById(R.id.tvDate);
        tvPin = findViewById(R.id.tvPin);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date = df.format(calendar.getTime());
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm a");
        Time = tf.format(calendar.getTime());

        tvDate.setEnabled(false);
        tvPin.setEnabled(false);
        tvDate.setText(Date);

        SharedRef = new SharedReference(this);
        String Teacher_Name = SharedRef.getUser().UserName;
        Subject = SharedRef.getSectionAndSubject().Subject;
        Section = SharedRef.getSectionAndSubject().Section;
        Course_No = SharedRef.getCourse_No();
        Lt_No = SharedRef.getLt_No();

        String Title = Teacher_Name + "    (" + Section + "  |  " + Subject + ")";
        actionBar = getSupportActionBar();
        actionBar.setTitle(Title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Random r = new Random();
        String id = String.format("%04d", r.nextInt(10000));
        tvPin.setText(id);

    }

    public void buCreate(View view) {

        Pin = tvPin.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attendance");
        builder.setMessage("Are you want to take Attendance\n" +
                "Pin : " + tvPin.getText().toString());
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Insert_Current_Attendance();
                Toast.makeText(getApplicationContext(), "Pin Created for Attendance", Toast.LENGTH_LONG).show();
                finish();
                Intent i = new Intent(getApplicationContext(), TimeTableActivity.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();


    }

    void Insert_Current_Attendance() {

        String URL = IP.BASE_URL + "CurrentAttendance/Insert_Current_Attendance";
        Attendance_Pin = Lt_No + Subject + "/" + Date + "/" + Pin;
        try {
            JSONObject obj = new JSONObject();
            obj.put("Attendance_Pin", Attendance_Pin);
            obj.put("Course_No", Course_No);
            obj.put("Class", Section);
            obj.put("Date", Date);
            obj.put("Time", Time);
            final JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, URL, obj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        } catch (JSONException e) {
        }
    }


    public void buCancel(View view) {
        back_Button_Pressed();
    }

    @Override
    public void onBackPressed() {
        back_Button_Pressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            back_Button_Pressed();
        }
        return super.onOptionsItemSelected(item);
    }

    void back_Button_Pressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit from Attendance?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
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
