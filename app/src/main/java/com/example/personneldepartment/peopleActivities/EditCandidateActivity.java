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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
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

public class EditCandidateActivity extends AppCompatActivity {
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
    private Button btn_addCandidate;
    private Button btn_deleteCandidate;
    private Button btn_toEmployee;

    private String url;
    private Person person;
    private Post[] posts;
    private Boolean is_employee = false;
    private int spr_selectedItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cand);

        edTxt_number = findViewById(R.id.edTxt_number_EditCandidate);
        edTxt_surname = findViewById(R.id.edTxt_surname_EditCandidate);
        edTxt_name = findViewById(R.id.edTxt_name_EditCandidate);
        edTxt_patronymic = findViewById(R.id.edTxt_patronymic_EditCandidate);
        edTxt_date = findViewById(R.id.edTxt_date_EditCandidate);
        edTxt_phoneNumber = findViewById(R.id.edTxt_phoneNumber_EditCandidate);
        edTxt_email = findViewById(R.id.edTxt_email_EditCandidate);
        edTxt_address = findViewById(R.id.edTxt_address_EditCandidate);
        edTxt_education = findViewById(R.id.edTxt_education_EditCandidate);
        edTxt_experience = findViewById(R.id.edTxt_experience_EditCandidate);
        spr_posts = findViewById(R.id.spr_posts_EditCandidate);
        btn_addCandidate = findViewById(R.id.btn_saveCandidate);
        btn_deleteCandidate = findViewById(R.id.btn_deleteCandidate);
        btn_toEmployee = findViewById(R.id.btn_CandidateToEmployee);

        url = getIntent().getExtras().getString("url");
        person = new Gson().fromJson(getIntent().getExtras().getString("person"), Person.class);

        edTxt_number.setText(person.getNumber_employment_record());
        edTxt_surname.setText(person.getSurname());
        edTxt_name.setText(person.getName());
        edTxt_patronymic.setText(person.getPatronymic());
        edTxt_date.setText(person.getFormattedDate());
        edTxt_phoneNumber.setText(person.getPhone_number());
        edTxt_email.setText(person.getEmail());
        edTxt_address.setText(person.getAddress());
        edTxt_education.setText(person.getEducation());
        edTxt_experience.setText(Integer.toString(person.getExperience()));
        getPosts();

        final View.OnClickListener edTxtDateOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePicker();
            }
        };
        edTxt_date.setOnClickListener(edTxtDateOnClickListener);

        spr_posts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spr_selectedItemId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        final View.OnClickListener btnSavePostListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEmployee(person.getId());
            }
        };
        btn_addCandidate.setOnClickListener(btnSavePostListener);

        final View.OnClickListener btnDeletePostListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEmployee(person.getId());
            }
        };
        btn_deleteCandidate.setOnClickListener(btnDeletePostListener);

        final View.OnClickListener btnToEmployeeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                candidateToEmployee(person.getId());
            }
        };
        btn_toEmployee.setOnClickListener(btnToEmployeeListener);
    }

    private void candidateToEmployee(int id) {
        short mode = (short) 1;
        Integer idPost;
        if (spr_selectedItemId == 0) {
            idPost = null;
        } else {
            idPost = new Integer(posts[spr_selectedItemId - 1].getId());
        }
        Person newPerson;
        try {
            newPerson = new Person(
                    id,
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
            Toast.makeText(EditCandidateActivity.this, "Неверный формат данных", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject data = null;
        try {
            data = new JSONObject(new Gson().toJson(newPerson));
        } catch (JSONException e) {
            Toast.makeText(EditCandidateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.PUT, url + "People/" + id, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = new Gson().fromJson(response.toString(), Message.class);
                        if (message.getSuccess() == 1) {
                            Toast.makeText(EditCandidateActivity.this, "Сохранено", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditCandidateActivity.this, message.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass() == TimeoutError.class){
                            Toast.makeText(EditCandidateActivity.this,
                                    "Нет соединения с сервером",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditCandidateActivity.this,
                                    error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
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
                        if (is_employee) {
                            names.add("Должность не выбрана");
                        } else {
                            names.add("Вакансия не выбрана");
                        }
                        for (Post post : posts) {
                            names.add(post.getName());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditCandidateActivity.this, R.layout.support_simple_spinner_dropdown_item, names);
                        spr_posts.setAdapter(arrayAdapter);

                        try {
                            spr_posts.setSelection(findPostById(person.getId_position()) + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass() == TimeoutError.class){
                            Toast.makeText(EditCandidateActivity.this,
                                    "Нет соединения с сервером",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditCandidateActivity.this,
                                    error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private int findPostById(int id) throws Exception {
        for (int index = 0; index < posts.length; index++) {
            if (posts[index].getId() == id) {
                return index;
            }
        }
        throw new Exception("Текущая должность не найдена");
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
                        if (monthOfYear < 9) {
                            monthString = "0" + ++monthOfYear;
                        } else {
                            monthString = "" + ++monthOfYear;
                        }
                        String dayString;
                        if (dayOfMonth < 10) {
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

    private void deleteEmployee(int id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.DELETE, url + "People/" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = new Gson().fromJson(response.toString(), Message.class);
                        if (message.getSuccess() == 1) {
                            Toast.makeText(EditCandidateActivity.this, "Удалено", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditCandidateActivity.this, message.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            if((error.networkResponse.statusCode == 400)) {
                                Toast.makeText(EditCandidateActivity.this,
                                        "Есть ссылки на данного человека",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditCandidateActivity.this,
                                        "ERROR " + error.networkResponse.statusCode,
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (error.getClass() == TimeoutError.class){
                                Toast.makeText(EditCandidateActivity.this,
                                        "Нет соединения с сервером",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditCandidateActivity.this,
                                        error.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonObj);
    }

    private void editEmployee(int id) {
        short mode = (short) (is_employee ? 1 : 0);
        Integer idPost;
        if (spr_selectedItemId == 0) {
            idPost = null;
        } else {
            idPost = new Integer(posts[spr_selectedItemId - 1].getId());
        }
        Person newPerson;
        try {
            newPerson = new Person(
                    id,
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
            Toast.makeText(EditCandidateActivity.this, "Неверный формат данных", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject data = null;
        try {
            data = new JSONObject(new Gson().toJson(newPerson));
        } catch (JSONException e) {
            Toast.makeText(EditCandidateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.PUT, url + "People/" + id, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = new Gson().fromJson(response.toString(), Message.class);
                        if (message.getSuccess() == 1) {
                            Toast.makeText(EditCandidateActivity.this, "Сохранено", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditCandidateActivity.this, message.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass() == TimeoutError.class){
                            Toast.makeText(EditCandidateActivity.this,
                                    "Нет соединения с сервером",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditCandidateActivity.this,
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