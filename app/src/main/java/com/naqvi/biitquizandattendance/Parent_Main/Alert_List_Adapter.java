package com.naqvi.biitquizandattendance.Parent_Main;

import android.content.Context;
import android.content.Intent;
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

public class Alert_List_Adapter extends ArrayAdapter<String> {
    ArrayList<String> msgs;
    Context context;
    SharedReference SharedRef;

    public Alert_List_Adapter(Context context, ArrayList<String> msgs) {
        super(context, R.layout.notification_item, msgs);
        this.msgs = msgs;
        this.context = context;
        SharedRef = new SharedReference(context);
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notification_item, parent, false);

        TextView tvCourseTitle = view.findViewById(R.id.tvmessage);
        String msg = msgs.get(position);
        tvCourseTitle.setText(msg);
        return view;
    }
}
