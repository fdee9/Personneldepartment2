package com.example.personneldepartment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    private Button Startbd;
    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Startbd = findViewById(R.id.Startbd);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        final View.OnClickListener btn_startbdListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // String selectTableSQL = "SELECT password from account WHERE login AS " + login.getText();
               // try {

                 //   java.sql.Connection dbConnection = getDBConnection();
                 //   Statement statement = dbConnection.createStatement();

                    // выбираем данные с БД
                  //  ResultSet rs = statement.executeQuery(selectTableSQL);

                    // И если что то было получено то цикл while сработает
               // while (rs.next()) {
                //        if (rs.getString("password").equals(password.getText())) {
                           startActivity(new Intent(MainActivity.this, general_menu.class));
                  /*      } else {
                   //         Toast.makeText(MainActivity.this, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                            return;

                        }
                    }
             //   } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } */

            }
        };
        Startbd.setOnClickListener(btn_startbdListener);


    }

    private static java.sql.Connection getDBConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        java.sql.Connection dbConnection = null;

        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

        dbConnection = DriverManager.getConnection("jdbc:mysql://192.168.42.34:3306/personnel_departmen_androidapp", "AndroidApp", "mcSvrAd9JaNp");
        return dbConnection;

    }
}
