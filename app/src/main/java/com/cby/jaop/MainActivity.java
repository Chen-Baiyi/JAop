package com.cby.jaop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cby.aspectj.annotation.Intercept;
import com.cby.aspectj.annotation.SingleClick;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        textView = findViewById(R.id.text);
    }


    int i = 0;
    @SingleClick(3000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                i++;
                break;
            case R.id.btn2:
                toast();
                break;
            case R.id.btn3:
                login("");
                break;
            case R.id.btn4:
                login2();
                break;
        }
        textView.setText(i + "");
    }


    @Intercept(JApplication.InterceptorType.TYPE_1)
    private void toast() {
        Toast.makeText(this, "(～￣▽￣)～", Toast.LENGTH_SHORT).show();
    }

    @Intercept(JApplication.InterceptorType.TYPE_0)
    private void login(String a) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    private void login2() {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
