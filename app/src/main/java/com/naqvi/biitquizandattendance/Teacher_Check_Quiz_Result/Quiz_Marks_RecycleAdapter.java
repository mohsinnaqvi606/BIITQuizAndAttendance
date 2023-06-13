package com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.ImageUtil;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.Quiz_Pin_Detail;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Quiz_Marks_RecycleAdapter extends RecyclerView.Adapter<Quiz_Marks_RecycleAdapter.AttendanceViewHolder> {

    public ArrayList<Quiz_Marks> marks_List;
    public Context context;
    SharedReference SharedRef;

    public Quiz_Marks_RecycleAdapter(Context context, ArrayList<Quiz_Marks> marks_List) {
        this.marks_List = marks_List;
        this.context = context;
        SharedRef = new SharedReference(context);
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.quiz_marks_item, parent, false);
        AttendanceViewHolder holder = new AttendanceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder holder, final int position) {
        final Quiz_Marks quiz = marks_List.get(position);

        holder.tv_aridNo.setText(quiz.Reg_No);
        holder.tv_user_name.setText(quiz.Name);
        holder.tv_marks.setText(quiz.Marks);
        if (!quiz.Image.equalsIgnoreCase("NILL")) {
            Bitmap img = ImageUtil.convertToImage(quiz.Image);
            holder.Img.setImageBitmap(img);
        } else {
            holder.Img.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quiz_Marks q = marks_List.get(position);
                if (!q.Image.equalsIgnoreCase("NILL")) {
                    final Dialog nagDialog = new Dialog(context);
                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    nagDialog.setCancelable(false);
                    nagDialog.setContentView(R.layout.preview_image);
                    Button btn_close = (Button) nagDialog.findViewById(R.id.btn_close);
                    ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.iv_preview_image);
                    Bitmap fullImage = ImageUtil.convertToImage(quiz.Image);
                    ivPreview.setImageBitmap(Bitmap.createScaledBitmap(fullImage, 1140, 1400, false));



                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nagDialog.dismiss();
                        }
                    });
                    nagDialog.show();
                } else {
                    Toasty.normal(context, q.Name + " have not submit the quiz", Toasty.LENGTH_LONG).show();
                }
            }
        });

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quiz.Image.equalsIgnoreCase("NILL")) {
                    Toasty.normal(context, quiz.Name + " have not submit the quiz", Toasty.LENGTH_LONG).show();
                } else {
                    String Quiz_Detail_Id = marks_List.get(position).Quiz_Detail_Id;
                    SharedRef.saveQuizPinDetail(new Quiz_Pin_Detail(Quiz_Detail_Id, null, null, null, null));
                    Intent intent = new Intent(context, Quiz_Answers_Activity.class);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return marks_List.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        ImageView Img;
        TextView tv_aridNo;
        TextView tv_user_name;
        TextView tv_marks;
        LinearLayout parent_layout;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_aridNo = itemView.findViewById(R.id.tv_aridNo);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_marks = itemView.findViewById(R.id.tv_marks);
            Img = itemView.findViewById(R.id.Img);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

