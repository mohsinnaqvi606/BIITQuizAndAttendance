package com.naqvi.biitquizandattendance.Student_Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SectiomAndSubject;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Check_Attendance.Show_Attendance_Activity;
import com.naqvi.biitquizandattendance.Student_Quiz_Result.Show_Quiz_List_Activity;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Student_Courses_List_Adapter extends ArrayAdapter<Course> {
    ArrayList<Course> courses;
    Context context;
    SharedReference SharedRef;

    public Student_Courses_List_Adapter(Context context, ArrayList<Course> courses) {
        super(context, R.layout.student_courses_list_item, courses);
        this.courses = courses;
        this.context = context;
        SharedRef = new SharedReference(context);
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.student_courses_list_item,parent,false);

        TextView tvCourseTitle = view.findViewById(R.id.tvCourseTitle);
        TextView tvCourseNo = view.findViewById(R.id.tvCourseNo);
        TextView tvTeacher = view.findViewById(R.id.tvTeacher);
        LinearLayout layout = view.findViewById(R.id.parent_layout);

        final Course s = courses.get(position);

        tvCourseTitle.setText("COURSE : "+s.Course);
        tvCourseNo.setText("COURSE NO : "+s.Course_No);
        tvTeacher.setText("TEACHER : "+s.Teacher_Name);


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Section = SharedRef.getSectionAndSubject().Section;
                SharedRef.saveSectionAndSubject(new SectiomAndSubject(Section,s.Course));
                SharedRef.saveTeacher(s.Teacher_Name);
                SharedRef.saveCourse_No(s.Course_No);

                final CharSequence options[] = {"Attendance Detail","Quiz Result"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Menu");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which].equals("Attendance Detail")){
                          //  ((Activity)context).finish();
                            Intent intent = new Intent(context, Show_Attendance_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        if(options[which].equals("Quiz Result")){
                          //  ((Activity)context).finish();
                            Intent intent = new Intent(context, Show_Quiz_List_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });

        return view;
    }
}
