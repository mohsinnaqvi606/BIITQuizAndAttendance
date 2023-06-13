package com.naqvi.biitquizandattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naqvi.biitquizandattendance.Parent_Main.Parent_Main_Activity;
import com.naqvi.biitquizandattendance.Student_Main.StudentMainActivity;
import com.naqvi.biitquizandattendance.TimeTable.TimeTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    EditText etUserName;
    EditText etPassword;
    RequestQueue queue;
    SharedReference SharedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);

        queue = Volley.newRequestQueue(this);
        SharedRef = new SharedReference(getApplicationContext());

        String Type = SharedRef.getUser().Type;

        if (Type.equals("Teacher")) {
            Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (Type.equals("Student")) {
            Intent intent = new Intent(getApplicationContext(), StudentMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else if (Type.equals("Parent")) {
            Intent intent = new Intent(getApplicationContext(), Parent_Main_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    public void buLogin(View view) {
        String UserName = etUserName.getText().toString();
        String Password = etPassword.getText().toString();

        if (UserName.equals("") || Password.equals("")) {

            Toasty.info(getApplicationContext(), "Enter UserName and Password", Toast.LENGTH_LONG, true).show();
        } else {
            String URL = IP.BASE_URL + "Users/Select_User_By_Id";
            String query = "?UserName=" + UserName + "&Password=" + Password;

            StringRequest request = new StringRequest(
                    Request.Method.GET, URL + query,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray arr = new JSONArray(response);
                                JSONObject obj = arr.getJSONObject(0);
                                String UserName = obj.getString("User_Name").trim();
                                String Password = obj.getString("Password").trim();
                                String Type = obj.getString("Type").trim();
                                String Name = obj.getString("Name").trim();

                                if (UserName.equals(etUserName.getText().toString()) && Password.equals(etPassword.getText().toString())) {
                                    //Save Data in Shared Refference
                                    SharedRef.saveUser(new User(Name, UserName, Password, Type));
                                    //   SharedRef.saveName(Name);

                                    if (Type.equals("Teacher")) {
                                        //Teacher Login
                                        Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } else if(Type.equals("Student")) {
                                        // Course Login
                                        Intent intent = new Intent(getApplicationContext(), StudentMainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else if(Type.equalsIgnoreCase("Parent")) {
                                        // Course Login
                                        Intent intent = new Intent(getApplicationContext(), Parent_Main_Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toasty.error(getApplicationContext(), "incorrect UserName or Password", Toast.LENGTH_LONG, true).show();
                                }
                            } catch (JSONException e) {
                                String Msg = response.replace("\"","");
                                Toasty.error(getApplicationContext(), Msg, Toast.LENGTH_LONG, true).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            queue.add(request);
        }
    }


}
