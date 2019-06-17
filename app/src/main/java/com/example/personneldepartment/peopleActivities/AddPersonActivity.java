package com.example.personneldepartment.peopleActivities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.personneldepartment.R;
import com.example.personneldepartment.models.Message;
import com.example.personneldepartment.models.Person;
import com.example.personneldepartment.models.Post;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AddPersonActivity extends AppCompatActivity {
    private TextView txtV_title;
    private EditText edTxt_number;
    private EditText edTxt_surname;
    private EditText edTxt_name;
    private EditText edTxt_patronymic;
    private EditText edTxt_date;
    private EditText edTxt_phoneNumber;
    private EditText edTxt_email;
    private EditText edTxt_address;
    private EditText edTxt_education;
    private EditText edTxt_experience;
    private Spinner spr_posts;
    private Button btn_addPerson;

    private Post[] posts;
    private String url;
    private Boolean is_employee;
    private int spr_selectedItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addemp);

        txtV_title = findViewById(R.id.txtV_title_AddPerson);
        edTxt_number = findViewById(R.id.edTxt_number_AddPerson);
        edTxt_surname = findViewById(R.id.edTxt_surname_AddPerson);
        edTxt_name = findViewById(R.id.edTxt_name_AddPerson);
        edTxt_patronymic = findViewById(R.id.edTxt_patronymic_AddPerson);
        edTxt_date = findViewById(R.id.edTxt_date_AddPerson);
        edTxt_phoneNumber = findViewById(R.id.edTxt_phoneNumber_AddPerson);
        edTxt_email = findViewById(R.id.edTxt_email_AddPerson);
        edTxt_address = findViewById(R.id.edTxt_address_AddPerson);
        edTxt_education = findViewById(R.id.edTxt_education_AddPerson);
        edTxt_experience = findViewById(R.id.edTxt_experience_AddPerson);
        spr_posts = findViewById(R.id.spr_posts);
        btn_addPerson = findViewById(R.id.btn_addPerson);

        url = getIntent().getExtras().getString("url");
        is_employee = (Boolean) getIntent().getExtras().get("is_employee");

        if (is_employee) {
            txtV_title.setText("Добавить сотрудника");
        } else {
            txtV_title.setText("Добавить кандидата");
        }

        final View.OnClickListener edTxtDateOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePicker();
            }
        };
        edTxt_date.setOnClickListener(edTxtDateOnClickListener);

        getPosts();

        spr_posts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spr_selectedItemId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        final View.OnClickListener btnSaveListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                short mode = (short) (is_employee ? 1 : 0);
                Integer idPost;
                if(spr_selectedItemId == 0){
                    idPost = null;
                } else {
                    idPost = new Integer(posts[spr_selectedItemId - 1].getId());
                }
                Person person;
                try {
                    person = new Person(
                            edTxt_number.getText().toString(),
                            edTxt_surname.getText().toString(),
                            edTxt_name.getText().toString(),
                            edTxt_patronymic.getText().toString(),
                            edTxt_date.getText().toString(),
                            edTxt_phoneNumber.getText().toString(),
                            edTxt_email.getText().toString(),
                            edTxt_address.getText().toString(),
                            edTxt_education.getText().toString(),
                            edTxt_experience.getText().toString(),
                            mode,
                            idPost
                    );
                } catch (Exception exp) {
                    Toast.makeText(AddPersonActivity.this, "Неверный формат данных", Toast.LENGTH_LONG).show();
                    return;
                }
                savePerson(person);
            }
        };

        btn_addPerson.setOnClickListener(btnSaveListener);
    }

    private void callDatePicker() {
        // получаем текущую дату
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        // инициализируем диалог выбора даты текущими значениями
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String monthString;
                        if(monthOfYear < 9){
                            monthString = "0" + ++monthOfYear;
                        } else {
                            monthString = "" + ++monthOfYear;
                        }
                        String dayString;
                        if(dayOfMonth < 10){
                            dayString = "0" + dayOfMonth;
                        } else {
                            dayString = "" + dayOfMonth;
                        }

                        String editTextDateParam = year + "." + monthString + "." + dayString;
                        edTxt_date.setText(editTextDateParam);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getPosts() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "Posts", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        posts = new Gson().fromJson(response.toString(), Post[].class);

                        ArrayList<String> names = new ArrayList<>();
                        if (is_employee) {
                            names.add("Должность не выбрана");
                        } else {
                            names.add("Вакансия не выбрана");
                        }
                        for (Post post : posts) {
                            names.add(post.getName());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddPersonActivity.this, R.layout.support_simple_spinner_dropdown_item, names);
                        spr_posts.setAdapter(arrayAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddPersonActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void savePerson(Person person) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject data = null;
        try {
            data = new JSONObject(new Gson().toJson(person));
        } catch (JSONException e) {
            Toast.makeText(AddPersonActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, url + "People", data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = new Gson().fromJson(response.toString(), Message.class);
                        if (message.getSuccess() == 1) {
                            Toast.makeText(AddPersonActivity.this, "Сохранено", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddPersonActivity.this, message.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddPersonActivity.this, "Неверный формат данных", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonObj);
    }
}
