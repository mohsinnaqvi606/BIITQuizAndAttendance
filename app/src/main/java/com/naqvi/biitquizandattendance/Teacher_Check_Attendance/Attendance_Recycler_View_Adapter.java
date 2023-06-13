package com.naqvi.biitquizandattendance.Teacher_Check_Attendance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.naqvi.biitquizandattendance.R;

import java.util.ArrayList;


public class Attendance_Recycler_View_Adapter extends RecyclerView.Adapter<Attendance_Recycler_View_Adapter.AttendanceViewHolder> {

    public ArrayList<Attendance> attendances;
    Context context;
    public Attendance_Recycler_View_Adapter(Context context, ArrayList<Attendance> attendances) {
        this.attendances = attendances;
        this.context = context;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.attendance_list_item, parent, false);
        AttendanceViewHolder holder = new AttendanceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder holder, final int position) {
        final Attendance attendance = attendances.get(position);

        final int attend = attendance.Percentage;

        holder.tvattendancePercentage.setTextColor(Color.BLACK);

        holder.tvAridNo.setText(attendance.AridNo);
        holder.tvName.setText(attendance.Name);
        holder.tvattendancePercentage.setText(attend + "%");

        if (attend < 50) {
            holder.tvattendancePercentage.setTextColor(Color.RED);
        }

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Show_Specific_Attendance_Activity.class);
                intent.putExtra("Name",attendance.Name);
                intent.putExtra("Reg_No",attendance.AridNo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendances.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvAridNo;
        TextView tvName;
        TextView tvattendancePercentage;
        RelativeLayout parent_layout;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAridNo = itemView.findViewById(R.id.tvAridNo);
            tvName = itemView.findViewById(R.id.tvName);
            tvattendancePercentage = itemView.findViewById(R.id.tvAttendancePercentage);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

