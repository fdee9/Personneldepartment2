package com.example.personneldepartment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.personneldepartment.models.Message;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {
    private Button btn_startBD;
    private EditText edTxt_login;
    private EditText edTxt_password;
    private EditText edTxt_IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_startBD = findViewById(R.id.Startbd);
        edTxt_login = findViewById(R.id.login);
        edTxt_password = findViewById(R.id.password);
        edTxt_IP = findViewById(R.id.edTxt_IP);

        final View.OnClickListener btn_startbdListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://" + edTxt_IP.getText() + "/api/Accounts";
                try {
                    postData(url, new JSONObject("{\"login\":\"" + edTxt_login.getText() + "\",\"password\":\"" + edTxt_password.getText() + "\"}"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        btn_startBD.setOnClickListener(btn_startbdListener);
    }

    public void postData(String url, JSONObject data) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = new Gson().fromJson(response.toString(), Message.class);
                        if (message.getSuccess() == 1) {
                            Intent intent = new Intent(StartActivity.this, GeneralMenuActivity.class);
                            intent.putExtra("url", "http://" + edTxt_IP.getText() + "/api/");
                            startActivity(intent);
                        } else {
                            Toast.makeText(StartActivity.this, message.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass() == TimeoutError.class){
                            Toast.makeText(StartActivity.this,
                                    "Нет соединения с сервером",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(StartActivity.this,
                                    error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonObj);
    }
}
