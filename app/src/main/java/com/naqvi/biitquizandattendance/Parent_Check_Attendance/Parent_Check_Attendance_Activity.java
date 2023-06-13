package com.naqvi.biitquizandattendance.Parent_Check_Attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.Parent_Main.Child;
import com.naqvi.biitquizandattendance.Parent_Main.Child_List_Adapter;
import com.naqvi.biitquizandattendance.Parent_Main.Parent_Main_Activity;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Parent_Check_Attendance_Activity extends AppCompatActivity {

    ListView Attendance_lst_View;
    SharedReference SharedRef;
    RequestQueue queue;
    String URL;
    String Name;
    String Reg_No;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_check_attendance);
        SharedRef = new SharedReference(this);
        Attendance_lst_View = findViewById(R.id.Attendance_lst_View);
        textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        Name = intent.getStringExtra("Name");
        Reg_No = intent.getStringExtra("Reg_No");

        textView.setText(Name+"  (Attendance)");
        String Name = SharedRef.getUser().Name;
        String title = Name;
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        get_Attendance();
    }

    void get_Attendance() {
        queue = Volley.newRequestQueue(this);
        String Number = SharedRef.getUser().UserName;
        URL = IP.BASE_URL + "Parent/Select_Attendance_By_Number";
        String Query = "?Number=" + Number + "&Reg_No=" + Reg_No;
        final ArrayList<Child> childList = new ArrayList<>();

        StringRequest request = new StringRequest(
                Request.Method.GET, URL + Query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String Name = obj.getString("Course");
                                String Reg_No = obj.getString("Course_No");
                                String child_class = obj.getString("Percentage");

                                Child c = new Child(Name, Reg_No, child_class);
                                childList.add(c);
                            }
                            Child_Attenance_List_Adapter adapter = new Child_Attenance_List_Adapter(Parent_Check_Attendance_Activity.this, childList);
                            Attendance_lst_View.setAdapter(adapter);
                            //   check_For_Attendance.run();

                        } catch (JSONException e) {
                            Toasty.error(Parent_Check_Attendance_Activity.this, e.getMessage(), Toasty.LENGTH_LONG, false).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(Parent_Check_Attendance_Activity.this, error.getMessage(), Toasty.LENGTH_LONG, false).show();
            }
        });
        queue.add(request);
    }
}
