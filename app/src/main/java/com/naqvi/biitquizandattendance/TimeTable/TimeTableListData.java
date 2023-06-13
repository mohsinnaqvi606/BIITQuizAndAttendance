package com.naqvi.biitquizandattendance.TimeTable;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.IP;
import com.naqvi.biitquizandattendance.SharedReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeTableListData {

    Context context;
    RequestQueue queue;
    String URL;
    SharedReference SharedRef;
    String UserName;

    public TimeTableListData(Context context)
    {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        SharedRef = new SharedReference(context);
        UserName = SharedRef.getUser().UserName;
    }

    public HashMap<String, ArrayList<TimeTable>> getData(){

        HashMap<String,ArrayList<TimeTable>> expandaleListDetail  = new HashMap<String, ArrayList<TimeTable>>();
        ArrayList<TimeTable> Monday = getDay("Monday");
        ArrayList<TimeTable> Tuesday = getDay("Tuesday");
        ArrayList<TimeTable> Wednesday = getDay("Wednesday");
        ArrayList<TimeTable> Thursday = getDay("Thursday");
        ArrayList<TimeTable> Friday = getDay("Friday");

        expandaleListDetail.put("Monday",Monday);
        expandaleListDetail.put("Tuesday",Tuesday);
        expandaleListDetail.put("Wednesday",Wednesday);
        expandaleListDetail.put("Thursday",Thursday);
        expandaleListDetail.put("Friday",Friday);

        return expandaleListDetail;
    }

    public ArrayList<TimeTable> getDay(String Day){
        URL = IP.BASE_URL+"TimeTable/Select_Time_Table_By_Day";
        String query = "?Teacher="+UserName+"&Day="+Day;
        final ArrayList<TimeTable> timeTableList = new ArrayList<TimeTable>();
        StringRequest request = new StringRequest(
                Request.Method.GET, URL+query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for(int i=0;i<arr.length();i++)
                            {
                                JSONObject obj = arr.getJSONObject(i);
                                TimeTable table = new TimeTable();

                                table.Day = obj.getString("Day").trim();
                                table.Section = obj.getString("Class").trim();
                                table.Time = obj.getString("Time").trim();
                                table.Course_No = obj.getString("Course_No").trim();
                                table.Course = obj.getString("Course").trim();
                                table.Room = obj.getString("Room").trim();
                                timeTableList.add(table);
                            }

                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);
        return timeTableList;
    }
}
