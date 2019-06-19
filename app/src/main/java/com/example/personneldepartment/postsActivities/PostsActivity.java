package com.example.personneldepartment.postsActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.personneldepartment.models.SearchPostsByName;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {
    private ListView lV_posts;
    private EditText edTxt_findPosts;
    private Button btn_addPost;
    private Button btn_update;
    private Button btn_findPosts;

    private String url;
    private Post[] posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        lV_posts = findViewById(R.id.lV_posts);
        edTxt_findPosts = findViewById(R.id.edTxt_findPosts);
        btn_addPost = findViewById(R.id.btn_addPost);
        btn_update = findViewById(R.id.btn_updatePosts);
        btn_findPosts = findViewById(R.id.btn_findPosts);

        url = getIntent().getExtras().getString("url");

        getPosts();

        final View.OnClickListener btnAddPostListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostsActivity.this, AddPotsActivity.class);
                intent.putExtra("url", getIntent().getExtras().getString("url"));
                startActivity(intent);
            }
        };
        btn_addPost.setOnClickListener(btnAddPostListener);

        final View.OnClickListener btnUpdateListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPosts();
            }
        };
        btn_update.setOnClickListener(btnUpdateListener);

        final View.OnClickListener btnFindByNameListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPostsByName();
            }
        };
        btn_findPosts.setOnClickListener(btnFindByNameListener);

        final ListView.OnItemClickListener lVOnItemClickListener = new ListView.OnItemClickListener() {
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

    private void findPostsByName(){
        SearchPostsByName parameters = new SearchPostsByName(edTxt_findPosts.getText().toString());
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONArray data = null;
        try {
            data = new JSONArray().put(new JSONObject(new Gson().toJson(parameters)));
        } catch (JSONException e) {
            Toast.makeText(PostsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        JsonArrayRequest jsonObj = new JsonArrayRequest(Request.Method.POST, url + "Posts/SearchByName", data,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Post[] posts = new Gson().fromJson(response.toString(), Post[].class);

                        ArrayList<String> names = new ArrayList<>();
                        for (Post post : posts) {
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
        requestQueue.add(jsonObj);
    }

    private void getPosts() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "Posts", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        posts = new Gson().fromJson(response.toString(), Post[].class);

                        ArrayList<String> names = new ArrayList<>();
                        for (Post post : posts) {
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
    }
}
