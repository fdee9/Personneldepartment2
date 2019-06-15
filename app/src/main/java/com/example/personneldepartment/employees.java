package com.example.personneldepartment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.personneldepartment.postsActivities.PostsActivity;

public class employees extends AppCompatActivity {
    private ListView lV_employees;
    private Button btn_addEmployee;
    private Button btn_posts;
    private Button timesemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        lV_employees = findViewById(R.id.lV_employees);
        btn_addEmployee = findViewById(R.id.btn_addEmployee);
        btn_posts = findViewById(R.id.btn_posts);

        ///////////////////////////////////
        timesemp = findViewById(R.id.timesemp);
        //TODO fix this ***
        String[] names = {"Устинов Егор Викторович", "Шевченко Андрей Александрович"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, names);
        lV_employees.setAdapter(arrayAdapter);
        //////////////////////////////////////
        final View.OnClickListener btn_startbdListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(employees.this, addemp.class));
            }
        };
        final View.OnClickListener btnPostsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employees.this, PostsActivity.class);
                intent.putExtra("url", getIntent().getExtras().getString("url"));
                startActivity(intent);
            }
        };
        final View.OnClickListener btn_starteditemp = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(employees.this, edit_emp.class));
            }
        };
        btn_addEmployee.setOnClickListener(btn_startbdListener);
        timesemp.setOnClickListener(btn_starteditemp);
        btn_posts.setOnClickListener(btnPostsListener);
    }
}