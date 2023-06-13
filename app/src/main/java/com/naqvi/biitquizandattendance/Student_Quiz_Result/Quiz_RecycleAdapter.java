package com.naqvi.biitquizandattendance.Student_Quiz_Result;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.Quiz_Pin_Detail;
import com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result.Quiz;
import com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result.Quiz_Marks_Activity;

import java.util.ArrayList;

public class Quiz_RecycleAdapter extends RecyclerView.Adapter<Quiz_RecycleAdapter.AttendanceViewHolder> {

    public ArrayList<Quiz> quiz_List;
    public Context context;
    SharedReference SharedRef;

    public Quiz_RecycleAdapter(Context context, ArrayList<Quiz> quiz_List) {
        this.quiz_List = quiz_List;
        this.context = context;
        SharedRef = new SharedReference(context);
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.quiz_recycle_item, parent, false);
        AttendanceViewHolder holder = new AttendanceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder holder, int position) {
        final Quiz quiz = quiz_List.get(position);


        String q_no = (position + 1) + ")";
        holder.tv_quiz_no.setText(q_no);
        String Date = "Date : " + quiz.Quiz_Date;
        String Time = "Time : " + quiz.Time;
        holder.tv_quiz_title.setText(quiz.Quiz_Tile);
        holder.tv_quiz_date.setText(Date);
        holder.tv_quiz_time.setText(Time);

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedRef.saveQuizPinDetail(new Quiz_Pin_Detail(quiz.Quiz_Id, quiz.Quiz_Id, null, null, null));
                Intent intent = new Intent(context, Quiz_Result_Activity.class);
                intent.putExtra("Date", quiz.Quiz_Date);
                intent.putExtra("Time", quiz.Time);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return quiz_List.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tv_quiz_no;
        TextView tv_quiz_title;
        TextView tv_quiz_date, tv_quiz_time;
        LinearLayout parent_layout;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_quiz_no = itemView.findViewById(R.id.tv_quiz_no);
            tv_quiz_title = itemView.findViewById(R.id.tv_quiz_title);
            tv_quiz_date = itemView.findViewById(R.id.tv_quiz_date);
            tv_quiz_time = itemView.findViewById(R.id.tv_quiz_time);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

