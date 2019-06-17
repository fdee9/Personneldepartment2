package com.example.personneldepartment.interviewActivities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.personneldepartment.R;
import com.example.personneldepartment.models.Interview;
import com.example.personneldepartment.models.Message;
import com.example.personneldepartment.models.Person;
import com.example.personneldepartment.models.Post;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class EditInterviewActivity extends AppCompatActivity {
    private EditText edTxt_date;
    private EditText edTxt_time;
    private Spinner spr_candidate;
    private Spinner spr_vacancy;
    private Button btn_save;
    private Button btn_delete;

    private String url;
    private Post[] posts;
    private Person[] people;
    private int spr_idSelectedCandidate;
    private int spr_idSelectedVacancy;
    private Interview interview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_interview);

        edTxt_date = findViewById(R.id.edTxt_date_Interview_EditInterview);
        edTxt_time = findViewById(R.id.edTxt_time_Interview_EditInterview);
        spr_candidate = findViewById(R.id.spr_candidate_EditInterview);
        spr_vacancy = findViewById(R.id.spr_vacancy_EditInterview);
        btn_save = findViewById(R.id.btn_saveInterview_EditInterview);
        btn_delete = findViewById(R.id.btn_deleteInterview);

        url = getIntent().getExtras().getString("url");
        getInterviewById((int)getIntent().getExtras().get("idInterview"));

        spr_candidate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spr_idSelectedCandidate = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spr_vacancy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spr_idSelectedVacancy = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        final View.OnClickListener btnSaveListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer idCandidate;
                if(spr_idSelectedCandidate == 0){
                    idCandidate = null;
                } else {
                    idCandidate = new Integer(people[spr_idSelectedCandidate - 1].getId());
                }
                Integer idVacancy;
                if(spr_idSelectedVacancy == 0){
                    idVacancy = null;
                } else {
                    idVacancy = new Integer(posts[spr_idSelectedVacancy - 1].getId());
                }
                Interview newInterview;
                try {
                    newInterview = new Interview(
                            interview.getId(),
                            edTxt_date.getText().toString() + " " + edTxt_time.getText().toString(),
                            idCandidate,
                            idVacancy
                    );
                } catch (Exception exp) {
                    Toast.makeText(EditInterviewActivity.this, "Неверный формат данных", Toast.LENGTH_LONG).show();
                    return;
                }
                updateInterview(newInterview);
            }
        };
        btn_save.setOnClickListener(btnSaveListener);

        final View.OnClickListener edTxtDateOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePicker();
            }
        };
        edTxt_date.setOnClickListener(edTxtDateOnClickListener);

        final View.OnClickListener edTxtTimeOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTimePicker();
            }
        };
        edTxt_time.setOnClickListener(edTxtTimeOnClickListener);

        final View.OnClickListener btnDeleteOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteInterview(interview.getId());
            }
        };
        btn_delete.setOnClickListener(btnDeleteOnClickListener);
    }

    private void deleteInterview(int id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.DELETE, url + "Interviews/" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = new Gson().fromJson(response.toString(), Message.class);
                        if (message.getSuccess() == 1) {
                            Toast.makeText(EditInterviewActivity.this, "Удалено", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditInterviewActivity.this, message.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditInterviewActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonObj);
    }

    private void updateInterview(Interview interview) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject data = null;
        try {
            data = new JSONObject(new Gson().toJson(interview));
        } catch (JSONException e) {
            Toast.makeText(EditInterviewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.PUT, url + "Interviews/" + interview.getId(), data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message message = new Gson().fromJson(response.toString(), Message.class);
                        if (message.getSuccess() == 1) {
                            Toast.makeText(EditInterviewActivity.this, "Сохранено", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditInterviewActivity.this, message.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditInterviewActivity.this, "Неверный формат данных", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonObj);
    }

    private void getCandidates(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "People/Candidates", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        people = new Gson().fromJson(response.toString(), Person[].class);

                        ArrayList<String> names = new ArrayList<>();
                        names.add("Кандидат не выбран");
                        for(Person person : people){
                            names.add(person.getSurname() + " " + person.getName() + " " + person.getPatronymic());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditInterviewActivity.this, R.layout.support_simple_spinner_dropdown_item, names);
                        spr_candidate.setAdapter(arrayAdapter);

                        try {
                            spr_candidate.setSelection(findCandidateById(interview.getId_candidate()) + 1);
                        } catch (Exception e) {
                            Toast.makeText(EditInterviewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditInterviewActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public int findCandidateById(int id) throws Exception {
        for (int index = 0; index < people.length; index++) {
            if (people[index].getId() == id) {
                return index;
            }
        }
        throw new Exception("Кандидат не найден");
    }

    private void getPosts() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "Posts", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        posts = new Gson().fromJson(response.toString(), Post[].class);

                        ArrayList<String> names = new ArrayList<>();
                        names.add("Вакансия не выбрана");
                        for (Post post : posts) {
                            names.add(post.getName());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditInterviewActivity.this, R.layout.support_simple_spinner_dropdown_item, names);
                        spr_vacancy.setAdapter(arrayAdapter);

                        try {
                            spr_vacancy.setSelection(findPostById(interview.getId_vacancy()) + 1);
                        } catch (Exception e) {
                            Toast.makeText(EditInterviewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditInterviewActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void callTimePicker() {
        // получаем текущее время
        final Calendar cal = Calendar.getInstance();
        final int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinute = cal.get(Calendar.MINUTE);

        // инициализируем диалог выбора времени текущими значениями
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hourString;
                        if(hourOfDay < 10){
                            hourString = "0" + hourOfDay;
                        } else {
                            hourString = "" + hourOfDay;
                        }
                        String minuteString;
                        if (minute < 10){
                            minuteString = "0" + minute;
                        } else {
                            minuteString = "" + minute;
                        }

                        String editTextTimeParam = hourString + ":" + minuteString;
                        edTxt_time.setText(editTextTimeParam);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void getInterviewById(int id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + "Interviews/" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        interview = new Gson().fromJson(response.toString(), Interview.class);
                        interview.fixDateTime();

                        edTxt_date.setText(interview.getDateString());
                        edTxt_time.setText(interview.getTimeString());

                        getCandidates();
                        getPosts();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditInterviewActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}