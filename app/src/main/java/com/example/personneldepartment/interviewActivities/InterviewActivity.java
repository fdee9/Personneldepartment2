package com.example.personneldepartment.interviewActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.personneldepartment.R;
import com.example.personneldepartment.models.Interview;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class InterviewActivity extends AppCompatActivity {
    private ListView lV_interview;
    private Button btn_addInterview;
    private Button btn_update;

    private String url;
    private Interview[] interviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        lV_interview = findViewById(R.id.lV_interviews);
        btn_addInterview = findViewById(R.id.btn_addInterview);
        btn_update = findViewById(R.id.btn_updateInterviews);

        final View.OnClickListener btnUpdateListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInterviews();
            }
        };
        btn_update.setOnClickListener(btnUpdateListener);

        final View.OnClickListener btnAddInterviewListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterviewActivity.this, AddInterviewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        };
        btn_addInterview.setOnClickListener(btnAddInterviewListener);

        final ListView.OnItemClickListener lVOnItemClickListener = new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(InterviewActivity.this, EditInterviewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("idInterview", interviews[position].getId());
                startActivity(intent);
            }
        };
        lV_interview.setOnItemClickListener(lVOnItemClickListener);

        url = getIntent().getExtras().getString("url");
        getInterviews();
    }

    private void getInterviews(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "Interviews", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        interviews = new Gson().fromJson(response.toString(), Interview[].class);

                        ArrayList<String> names = new ArrayList<>();
                        for(Interview interview : interviews){
                            interview.fixDateTime();
                            names.add(interview.getDateTimeString());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(InterviewActivity.this, R.layout.support_simple_spinner_dropdown_item, names);
                        lV_interview.setAdapter(arrayAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InterviewActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
}
