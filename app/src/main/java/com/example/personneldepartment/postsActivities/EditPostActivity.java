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

public class EditPostActivity extends AppCompatActivity {
    private EditText edTxt_name;
    private EditText edTxt_department;
    private EditText edTxt_salary;
    private EditText edTxt_education;
    private EditText edTxt_experience;
    private Button btn_save;
    private Button btn_delete;

    private String url;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        edTxt_name = findViewById(R.id.edTxt_namePost_EditPost);
        edTxt_department = findViewById(R.id.edTxt_department_EditPost);
        edTxt_salary = findViewById(R.id.edTxt_salary_EditPost);
        edTxt_education = findViewById(R.id.edTxt_education_EditPost);
        edTxt_experience = findViewById(R.id.edTxt_experience_EditPost);
        btn_save = findViewById(R.id.btn_savePost_EditPost);
        btn_delete = findViewById(R.id.btn_deletePost_EditPost);

        url = getIntent().getExtras().getString("url");
        post = new Gson().fromJson(getIntent().getExtras().getString("post"), Post.class);
        edTxt_name.setText(post.getName());
        edTxt_department.setText(post.getDepartment());
        edTxt_salary.setText(Double.toString(post.getSalary()));
        edTxt_education.setText(post.getMin_education());
        edTxt_experience.setText(post.getMin_experience());

        final View.OnClickListener btnSavePostListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePost(post.getId());
            }
        };
        btn_save.setOnClickListener(btnSavePostListener);

        final View.OnClickListener btnDeletePostListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost(post.getId());
            }
        };
        btn_delete.setOnClickListener(btnDeletePostListener);
    }

    private void savePost(int id){
        Post newPost = new Post(
                id,
                edTxt_name.getText().toString(),
                edTxt_department.getText().toString(),
                edTxt_salary.getText().toString(),
                edTxt_education.getText().toString(),
                edTxt_experience.getText().toString());

        JSONObject data = null;
        try {
            data = new JSONObject(new Gson().toJson(newPost));
        } catch (JSONException e) {
            Toast.makeText(EditPostActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.PUT, url + "Posts/" + id, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = new Gson().fromJson(response.toString(), Message.class);
                        if (message.getSuccess() == 1) {
                            Toast.makeText(EditPostActivity.this, "Сохранено", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditPostActivity.this, message.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditPostActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonObj);
    }

    private void deletePost(int id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.DELETE, url + "Posts/" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = new Gson().fromJson(response.toString(), Message.class);
                        if (message.getSuccess() == 1) {
                            Toast.makeText(EditPostActivity.this, "Удалено", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditPostActivity.this, message.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditPostActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonObj);
    }
}
