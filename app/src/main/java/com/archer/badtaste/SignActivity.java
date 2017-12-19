package com.archer.badtaste;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignActivity extends AppCompatActivity {

    private EditText emailField;
    private Button emailBtn;

    public void initViews() {
        emailField = (EditText) findViewById(R.id.emailField);
        emailBtn = (Button) findViewById(R.id.emailBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initViews();
    }
    public void sendEmailBack(View v) {
        Intent intent = new Intent();
        intent.putExtra("email", emailField.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
