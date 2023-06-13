package com.naqvi.biitquizandattendance.Student_Mark_Attendance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.Camera.Take_Picture_Activity;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.ImageUtil;
import com.naqvi.biitquizandattendance.Student_Main.StudentMainActivity;
import com.naqvi.biitquizandattendance.Student_Quiz_Result.Quiz_Result_Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class Student_Submit_Attendance_Activity extends AppCompatActivity {

    ImageView imageView;
    int tag = 1;
    Button buSubmmit_Quiz;
    Button buTake_Picture;
    String Subject;
    SharedReference SharedRef;
    String base64String;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_submit_attendance);

        SharedRef = new SharedReference(this);
        Subject = SharedRef.getSectionAndSubject().Subject;
        String Student_Name = SharedRef.getUser().Name + "  (" + Subject + ")";
        actionBar = getSupportActionBar();
        actionBar.setTitle(Student_Name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        imageView = findViewById(R.id.imageView);
        buTake_Picture = findViewById(R.id.buTake_Picture);
        buSubmmit_Quiz = findViewById(R.id.buSubmmit_Quiz);
        buSubmmit_Quiz.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == tag) {
            base64String = SharedRef.getImage();
            Bitmap bm = ImageUtil.convertToImage(base64String);
       //     imageView.setImageBitmap(bm);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bm, 450, 650, false));

            buSubmmit_Quiz.setVisibility(View.VISIBLE);
            buTake_Picture.setVisibility(View.GONE);
        }
    }

    public void buSubmit(View view) {
        buSubmmit_Quiz.setEnabled(false);
        Insert_Attendance();
    }

    public void buTake_Picture_Click(View view) {
        Intent intent = new Intent(Student_Submit_Attendance_Activity.this, Take_Picture_Activity.class);
        startActivityForResult(intent, tag);
    }

    void Insert_Attendance() {
        String URL = IP.BASE_URL + "StudentAttendance/Update_Attendance";
        String Reg_No = SharedRef.getUser().UserName;
        String Course_No = SharedRef.getCourse_No();
        try {
            JSONObject obj = new JSONObject();
            obj.put("Reg_No", Reg_No);
            obj.put("Course_No", Course_No);
            obj.put("Student_Image", base64String);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT, URL, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String Msg = response.optString("Msg");
                            if (Msg.equals("Attendance Marked")) {
                                Intent intent = new Intent(getApplicationContext(), StudentMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toasty.success(Student_Submit_Attendance_Activity.this, Msg, Toasty.LENGTH_LONG, true).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
