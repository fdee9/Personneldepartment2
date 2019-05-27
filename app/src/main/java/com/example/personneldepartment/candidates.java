package com.example.personneldepartment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class candidates extends AppCompatActivity {
    private ListView candidates;
    private Button addcand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);
        candidates = findViewById(R.id.emoloyees);
        addcand = findViewById(R.id.addcand);
        String[] names = {"Кипелова Анна Викторовна", "Александров Александр Олегович"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, names);
        candidates.setAdapter(arrayAdapter);
        final View.OnClickListener btn_startbdListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(candidates.this, addcand.class));
            }
        };
        addcand.setOnClickListener(btn_startbdListener);
    }
}