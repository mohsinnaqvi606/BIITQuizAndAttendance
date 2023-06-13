package com.naqvi.biitquizandattendance.Parent_Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.naqvi.biitquizandattendance.Parent_Check_Attendance.Parent_Check_Attendance_Activity;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;

import java.util.ArrayList;

public class Child_List_Adapter extends ArrayAdapter<Child> {
    ArrayList<Child> Childs;
    Context context;
    SharedReference SharedRef;

    public Child_List_Adapter(Context context, ArrayList<Child> courses) {
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
        View view = inflater.inflate(R.layout.student_courses_list_item, parent, false);

        TextView tvCourseTitle = view.findViewById(R.id.tvCourseTitle);
        TextView tvCourseNo = view.findViewById(R.id.tvCourseNo);
        TextView tvTeacher = view.findViewById(R.id.tvTeacher);
        LinearLayout layout = view.findViewById(R.id.parent_layout);

        final Child c = Childs.get(position);

        tvCourseTitle.setText("Name : " + c.Name);
        tvCourseNo.setText("Reg_No : " + c.Reg_No);
        tvTeacher.setText("Class : " +c.child_class);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Parent_Check_Attendance_Activity.class);
                intent.putExtra("Reg_No",c.Reg_No);
                intent.putExtra("Name",c.Name);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
