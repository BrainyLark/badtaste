package com.archer.badtaste;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText unameField;
    private EditText passwdField;
    private Button loginBtn;
    private TextView signBtn;

    private String username;
    private String password;
    private String email;
    private ProgressBar progressBar;

    public void initViews() {
        unameField = (EditText) findViewById(R.id.unameField);
        passwdField = (EditText) findViewById(R.id.passwdField);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signBtn = (TextView) findViewById(R.id.signBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                email = data.getStringExtra("email");
                // new Sign User task is to be defined
            } else {
                Toast.makeText(this, "Sorry it was failure", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void signUser(View v) {
        username = unameField.getText().toString();
        password = passwdField.getText().toString();

        Intent intent = new Intent(this, SignActivity.class);
        startActivityForResult(intent, 1);
    }

    public void loginUser(View v) {
        if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
            // new Login User task is to be defined
        }
    }

    class SignupTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            // request to server and get response
            return true;
        }

        @Override
        protected void onPostExecute(Boolean successState) {
            super.onPostExecute(successState);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    class LoginTask extends AsyncTask<String[], Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String[]... strings) {
            // request to server and get response
            return true;
        }

        @Override
        protected void onPostExecute(Boolean successState) {
            super.onPostExecute(successState);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
