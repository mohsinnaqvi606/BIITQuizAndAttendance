package com.naqvi.biitquizandattendance.TimeTable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.naqvi.biitquizandattendance.Quiz.CreateQuizActivity;
import com.naqvi.biitquizandattendance.Quiz.Show_All_Quiz_Activity;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SectiomAndSubject;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Quiz_Result.Show_Quiz_List_Activity;
import com.naqvi.biitquizandattendance.Teacher_Check_Attendance.Show_Attendance_Activity;
import com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result.Quiz_List_Activity;
import com.naqvi.biitquizandattendance.Teacher_Take_Attendance.Teacher_Take_Attendance_Activity;

import java.util.ArrayList;
import java.util.HashMap;


public class TimeTableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String, ArrayList<TimeTable>> childtitles;
    ArrayList<String> headertitles;
    SharedReference SharedRef;

    public TimeTableListAdapter(Context context, ArrayList<String> headertitles, HashMap<String, ArrayList<TimeTable>> childtitles) {
        this.context = context;
        this.childtitles = childtitles;
        this.headertitles = headertitles;
        SharedRef = new SharedReference(context);
    }

    @Override
    public int getGroupCount() {
        return this.headertitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childtitles.get(this.headertitles.get(groupPosition)).size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return this.headertitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childtitles.get(this.headertitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String listTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.time_table_main_list_item,null);
//            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.time_table_main_list_item, null);
        }

        TextView listTitleTextView =  convertView.findViewById(R.id.title);
        listTitleTextView.setText(listTitle);



        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final TimeTable expandedListText = (TimeTable) getChild(groupPosition, childPosition);

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.timetable_detail_for_day,null);
//            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.timetable_detail_for_day, null);
        }

        TextView tvSection = (TextView) convertView.findViewById(R.id.tvSection);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvCourse = (TextView) convertView.findViewById(R.id.tvCourse);
        TextView tvRoom = (TextView) convertView.findViewById(R.id.tvRoom);
        ConstraintLayout lecture = (ConstraintLayout) convertView.findViewById(R.id.lectureDetail);

        tvSection.setText("Class : " + expandedListText.Section);
        tvTime.setText("Time : " + expandedListText.Time);
        tvCourse.setText("Course : " + expandedListText.Course);
        tvRoom.setText("Room : " + expandedListText.Room);

        lecture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String Section = expandedListText.Section.trim();
                String Subject = expandedListText.Course.trim();
                String Course_No = expandedListText.Course_No.trim();
                String Lt_No = expandedListText.Room.trim();
                SharedRef.saveSectionAndSubject(new SectiomAndSubject(Section, Subject));
                SharedRef.saveCourse_No(Course_No);
                SharedRef.saveLt_No(Lt_No);


                final CharSequence options[] = {"Create Quiz","Take Quiz","Mark Attendance","Attendance Detail","Quiz Result"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Menu");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which].equals("Create Quiz")){
                        //    ((Activity)context).finish();
                            Intent intent = new Intent(context, CreateQuizActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        else if(options[which].equals("Take Quiz")){
                         //   ((Activity)context).finish();
                            Intent intent = new Intent(context, Show_All_Quiz_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        else if(options[which].equals("Mark Attendance")){
                          //  ((Activity)context).finish();
                            Intent intent = new Intent(context, Teacher_Take_Attendance_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        else if (options[which].equals("Attendance Detail")){
                         //   ((Activity)context).finish();
                            Intent intent = new Intent(context, Show_Attendance_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        else if(options[which].equals("Quiz Result")){
                          //  ((Activity)context).finish();
                            Intent intent = new Intent(context, Quiz_List_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
