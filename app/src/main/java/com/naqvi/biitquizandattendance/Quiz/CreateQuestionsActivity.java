package com.naqvi.biitquizandattendance.Quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.TimeTable.TimeTableActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class CreateQuestionsActivity extends AppCompatActivity {

    ArrayList<Quiz> questionList;
    SharedReference SharedRef;

    TextView tvQuestionNo;
    TextView tvQuestion;
    TextView tvOption1;
    TextView tvOption2;
    TextView tvOption3;
    RadioGroup radioGroup;
    String Answer;
    boolean isChecked;

    int totalQuestion;
    int time;
    int currentQuestionNumber;
    Button buNext;

    String QuizId;
    String TeacherId;
    String Quiz_Date;
    String Course_No;
    String Quiz_Title;
    String URL;

    RequestQueue queue;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_questions);

        SharedRef = new SharedReference(this);
        String Title = SharedRef.getUser().Name + "   -   Create Quiz";
        actionBar = getSupportActionBar();
        actionBar.setTitle(Title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        questionList = new ArrayList<>();
        URL = IP.BASE_URL;
        queue = Volley.newRequestQueue(this);

        isChecked = false;
        tvQuestionNo = findViewById(R.id.tvQuestionNo);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvOption1 = findViewById(R.id.tvOption1);
        tvOption2 = findViewById(R.id.tvOption2);
        tvOption3 = findViewById(R.id.tvOption3);
        radioGroup = findViewById(R.id.radioGroup);
        buNext = findViewById(R.id.buNext);

        TeacherId = SharedRef.getUser().UserName;
        Course_No = SharedRef.getCourse_No();
        Intent i = getIntent();
        Quiz_Title = i.getStringExtra("quizTitle");
        totalQuestion = i.getIntExtra("totalQuestion", 1);
        time = i.getIntExtra("time", 1);
        currentQuestionNumber = 1;

        if (totalQuestion == 1) {
            buNext.setText("Finish");
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rdOption1) {
                    int id1 = checkedId;
                    Answer = tvOption1.getText().toString().trim();
                } else if (checkedId == R.id.rdOption2) {
                    Answer = tvOption2.getText().toString().trim();
                } else if (checkedId == R.id.rdOption3) {
                    Answer = tvOption3.getText().toString().trim();
                }
                isChecked = true;
            }
        });
    }

    public void buNextClick(View view) {

        if (currentQuestionNumber <= totalQuestion) {
            // To check Radio Button is Selected or Not
            if (isChecked) {
                Quiz question = new Quiz();
                question.Question = tvQuestion.getText().toString().trim();
                question.Option1 = tvOption1.getText().toString().trim();
                question.Option2 = tvOption2.getText().toString().trim();
                question.Option3 = tvOption3.getText().toString().trim();
                question.Answer = Answer;
                question.Quiz_Title = Quiz_Title;
                question.Time = time + "";
                questionList.add(question);

                currentQuestionNumber++;
                if (currentQuestionNumber <= totalQuestion) {
                    tvQuestionNo.setText("Question No. # " + currentQuestionNumber);
                    resetFields();
                }

                // For Last Question
                if (currentQuestionNumber == totalQuestion) {
                    buNext.setText("Finish");
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please Select Correct Answer", Toast.LENGTH_LONG).show();
            }
        }

        if (currentQuestionNumber == (totalQuestion + 1) && isChecked) {
            Insert_Quiz_Detail();

            final SharedReference SharedRef = new SharedReference(getApplicationContext());
            SharedRef.saveCurrentQuizId(QuizId);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Quiz");
            builder.setMessage("Quiz Created...\nDo you Want to See It ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SharedRef.savebackactivity("Parent");
                    Intent intent = new Intent(getApplicationContext(), QuizDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            builder.show();
        }
    }

    public void resetFields() {
        radioGroup.clearCheck();
        tvQuestion.setText("");
        tvOption1.setText("");
        tvOption2.setText("");
        tvOption3.setText("");
        isChecked = false;
    }

    void Insert_Quiz_Detail() {

        generateQuizId();

        String values = "{ \"QuizId\":\"" + QuizId + "\"," +
                "\"TeacherId\":\"" + TeacherId + "\"," +
                "\"Course_No\":\"" + Course_No + "\"," +
                "\"Quiz_Title\":\"" + Quiz_Title + "\"," +
                "\"Total_Time\":\"" + time + "\"}";

        String URL = IP.BASE_URL + "QuizDetail/Insert_Quiz_Detail";

        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, URL,
                    new JSONObject(values), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String Msg = response.optString("Msg");
                    Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_LONG).show();

                    for (int i = 0; i < totalQuestion; i++) {
                        Insert_Quiz_Question(i);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error : " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void Insert_Quiz_Question(int Question_No) {
        String values = "{ \"QuizId\":\"" + QuizId + "\"," +
                "\"Question_No\":\"" + (Question_No + 1) + "\"," +
                "\"Question\":\"" + questionList.get(Question_No).Question + "\"," +
                "\"Option1\":\"" + questionList.get(Question_No).Option1 + "\"," +
                "\"Option2\":\"" + questionList.get(Question_No).Option2 + "\"," +
                "\"Option3\":\"" + questionList.get(Question_No).Option3 + "\"," +
                "\"Answer\":\"" + questionList.get(Question_No).Answer + "\"}";

        JsonObjectRequest request = null;
        try {
            request = new JsonObjectRequest(
                    Request.Method.POST, IP.BASE_URL + "QuizQuestions/Insert_Quiz_Question",
                    new JSONObject(values), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String Msg = response.optString("Msg");
          //          Toasty.success(CreateQuestionsActivity.this, Msg, Toast.LENGTH_LONG,true).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error : " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            );
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    void generateQuizId() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        String date = dateFormat.format(calendar.getTime());
        String time = timeFormat.format(calendar.getTime());

        Quiz_Date = date;
        QuizId = Course_No + "/" + date + "/" + time;
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
        Intent intent = new Intent(getApplicationContext(), CreateQuizActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
