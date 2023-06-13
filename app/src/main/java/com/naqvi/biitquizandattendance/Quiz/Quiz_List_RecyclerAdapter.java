package com.naqvi.biitquizandattendance.Quiz;

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

import java.util.ArrayList;

public class Quiz_List_RecyclerAdapter extends RecyclerView.Adapter<Quiz_List_RecyclerAdapter.QuizViewHolder> {

    public ArrayList<Quiz_Detail> quizList;
    Context context;

    public Quiz_List_RecyclerAdapter(Context context, ArrayList<Quiz_Detail> quizList) {
        this.quizList = quizList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.quiz_list_layout, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuizViewHolder holder, final int position) {
        final Quiz_Detail quiz = quizList.get(position);

        String Time = "Time : "+quiz.Time+" min";
        String Questions = "Question : "+quiz.Total_Question;
        String Title = (position+1)+") "+quiz.Quiz_Title;
        holder.tvTitle.setText(Title);
        holder.tvTotalQuestion.setText(Questions);
        holder.tvTime.setText(Time);

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedReference SharedRef = new SharedReference(context);
                SharedRef.saveCurrentQuizId(quiz.QuizId);
                Intent intent = new Intent(context, QuizDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvTotalQuestion;
        TextView tvTime;
        LinearLayout parent_layout;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTotalQuestion = itemView.findViewById(R.id.tvTotalQuestion);
            tvTime = itemView.findViewById(R.id.tvTime);

            parent_layout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
