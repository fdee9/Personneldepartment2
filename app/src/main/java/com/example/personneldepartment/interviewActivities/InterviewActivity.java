package com.example.personneldepartment.interviewActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.personneldepartment.models.SearchInterviewsByDate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class InterviewActivity extends AppCompatActivity {
    private ListView lV_interview;
    private EditText edTxt_startDate;
    private EditText edTxt_endDate;
    private Button btn_addInterview;
    private Button btn_update;
    private Button btn_findInterviews;

    private String url;
    private Interview[] interviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        lV_interview = findViewById(R.id.lV_interviews);
        edTxt_startDate = findViewById(R.id.edTxt_Startdate);
        edTxt_endDate = findViewById(R.id.edTxt_endDate);
        btn_addInterview = findViewById(R.id.btn_addInterview);
        btn_update = findViewById(R.id.btn_updateInterviews);
        btn_findInterviews = findViewById(R.id.btn_findInterviews);

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
        final View.OnClickListener edTxtDateOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePicker(view);
            }
        };
        edTxt_startDate.setOnClickListener(edTxtDateOnClickListener);
        edTxt_endDate.setOnClickListener(edTxtDateOnClickListener);
        final View.OnClickListener btnFindListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findInterviewsByDate();
            }
        };
        btn_findInterviews.setOnClickListener(btnFindListener);

        url = getIntent().getExtras().getString("url");
        getInterviews();
    }

    private void findInterviewsByDate(){
        SearchInterviewsByDate parameters = new SearchInterviewsByDate(
            edTxt_startDate.getText().toString(),
            edTxt_endDate.getText().toString()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONArray data = null;
        try {
            data = new JSONArray().put(new JSONObject(new Gson().toJson(parameters)));
        } catch (JSONException e) {
            Toast.makeText(InterviewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        JsonArrayRequest jsonObj = new JsonArrayRequest(Request.Method.POST, url + "Interviews/SearchByDate", data,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Interview[] interviews = new Gson().fromJson(response.toString(), Interview[].class);

                        ArrayList<String> names = new ArrayList<>();
                        for (Interview interview : interviews) {
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
        requestQueue.add(jsonObj);
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

    private void callDatePicker(View view) {
        final EditText edTxtDate = (EditText) view;
        // получаем текущую дату
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        // инициализируем диалог выбора даты текущими значениями
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String monthString;
                        if(monthOfYear < 9){
                            monthString = "0" + ++monthOfYear;
                        } else {
                            monthString = "" + ++monthOfYear;
                        }
                        String dayString;
                        if(dayOfMonth < 10){
                            dayString = "0" + dayOfMonth;
                        } else {
                            dayString = "" + dayOfMonth;
                        }

                        String editTextDateParam = year + "." + monthString + "." + dayString;
                        edTxtDate.setText(editTextDateParam);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
