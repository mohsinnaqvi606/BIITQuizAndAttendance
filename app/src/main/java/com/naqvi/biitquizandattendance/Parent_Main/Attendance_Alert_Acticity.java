package com.naqvi.biitquizandattendance.Parent_Main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CheckBox;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Teacher_Check_Attendance.Attendance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Attendance_Alert_Acticity extends AppCompatActivity {

    SharedReference SharedRef;
    RequestQueue queue;
    String URL;
    Handler handler = new Handler();
    ListView lst_View;
    CheckBox cb_attendance;
    Boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance__alert__acticity);

        SharedRef = new SharedReference(this);
        queue = Volley.newRequestQueue(this);

        String Name = SharedRef.getUser().Name;
        String title = Name;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        lst_View = findViewById(R.id.lst_View);

        get_Alert();
        Change_Notification_Seen_Status();
    }

    void get_Alert() {
        URL = IP.BASE_URL + "Parent/Select_Alerts_By_Number";
        String Number = SharedRef.getUser().UserName;
        String Query = "?Number=" + Number;
        final ArrayList<String> msgs = new ArrayList<>();

        StringRequest request = new StringRequest(
                Request.Method.GET, URL + Query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String msg = obj.optString("Message");
                                msgs.add(msg);
                            }
                            Alert_List_Adapter adapter = new Alert_List_Adapter(Attendance_Alert_Acticity.this, msgs);
                            lst_View.setAdapter(adapter);

                        } catch (JSONException e) {
                            Toasty.error(Attendance_Alert_Acticity.this, "No New Alert", Toasty.LENGTH_LONG, false).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(Attendance_Alert_Acticity.this, error.getMessage(), Toasty.LENGTH_LONG, false).show();
            }
        });
        queue.add(request);
    }


    void Change_Notification_Seen_Status() {
        String Phone = SharedRef.getUser().UserName;
        String URL = IP.BASE_URL + "Parent/Change_Notification_Seen_Status";
        String query = "?Phone=" + Phone;

        try {
            StringRequest request = new StringRequest(
                    Request.Method.PUT, URL + query,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String Msg = response.replace("\"", "");
                            if (Msg.equals("Status Changed")) {
                                //    Toasty.error(.this, response, Toasty.LENGTH_LONG, true).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toasty.error(Attendance_Alert_Acticity.this, error.getMessage(), Toasty.LENGTH_LONG, true).show();
                }
            });

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Attendance_Alert_Acticity.this, Parent_Main_Activity.class);
        startActivity(intent);
    }
}
