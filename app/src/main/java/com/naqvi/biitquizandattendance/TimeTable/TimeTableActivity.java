package com.naqvi.biitquizandattendance.TimeTable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.naqvi.biitquizandattendance.LoginActivity;
import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;
import com.naqvi.biitquizandattendance.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TimeTableActivity extends AppCompatActivity {

    SharedReference SharedRef;
    ExpandableListView mainList;

    HashMap<String, ArrayList<TimeTable>> listChild;
    ArrayList<String> listHeader;
    TimeTableListAdapter myadapter;
    TimeTableListData timeTableListData;
    ActionBar actionBar;
    int day;
    String Name;
    TextView textView;
    int time = 2000;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        mainList = findViewById(R.id.mainList);
        SharedRef = new SharedReference(getApplicationContext());
        Name = SharedRef.getUser().Name;


        actionBar = getSupportActionBar();
        actionBar.setTitle(Name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        textView = findViewById(R.id.textView);

        handler = new Handler();

        timeTableListData = new TimeTableListData(getApplicationContext());
        mainList = findViewById(R.id.mainList);
        listChild = timeTableListData.getData();
        listHeader = new ArrayList<String>();
        listHeader.add("Monday");
        listHeader.add("Tuesday");
        listHeader.add("Wednesday");
        listHeader.add("Thursday");
        listHeader.add("Friday");

        myadapter = new TimeTableListAdapter(TimeTableActivity.this, listHeader, listChild);
        mainList.setAdapter(myadapter);

        mainList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                expandOrCollaspeList(groupPosition);
                String Key = null;

                switch (groupPosition) {
                    case 0:
                        Key = "Monday";
                        break;
                    case 1:
                        Key = "Tuesday";
                        break;
                    case 2:
                        Key = "Wednesday";
                        break;
                    case 3:
                        Key = "Thursday";
                        break;
                    case 4:
                        Key = "Friday";
                        break;

                }
                ArrayList<TimeTable> lst = listChild.get(Key);
                if (lst.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Time Table Available for " + Key, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }


    void expandOrCollaspeList(int groupPosition) {
        if (mainList.isGroupExpanded(groupPosition)) {
            mainList.collapseGroup(groupPosition);
        } else {
            mainList.collapseGroup(0);
            mainList.collapseGroup(1);
            mainList.collapseGroup(2);
            mainList.collapseGroup(3);
            mainList.collapseGroup(4);
            mainList.expandGroup(groupPosition);
        }
    }

    Runnable expandGroup = new Runnable() {
        @Override
        public void run() {
            time -= 500;
            if (time < 500) {

                Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_WEEK);  // int value
                expandList(day);
                handler.removeCallbacks(this);
                return;
            }
            handler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        expandGroup.run();
        TimeOnActionBar.run();
    }


    void expandList(int day) {
        if (day == 2) {
            mainList.expandGroup(0);
        } else if (day == 3) {
            mainList.expandGroup(1);
        } else if (day == 4) {
            mainList.expandGroup(2);
        } else if (day == 5) {
            mainList.expandGroup(3);
        } else if (day == 6) {
            mainList.expandGroup(4);
        }
    }

    Runnable TimeOnActionBar = new Runnable() {
        @Override
        public void run() {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm:a");
            String date = dateFormat.format(calendar.getTime());
            String time = timeFormat.format(calendar.getTime());

            String Title = Name + "   (" + date + ")";
            actionBar.setTitle(Title);

            handler.postDelayed(this, 1000);
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
        if (item.getItemId() == R.id.logout){
            logout_button_click();
        }
        return super.onOptionsItemSelected(item);
    }

    void logout_button_click() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to Logout?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedRef.saveUser(new User("","","",""));
                Intent intent = new Intent(TimeTableActivity.this, LoginActivity.class);
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
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

}
