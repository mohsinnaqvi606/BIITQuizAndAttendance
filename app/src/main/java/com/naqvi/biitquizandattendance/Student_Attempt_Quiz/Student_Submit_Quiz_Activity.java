package com.naqvi.biitquizandattendance.Student_Attempt_Quiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.naqvi.biitquizandattendance.Student_Main.StudentMainActivity;
import com.naqvi.biitquizandattendance.Student_Mark_Attendance.Student_Submit_Attendance_Activity;
import com.naqvi.biitquizandattendance.Student_Quiz_Result.Quiz_Result_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class Student_Submit_Quiz_Activity extends AppCompatActivity {

    ImageView imageView;
    int tag = 1;
    Button buSubmmit_Quiz;
    Button buTake_Picture;
    String QuizId;
    String Subject;
    SharedReference SharedRef;
    String base64String;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__submit__quiz);

        SharedRef = new SharedReference(this);
        Subject = SharedRef.getQuizPinDetail().Subject;

        String Student_Name = SharedRef.getUser().Name + "  (" + Subject + ")";
        actionBar = getSupportActionBar();
        actionBar.setTitle(Student_Name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        imageView = findViewById(R.id.imageView);
        buTake_Picture = findViewById(R.id.buTake_Picture);
        buSubmmit_Quiz = findViewById(R.id.buSubmmit_Quiz);
        buSubmmit_Quiz.setVisibility(View.INVISIBLE);

        QuizId = SharedRef.getQuizPinDetail().QuizId;
    }

    public void buTake_Picture_Click(View view) {
        Intent intent = new Intent(Student_Submit_Quiz_Activity.this, Take_Picture_Activity.class);
        startActivityForResult(intent, tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == tag) {
            base64String = SharedRef.getImage();
            Bitmap bm = ImageUtil.convertToImage(base64String);
           // imageView.setImageBitmap(bm);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bm, 450, 650, false));




            buSubmmit_Quiz.setVisibility(View.VISIBLE);
            buTake_Picture.setVisibility(View.GONE);
        }
    }

    public void buSubmit(View view) {
        buSubmmit_Quiz.setEnabled(false);
        Insert_Student_Image();
    }

    void Insert_Student_Image() {
        String URL = IP.BASE_URL + "AttemptedQuizDetail/Update_Quiz_Detail";
        String Reg_No = SharedRef.getUser().UserName;
        String Course_No = SharedRef.getCourse_No();
        try {
            JSONObject obj = new JSONObject();
            obj.put("Reg_No", Reg_No);
            obj.put("Course_No", Course_No);
            obj.put("Student_Image", base64String);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT, URL, obj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String Msg = response.optString("Msg");
                    if(Msg.equals("Record Updated")){

                        AlertDialog.Builder builder = new AlertDialog.Builder(Student_Submit_Quiz_Activity.this);
                        builder.setTitle("Submit Quiz");
                        builder.setMessage("Quiz Submitted, do you want to check result");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SharedRef.savebackactivity("Parent");
                                Intent intent = new Intent(Student_Submit_Quiz_Activity.this, Quiz_Result_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Student_Submit_Quiz_Activity.this, StudentMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    }

                    else {
                        Toasty.error(Student_Submit_Quiz_Activity.this,Msg,Toasty.LENGTH_LONG,true).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //    Toast.makeText(getApplicationContext(), "Error : " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        } catch (JSONException e) {
            Toasty.error(Student_Submit_Quiz_Activity.this, "Exception : " + e.getMessage(), Toasty.LENGTH_LONG,true).show();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
