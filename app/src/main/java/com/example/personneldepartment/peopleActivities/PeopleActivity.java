package com.example.personneldepartment.peopleActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.personneldepartment.R;
import com.example.personneldepartment.interviewActivities.InterviewActivity;
import com.example.personneldepartment.models.Person;
import com.example.personneldepartment.postsActivities.PostsActivity;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class PeopleActivity extends AppCompatActivity {
    private TextView txtV_title;
    private ListView lV_employees;
    private Button btn_addPerson;
    private Button btn_posts;
    private Button btn_update;

    private String url;
    private Boolean is_employee;
    private Person[] people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        txtV_title = findViewById(R.id.txtV_title_PeopleActivity);
        lV_employees = findViewById(R.id.lV_people);
        btn_addPerson = findViewById(R.id.btn_addPerson);
        btn_posts = findViewById(R.id.btn_posts_interviews);
        btn_update = findViewById(R.id.btn_update);

        url = getIntent().getExtras().getString("url");
        is_employee = (Boolean) getIntent().getExtras().get("is_employee");

        final View.OnClickListener btnAddPersonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PeopleActivity.this, AddPersonActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("is_employee", is_employee);
                startActivity(intent);
            }
        };
        final View.OnClickListener btnPostsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PeopleActivity.this, PostsActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        };
        final View.OnClickListener btnInterviwesListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PeopleActivity.this, InterviewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        };
        final View.OnClickListener btnUpdateListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_employee) {
                    getPeople(url + "People/Employees");
                }else {
                    getPeople(url + "People/Candidates");
                }
            }
        };
        final ListView.OnItemClickListener lVOnItemClickListener = new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent;
                if (is_employee) {
                    intent = new Intent(PeopleActivity.this, EditEmployeeActivity.class);
                } else {
                    intent = new Intent(PeopleActivity.this, EditCandidateActivity.class);
                }
                intent.putExtra("url", url);
                intent.putExtra("person", new Gson().toJson(people[position]));
                startActivity(intent);
            }
        };

        if(is_employee) {
            txtV_title.setText("Сотрудники предприятия");
            btn_posts.setText("Должности");
            btn_posts.setOnClickListener(btnPostsListener);
            getPeople(url + "People/Employees");
        }else {
            txtV_title.setText("Кандидаты на открытые вакансии");
            btn_posts.setText("Собеседования");
            btn_posts.setOnClickListener(btnInterviwesListener);
            getPeople(url + "People/Candidates");
        }

        btn_addPerson.setOnClickListener(btnAddPersonListener);
        btn_update.setOnClickListener(btnUpdateListener);
        lV_employees.setOnItemClickListener(lVOnItemClickListener);
    }


    private void getPeople(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        people = new Gson().fromJson(response.toString(), Person[].class);

                        ArrayList<String> names = new ArrayList<>();
                        for(Person person : people){
                            names.add(person.getSurname() + " " + person.getName() + " " + person.getPatronymic());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PeopleActivity.this, R.layout.support_simple_spinner_dropdown_item, names);
                        lV_employees.setAdapter(arrayAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PeopleActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
}