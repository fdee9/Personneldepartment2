package com.example.personneldepartment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class employees extends AppCompatActivity {
    private ListView employees;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        employees = findViewById(R.id.emoloyees);
        String[] names = {"Устинов Егор Викторович", "Шевченко Андрей Александрович"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,names);
        employees.setAdapter(arrayAdapter);
    }
}
