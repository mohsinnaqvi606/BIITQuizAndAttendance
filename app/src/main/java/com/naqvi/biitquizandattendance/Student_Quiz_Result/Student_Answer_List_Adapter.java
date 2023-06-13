package com.naqvi.biitquizandattendance.Student_Quiz_Result;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.naqvi.biitquizandattendance.Quiz.Quiz;
import com.naqvi.biitquizandattendance.R;

import java.util.ArrayList;

public class Student_Answer_List_Adapter extends ArrayAdapter<Quiz_Answers> {

    Context context;
    ArrayList<Quiz_Answers> Answers;


    public Student_Answer_List_Adapter(@NonNull Context context, ArrayList<Quiz_Answers> Answers) {
        super(context, R.layout.answers_list_item);
        this.context = context;
        this.Answers = Answers;
    }

    @Override
    public int getCount() {
        return Answers.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.answers_list_item, parent, false);
        TextView tvQuestion, tvOption1, tvOption2, tvOption3;
        ImageView imgView1, imgView2, imgView3;
        ConstraintLayout parent_layout = view.findViewById(R.id.parent_layout);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvOption1 = view.findViewById(R.id.tvOption1);
        tvOption2 = view.findViewById(R.id.tvOption2);
        tvOption3 = view.findViewById(R.id.tvOption3);

        imgView1 = view.findViewById(R.id.imgView1);
        imgView2 = view.findViewById(R.id.imgView2);
        imgView3 = view.findViewById(R.id.imgView3);

        Quiz_Answers answer = Answers.get(position);

        tvQuestion.setText("Q#"+answer.Question_No+") "+ answer.Question);
        tvOption1.setText("a) " + answer.Option1);
        tvOption2.setText("b) " + answer.Option2);
        tvOption3.setText("c) " + answer.Option3);

        if (answer.Mark == 1) {
            if (answer.Answer.equals(answer.Option1)) {
                imgView1.setImageResource(R.drawable.ic_true);
            }

            if (answer.Answer.equals(answer.Option2)) {
                imgView2.setImageResource(R.drawable.ic_true);
            }

            if (answer.Answer.equals(answer.Option3)) {
                imgView3.setImageResource(R.drawable.ic_true);
            }
        }
        else {
            if (answer.Answer.equals("")) {
                parent_layout.setBackgroundColor(Color.LTGRAY);
                if (answer.Correct_Answer.equals(answer.Option1)) {
                    imgView1.setImageResource(R.drawable.ic_true);
                }

                if (answer.Correct_Answer.equals(answer.Option2)) {
                    imgView2.setImageResource(R.drawable.ic_true);
                }

                if (answer.Correct_Answer.equals(answer.Option3)) {
                    imgView3.setImageResource(R.drawable.ic_true);
                }
            }

            else if (answer.Option1.equals(answer.Correct_Answer)) {
                imgView1.setImageResource(R.drawable.ic_true);

                if (answer.Answer.equals(answer.Option2)) {
                    imgView2.setImageResource(R.drawable.ic_false);
                }

                if (answer.Answer.equals(answer.Option3)) {
                    imgView3.setImageResource(R.drawable.ic_false);
                }

            } else if (answer.Option2.equals(answer.Correct_Answer)) {
                imgView2.setImageResource(R.drawable.ic_true);


                if (answer.Answer.equals(answer.Option1)) {
                    imgView1.setImageResource(R.drawable.ic_false);
                }

                if (answer.Answer.equals(answer.Option3)) {
                    imgView3.setImageResource(R.drawable.ic_false);
                }


            } else if (answer.Option3.equals(answer.Correct_Answer)) {
                imgView3.setImageResource(R.drawable.ic_true);


                if (answer.Answer.equals(answer.Option1)) {
                    imgView1.setImageResource(R.drawable.ic_false);
                }

                if (answer.Answer.equals(answer.Option2)) {
                    imgView2.setImageResource(R.drawable.ic_false);
                }

            }
        }


        return view;
    }


}
