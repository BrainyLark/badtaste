package com.archer.badtaste;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText unameField;
    private EditText passwdField;
    private Button loginBtn;
    private TextView signBtn;
    private ProgressDialog pd;

    private String username;
    private String password;
    private String email;

    public void initViews() {
        unameField = (EditText) findViewById(R.id.unameField);
        passwdField = (EditText) findViewById(R.id.passwdField);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signBtn = (TextView) findViewById(R.id.signBtn);
        pd = new ProgressDialog(LoginActivity.this, R.style.myTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(R.style.Widget_AppCompat_ProgressBar);
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
        username = unameField.getText().toString();
        password = passwdField.getText().toString();
        if(!username.isEmpty() && !password.isEmpty()) {
            // new Login User task is to be defined
            LoginTask loginTask = new LoginTask();
            loginTask.execute(new String[]{username, password});
        }
    }

    class SignupTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            // request to server and get response
            return true;
        }

        @Override
        protected void onPostExecute(Boolean successState) {
            super.onPostExecute(successState);
        }
    }

    class LoginTask extends AsyncTask<String[], Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected Integer doInBackground(String[]... strings) {
            // request to server and get response
            HttpURLConnection urlConn;
            try {
                JSONObject reqObject = new JSONObject();
                reqObject.put("uname", strings[0][0]);
                reqObject.put("passwd", strings[0][1]);
                String json = reqObject.toString();
                URL mUrl = new URL("http://192.168.1.7:8080/login");
                urlConn = (HttpURLConnection) mUrl.openConnection();
                urlConn.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                urlConn.setRequestMethod("POST");
                OutputStream os = urlConn.getOutputStream();
                os.write(json.getBytes());
                os.close();

                InputStream in = new BufferedInputStream(urlConn.getInputStream());

                String result = IOUtils.toString(in, "UTF-8");
                JSONObject jsonObject = new JSONObject(result);

                boolean success = jsonObject.getBoolean("success");
                if(success) {
                    return jsonObject.getInt("id");
                }

            } catch (IOException | JSONException e) {

            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer userid) {
            super.onPostExecute(userid);
            pd.dismiss();
            if(userid != -1) {
                Bundle bundle = new Bundle();
                bundle.putInt("userid", userid);
                bundle.putString("username", username);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Уучлаарай, нэр эсвэл нууц үг чинь буруу байна.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
