package com.example.personneldepartment.postsActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.personneldepartment.R;
import com.example.personneldepartment.models.Post;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {
    private ListView lV_posts;
    private Button btn_addPost;
    private String url;
    private Post[] posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        lV_posts = findViewById(R.id.lV_posts);
        btn_addPost = findViewById(R.id.btn_addPost);

        url = getIntent().getExtras().getString("url");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "Posts", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        posts = new Gson().fromJson(response.toString(), Post[].class);

                        ArrayList<String> names = new ArrayList<>();
                        for(Post post : posts){
                            names.add(post.getName());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PostsActivity.this, R.layout.support_simple_spinner_dropdown_item, names);
                        lV_posts.setAdapter(arrayAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PostsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);

        final View.OnClickListener btnAddPostListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostsActivity.this, AddPotsActivity.class);
                intent.putExtra("url", getIntent().getExtras().getString("url"));
                startActivity(intent);
            }
        };
        btn_addPost.setOnClickListener(btnAddPostListener);

        final ListView.OnItemClickListener lVOnItemClickListener = new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(PostsActivity.this, EditPostActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("post", new Gson().toJson(posts[position]));
                startActivity(intent);
            }
        };
        lV_posts.setOnItemClickListener(lVOnItemClickListener);
    }
}
