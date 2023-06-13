package com.naqvi.biitquizandattendance.Parent_Main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.LoginActivity;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.Student_Main.StudentMainActivity;
import com.naqvi.biitquizandattendance.Student_Mark_Attendance.Student_Submit_Attendance_Activity;
import com.naqvi.biitquizandattendance.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class Parent_Main_Activity extends AppCompatActivity {

    SharedReference SharedRef;
    RequestQueue queue;
    String URL;
    Handler handler = new Handler();
    ListView lst_View;
    CheckBox cb_attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);

        SharedRef = new SharedReference(this);
        queue = Volley.newRequestQueue(this);

        String Name = SharedRef.getUser().Name;
        String title = Name;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        lst_View = findViewById(R.id.lst_View);
        cb_attendance = findViewById(R.id.cb_attendance);

        get_Childs();
        get_Status();

        cb_attendance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                check_for_attendance.run();
                chk_total_Alert.run();
                Change_Alert_Status("ON");


                Toasty.success(Parent_Main_Activity.this,"Attendance Alert is turned ON",Toasty.LENGTH_LONG).show();
            } else {

                Change_Alert_Status("OFF");
                handler.removeCallbacks(chk_total_Alert);
                handler.removeCallbacks(check_for_attendance);

                Toasty.success(Parent_Main_Activity.this,"Attendance Alert is turned OFF",Toasty.LENGTH_LONG).show();
            }
        });
    }

    void get_Childs() {
        URL = IP.BASE_URL + "Parent/Select_Childs_By_Number";
        String Number = SharedRef.getUser().UserName;
        String Query = "?Number=" + Number;
        final ArrayList<Child> childList = new ArrayList<>();

        StringRequest request = new StringRequest(
                Request.Method.GET, URL + Query,
                response -> {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            String Name = obj.getString("Student_Name");
                            String Reg_No = obj.getString("Reg_No");
                            String child_class = obj.getString("Student_Class");

                            Child c = new Child(Name, Reg_No, child_class);
                            childList.add(c);
                        }
                        Child_List_Adapter adapter = new Child_List_Adapter(Parent_Main_Activity.this, childList);
                        lst_View.setAdapter(adapter);

                    } catch (JSONException e) {
                        Toasty.error(Parent_Main_Activity.this, e.getMessage(), Toasty.LENGTH_LONG, false).show();
                    }
                }, error -> Toasty.error(Parent_Main_Activity.this, error.getMessage(), Toasty.LENGTH_LONG, false).show());
        queue.add(request);
    }

    void get_Status() {
        String Number = SharedRef.getUser().UserName;
        URL = IP.BASE_URL + "Parent/Get_Status";
        String Query = "?Number=" + Number;
        final ArrayList<Child> childList = new ArrayList<>();

        StringRequest request = new StringRequest(
                Request.Method.GET, URL + Query,
                response -> {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject obj = arr.getJSONObject(0);
                        String Alert_Status = obj.getString("Alert");
                        if (Alert_Status.equalsIgnoreCase("On")) {
                            cb_attendance.setChecked(true);
                            check_for_attendance.run();
                            chk_total_Alert.run();
                        }
                        //   check_For_Attendance.run();

                    } catch (JSONException e) {
                    //    Toasty.error(Parent_Main_Activity.this, e.getMessage(), Toasty.LENGTH_LONG, false).show();
                    }
                }, error -> Toasty.error(Parent_Main_Activity.this, error.getMessage(), Toasty.LENGTH_LONG, false).show());
        queue.add(request);
    }

    Runnable chk_total_Alert = new Runnable() {
        @Override
        public void run() {
            String Number = SharedRef.getUser().UserName;
            URL = IP.BASE_URL + "Parent/Select_Alerts_By_Number";
            String Query = "?Number=" + Number;
            StringRequest request = new StringRequest(
                    Request.Method.GET, URL + Query,
                    response -> {
                        try {
                            JSONArray arr = new JSONArray(response);

                            int count = arr.length();

                            if(count>0){
                                cb_attendance.setText("Attendance Alert ("+count+")");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, error -> Toasty.error(Parent_Main_Activity.this, error.getMessage(), Toasty.LENGTH_LONG, false).show());
            queue.add(request);
            handler.postDelayed(this,1000);
        }
    };

    Runnable check_for_attendance = new Runnable() {
        @Override
        public void run() {
            String Number = SharedRef.getUser().UserName;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String Date = df.format(calendar.getTime());
            URL = IP.BASE_URL + "Parent/Check_For_Alert";
            String Query = "?Number=" + Number+"&Date="+Date;
            StringRequest request = new StringRequest(
                    Request.Method.GET, URL + Query,
                    response -> {
                    }, error -> Toasty.error(Parent_Main_Activity.this, error.getMessage(), Toasty.LENGTH_LONG, false).show());
            queue.add(request);
            handler.postDelayed(this,1000);
        }
    };


    @Override
    public void onBackPressed() {
        go_Back();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            go_Back();
        }
        if (item.getItemId() == R.id.logout) {
            logout_button_click();
        }
        return super.onOptionsItemSelected(item);
    }

    void go_Back() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit from Application?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do No thing.... Continue...
            }
        });

        builder.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void logout_button_click() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to Logout?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  finish();

                SharedRef.saveUser(new User("", "", "", ""));
                Intent intent = new Intent(Parent_Main_Activity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do No thing.... Continue...
            }
        });

        builder.show();
    }


    void Change_Alert_Status(String Status) {
        String Phone = SharedRef.getUser().UserName;
        String URL = IP.BASE_URL + "Parent/Change_Alert_Status";
        String query = "?Status=" + Status + "&Phone=" + Phone;

        try {
            StringRequest request = new StringRequest(
                    Request.Method.PUT, URL + query,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String Msg = response.replace("\"", "");
                            if (Msg.equals("Status Changed")) {
                                Toasty.error(Parent_Main_Activity.this, response, Toasty.LENGTH_LONG, true).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toasty.error(Parent_Main_Activity.this, error.getMessage(), Toasty.LENGTH_LONG, true).show();
                }
            });

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        } catch (Exception e) {
            Toasty.error(Parent_Main_Activity.this, e.getMessage(), Toasty.LENGTH_LONG, true).show();
        }
    }

    public void notification_click(View view) {
        handler.removeCallbacks(check_for_attendance);
        handler.removeCallbacks(chk_total_Alert);
        finish();
        Intent intent = new Intent(Parent_Main_Activity.this, Attendance_Alert_Acticity.class);
        startActivity(intent);
    }
}
