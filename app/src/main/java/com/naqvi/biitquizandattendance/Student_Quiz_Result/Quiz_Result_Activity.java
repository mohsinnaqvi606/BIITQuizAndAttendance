package com.naqvi.biitquizandattendance.Student_Quiz_Result;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.Quiz.Quiz_Detail;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.ImageUtil;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.Student_Submit_Quiz_Activity;
import com.naqvi.biitquizandattendance.Student_Main.StudentMainActivity;
import com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result.Quiz_List_Activity;
import com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result.Quiz_Marks_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Quiz_Result_Activity extends AppCompatActivity {

    ImageView ImgView;
    ListView lstAnswers;
    TextView tvTitle;
    TextView tvMarks;
    TextView tvTotalQuestions;
    TextView tvCorrectAnswers;
    TextView tvInCorrectAnswers;
    String Quiz_Detail_Id;
    String Subject;

    ActionBar actionBar;
    SharedReference SharedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz__result);

        SharedRef = new SharedReference(this);
        Quiz_Detail_Id = SharedRef.getQuizPinDetail().Quiz_Detail_Id;
        Subject = SharedRef.getSectionAndSubject().Subject;

        String Student_Name = SharedRef.getUser().Name + "  (" + Subject + ")";
        actionBar = getSupportActionBar();
        actionBar.setTitle(Student_Name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        ImgView = findViewById(R.id.ImgView);
        tvTitle = findViewById(R.id.tvTitle);
        tvMarks = findViewById(R.id.tvMarks);
        tvTotalQuestions = findViewById(R.id.tvTotalQuestions);
        tvCorrectAnswers = findViewById(R.id.tvCorrectAnswers);
        tvInCorrectAnswers = findViewById(R.id.tvInCorrectAnswers);
        lstAnswers = findViewById(R.id.lstAnswers);

        Load_Result();
    }

    void Load_Result() {
        String URL = IP.BASE_URL + "AttemptedQuizAnswers/Select_Quiz_Result";
        String Query = "?Quiz_Detail_Id=" + Quiz_Detail_Id;
        StringRequest request = new StringRequest(
                Request.Method.GET, URL + Query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            final String base64String = arr.getJSONObject(0).getString("Student_Image");
                            Bitmap bitmap = ImageUtil.convertToImage(base64String);
                            ImgView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 890, 890, false));

                            ImgView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog nagDialog = new Dialog(Quiz_Result_Activity.this);
                                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    nagDialog.setCancelable(false);
                                    nagDialog.setContentView(R.layout.preview_image);
                                    Button btn_close = (Button) nagDialog.findViewById(R.id.btn_close);
                                    ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.iv_preview_image);
                                    Bitmap fullImage = ImageUtil.convertToImage(base64String);
                                    //    ivPreview.setImageBitmap(fullImage);
                                    ivPreview.setImageBitmap(Bitmap.createScaledBitmap(fullImage, 450, 650, false));

                                    btn_close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            nagDialog.dismiss();
                                        }
                                    });
                                    nagDialog.show();
                                }
                            });

                            int TotalQuestion = arr.length();
                            tvTitle.setText("Title : " + arr.getJSONObject(0).getString("Quiz_Title"));
                            tvTotalQuestions.setText("Questions : " + TotalQuestion);
                            int Correct = 0;
                            int Incorrect = 0;

                            final ArrayList<Quiz_Answers> Answers = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                int Mark = Integer.parseInt(obj.getString("Mark"));
                                String Question = obj.getString("Question");
                                String Option1 = obj.getString("Option1");
                                String Option2 = obj.getString("Option2");
                                String Option3 = obj.getString("Option3");
                                String Answer = obj.getString("Answer");
                                String Correct_Answer = obj.getString("Correct_Answer");
                                int Question_No = obj.getInt("Question_No");

                                if (Mark == 0) {
                                    Incorrect++;
                                }
                                if (Mark == 1) {
                                    Correct++;
                                }
                                Quiz_Answers answer = new Quiz_Answers(Question_No, Question, Option1, Option2, Option3, Answer, Correct_Answer, Mark);
                                Answers.add(answer);
                            }

                            tvMarks.setText("Marks : " + Correct + "/" + TotalQuestion);
                            tvCorrectAnswers.setText("Correct Answer : " + Correct);
                            tvInCorrectAnswers.setText("Incorrect Answer : " + Incorrect);
                            Student_Answer_List_Adapter adapter = new Student_Answer_List_Adapter(Quiz_Result_Activity.this, Answers);
                            lstAnswers.setAdapter(adapter);

                        } catch (JSONException e) {
                            String Msg = "You Did not Attempt this Quiz";
                            Toasty.error(Quiz_Result_Activity.this, Msg, Toasty.LENGTH_LONG, true).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(Quiz_Result_Activity.this, error.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
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
        finish();
        if (SharedRef.getbackactivity().equalsIgnoreCase("Parent")) {
            SharedRef.savebackactivity("No");
            Intent intent = new Intent(Quiz_Result_Activity.this, StudentMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
