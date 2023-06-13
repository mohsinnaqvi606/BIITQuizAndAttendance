package com.naqvi.biitquizandattendance.Student_Check_Attendance;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.ImageUtil;
import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.Quiz_Pin_Detail;
import com.naqvi.biitquizandattendance.Student_Quiz_Result.Quiz_RecycleAdapter;
import com.naqvi.biitquizandattendance.Student_Quiz_Result.Quiz_Result_Activity;
import com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result.Quiz;

import java.util.ArrayList;

public class Attendance_RecycleView_Adapter extends RecyclerView.Adapter<Attendance_RecycleView_Adapter.ViewHolder> {
    ArrayList<Attendance_Date> Attendance_list;
    Context context;
    SharedReference SharedRef;

    public Attendance_RecycleView_Adapter(Context context, ArrayList<Attendance_Date> Attendance_list) {
        this.Attendance_list = Attendance_list;
        this.context = context;
        SharedRef = new SharedReference(context);
    }

    @Override
    public int getItemCount() {
        return Attendance_list.size();
    }

    @Override
    public Attendance_RecycleView_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.attendance_date_item, parent, false);
        Attendance_RecycleView_Adapter.ViewHolder holder = new Attendance_RecycleView_Adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Attendance_RecycleView_Adapter.ViewHolder holder, int position) {
        final Attendance_Date s = Attendance_list.get(position);

        holder.tv_date.setText(s.Date);
        holder.tv_time.setText(s.Time);
        holder.tv_attendance.setText(s.Attendance);

        if (s.Attendance.equalsIgnoreCase("Absent")) {
            holder.tv_attendance.setTextColor(Color.RED);
            holder.Img.setImageResource(R.drawable.ic_launcher_background);
        } else {
            holder.tv_attendance.setTextColor(Color.BLACK);
            Bitmap img = ImageUtil.convertToImage(s.Image);
            holder.Img.setImageBitmap(img);
            holder.Img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog nagDialog = new Dialog(context);
                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    nagDialog.setCancelable(false);
                    nagDialog.setContentView(R.layout.preview_image);
                    Button btn_close = (Button) nagDialog.findViewById(R.id.btn_close);
                    ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.iv_preview_image);
                    Bitmap fullImage = ImageUtil.convertToImage(s.Image);
                    ivPreview.setImageBitmap(fullImage);

                    final String type = SharedRef.getUser().Type;
                    ivPreview.setImageBitmap(Bitmap.createScaledBitmap(fullImage, 450, 650, false));

                    if(type.equalsIgnoreCase("Teacher")){
                        ivPreview.setImageBitmap(Bitmap.createScaledBitmap(fullImage, 1140, 1400, false));
                    }

                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nagDialog.dismiss();
                        }
                    });
                    nagDialog.show();
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_time, tv_attendance;
        ImageView Img;

        public ViewHolder(@NonNull View view) {
            super(view);
            tv_date = view.findViewById(R.id.tv_date);
            tv_time = view.findViewById(R.id.tv_time);
            tv_attendance = view.findViewById(R.id.tv_attendance);
            Img = view.findViewById(R.id.Img);
        }
    }
}
