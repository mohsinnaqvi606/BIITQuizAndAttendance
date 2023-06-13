package com.naqvi.biitquizandattendance.Quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.TimeTable.TimeTableActivity;

public class CreateQuizActivity extends AppCompatActivity {

    ActionBar actionBar;
    TextView tvTotalQuestion;
    TextView tvQuizTitle;
    TextView tvTime;
    TextView tvSubject;

    SharedReference SharedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        SharedRef = new SharedReference(this);
        String Teacher = SharedRef.getUser().Name + "   -   Create Quiz";
        actionBar = getSupportActionBar();
        actionBar.setTitle(Teacher);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvQuizTitle = findViewById(R.id.tvQuizTitle);
        tvTotalQuestion = findViewById(R.id.tvTotalQuestions);
        tvTime = findViewById(R.id.tvTime);
        tvSubject = findViewById(R.id.tvSubject);

        String Subject = SharedRef.getSectionAndSubject().Subject;

        tvSubject.setText(Subject);
        tvSubject.setEnabled(false);

    }

    public void buCancel(View view) {
        go_Back();
    }

    public void buNext(View view) {
        String quizTitle = tvQuizTitle.getText().toString().trim();
        String totalQuestion = tvTotalQuestion.getText().toString().trim();
        String time = tvTime.getText().toString().trim();

        if (quizTitle.isEmpty() || totalQuestion.isEmpty() || time.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Fill All Fields", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), CreateQuestionsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("quizTitle", quizTitle);
            intent.putExtra("totalQuestion", Integer.parseInt(totalQuestion));
            intent.putExtra("time", Integer.parseInt(time));
            startActivity(intent);
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
        Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
