package com.example.personneldepartment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class general_menu extends AppCompatActivity {
    private Button employees;
    private Button candidates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_menu);
        employees = findViewById(R.id.inPersonal);
        final View.OnClickListener btninPersonalListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(general_menu.this, employees.class));
            }
        };
        final View.OnClickListener btnincandidatesListener = new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(general_menu.this, candidates.class));
            }

        };
        candidates = findViewById(R.id.inCandidats);
        employees.setOnClickListener(btninPersonalListener);
        candidates.setOnClickListener(btnincandidatesListener);
    }

}