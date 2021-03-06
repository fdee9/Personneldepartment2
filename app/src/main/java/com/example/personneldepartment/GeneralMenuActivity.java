package com.example.personneldepartment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.personneldepartment.peopleActivities.PeopleActivity;

public class GeneralMenuActivity extends AppCompatActivity {
    private Button btn_employees;
    private Button btn_candidates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_menu);

        btn_employees = findViewById(R.id.btn_inPersonal);
        btn_candidates = findViewById(R.id.btn_inCandidats);

        final View.OnClickListener btnInPersonalListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GeneralMenuActivity.this, PeopleActivity.class);
                intent.putExtra("url", getIntent().getExtras().getString("url"));
                intent.putExtra("is_employee", true);
                startActivity(intent);
            }
        };
        final View.OnClickListener btnInCandidatesListener = new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(GeneralMenuActivity.this, PeopleActivity.class);
                intent.putExtra("url", getIntent().getExtras().getString("url"));
                intent.putExtra("is_employee", false);
                startActivity(intent);
            }

        };
        btn_employees.setOnClickListener(btnInPersonalListener);
        btn_candidates.setOnClickListener(btnInCandidatesListener);
    }

}