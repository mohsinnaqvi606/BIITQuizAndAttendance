package com.naqvi.biitquizandattendance.Quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.TimeTable.TimeTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Take_Now_Quiz_Activity extends AppCompatActivity {
    ActionBar actionBar;
    SharedReference SharedRef;
    RequestQueue queue;

    EditText tvPin;
    String QuizId,Section,Date,Time,Subject,Course_No,Lt_No;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take__now__quiz);

        queue = Volley.newRequestQueue(this);
        SharedRef = new SharedReference(this);
        String Teacher = SharedRef.getUser().UserName;
        Section = SharedRef.getSectionAndSubject().Section;
        Subject = SharedRef.getSectionAndSubject().Subject;
        Course_No = SharedRef.getCourse_No();
        Lt_No = SharedRef.getLt_No();

        actionBar = getSupportActionBar();
        actionBar.setTitle(Teacher + "      (" + Subject + "  - " + Section + ")");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        tvPin = findViewById(R.id.etPin);
        tvPin.setEnabled(false);

        QuizId = SharedRef.getCurrentQuizId();


        Random r = new Random();
        String id = String.format("%04d", r.nextInt(10000));
        tvPin.setText(id);

    }



    String generate_Quiz_Pin() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormate = new SimpleDateFormat("hh:mm a");
        Date = dateFormat.format(calendar.getTime());
        Time = timeFormate.format(calendar.getTime());
        String Pin = tvPin.getText().toString().trim();
        String Quiz_Pin = Lt_No+Subject + "/" + Date + "/" + Pin;

        return Quiz_Pin;
    }



    public void buStart(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz");
        builder.setMessage("Do you want to Start Quiz \n Pin : " + tvPin.getText().toString());
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertQuiz.run();
                Toast.makeText(Take_Now_Quiz_Activity.this,"Quiz started and will be delete fter 20 seconds",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    Runnable insertQuiz = new Runnable() {
        @Override
        public void run() {
            insert_Current_Quiz();
        }
    };

    void insert_Current_Quiz() {
        String URL = IP.BASE_URL + "CurrentQuiz/Insert_Current_Quiz";
        try {
            JSONObject obj = new JSONObject();
            obj.put("Quiz_Pin", generate_Quiz_Pin());
            obj.put("Quiz_Id", QuizId);
            obj.put("Class", Section);
            obj.put("Course_No",Course_No);
            obj.put("Date",Date);
            obj.put("Time",Time);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, URL, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            queue.add(request);

        } catch (JSONException e) {
        }

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
        Intent intent = new Intent(getApplicationContext(), QuizDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
