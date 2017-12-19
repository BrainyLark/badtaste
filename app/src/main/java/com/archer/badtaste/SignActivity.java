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

    public void initViews() {
        emailField = (EditText) findViewById(R.id.emailField);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initViews();
    }
    public void sendEmailBack(View v) {
        Intent intent = getIntent();
        intent.putExtra("email", emailField.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
