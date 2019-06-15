package com.example.personneldepartment.postsActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.personneldepartment.R;
import com.example.personneldepartment.models.Message;
import com.example.personneldepartment.models.Post;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AddPotsActivity extends AppCompatActivity {
    private EditText edTxt_name;
    private EditText edTxt_department;
    private EditText edTxt_salary;
    private EditText edTxt_education;
    private EditText edTxt_experience;
    private Button btn_save;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pots);

        edTxt_name = findViewById(R.id.edTxt_namePost);
        edTxt_department = findViewById(R.id.edTxt_department);
        edTxt_salary = findViewById(R.id.edTxt_salary);
        edTxt_education = findViewById(R.id.edTxt_education);
        edTxt_experience = findViewById(R.id.edTxt_experience);
        btn_save = findViewById(R.id.btn_savePost);

        url = getIntent().getExtras().getString("url") + "Posts";

        final View.OnClickListener btnSaveListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePost(new Post(  edTxt_name.getText().toString(),
                                    edTxt_department.getText().toString(),
                                    edTxt_salary.getText().toString(),
                                    edTxt_education.getText().toString(),
                                    edTxt_experience.getText().toString()));
            }
        };

        btn_save.setOnClickListener(btnSaveListener);
    }

    private void savePost(Post post){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject data = null;
        try {
            data = new JSONObject(new Gson().toJson(post));
        } catch (JSONException e) {
            Toast.makeText(AddPotsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = new Gson().fromJson(response.toString(), Message.class);
                        if (message.getSuccess() == 1) {
                            Toast.makeText(AddPotsActivity.this, "Сохранено", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddPotsActivity.this, message.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddPotsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonObj);
    }
}
