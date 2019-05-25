package com.example.personneldepartment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class candidates extends AppCompatActivity {
private ListView candidates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);
        candidates = findViewById(R.id.emoloyees);
        String[] names = {"Кипелова Анна Викторовна", "Александров Александр Олегович"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,names);
        candidates.setAdapter(arrayAdapter);
    }
}
