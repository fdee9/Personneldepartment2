package com.example.personneldepartment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class employees extends AppCompatActivity {
    private ListView employees;
    private Button addemp;
    private Button timescand;
    private Button timesemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        employees = findViewById(R.id.emoloyees);
        addemp = findViewById(R.id.addemp);
        timescand = findViewById(R.id.timescand);
        timesemp = findViewById(R.id.timesemp);
        String[] names = {"Устинов Егор Викторович", "Шевченко Андрей Александрович"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, names);
        employees.setAdapter(arrayAdapter);
        final View.OnClickListener btn_startbdListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(employees.this, addemp.class));
            }
        };
        final View.OnClickListener btn_starteditcand = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(employees.this, edit_cand.class));
            }
        };
        final View.OnClickListener btn_starteditemp = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(employees.this, edit_emp.class));
            }
        };
        addemp.setOnClickListener(btn_startbdListener);
        timesemp.setOnClickListener(btn_starteditemp);
        timescand.setOnClickListener(btn_starteditcand);
    }
}