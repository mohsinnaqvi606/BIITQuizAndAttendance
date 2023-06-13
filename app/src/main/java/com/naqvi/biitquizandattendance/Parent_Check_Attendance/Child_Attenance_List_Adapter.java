package com.naqvi.biitquizandattendance.Parent_Check_Attendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.naqvi.biitquizandattendance.Parent_Main.Child;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;

import java.util.ArrayList;

public class Child_Attenance_List_Adapter extends ArrayAdapter<Child> {
    ArrayList<Child> Childs;
    Context context;
    SharedReference SharedRef;

    public Child_Attenance_List_Adapter(Context context, ArrayList<Child> courses) {
        super(context, R.layout.student_courses_list_item, courses);
        this.Childs = courses;
        this.context = context;
        SharedRef = new SharedReference(context);
    }

    @Override
    public int getCount() {
        return Childs.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.attendance_list_item, parent, false);

        TextView tvcourse = view.findViewById(R.id.tvName);
        TextView tvcourse_no = view.findViewById(R.id.tvAridNo);
        TextView tvAttendancePercentage = view.findViewById(R.id.tvAttendancePercentage);
        RelativeLayout layout = view.findViewById(R.id.parent_layout);

        final Child c = Childs.get(position);

        tvcourse.setText("Course : " + c.Name);
        tvcourse_no.setText("Code : " + c.Reg_No);
        tvAttendancePercentage.setText(c.child_class + "%");
        try {
            int per = Integer.parseInt(c.child_class);
            if (per < 50) {
                layout.setBackgroundColor(Color.RED);
                tvcourse.setTextColor(Color.WHITE);
                tvcourse_no.setTextColor(Color.WHITE);
                tvAttendancePercentage.setTextColor(Color.WHITE);

            }

        } catch (Exception ex) {
            tvAttendancePercentage.setText("0%");
        }

        return view;
    }
}
