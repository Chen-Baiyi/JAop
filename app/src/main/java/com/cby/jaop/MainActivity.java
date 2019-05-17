package com.cby.jaop;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cby.aspectj.annotation.JIntercept;
import com.cby.aspectj.annotation.JPermission;
import com.cby.aspectj.annotation.JSingleClick;


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

    @JSingleClick(3000)
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

    @JIntercept(JApplication.InterceptorType.TYPE_1)
    private void toast() {
        Toast.makeText(this, "(～￣▽￣)～", Toast.LENGTH_SHORT).show();
    }

    @JPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE})
//    @JIntercept(JApplication.InterceptorType.TYPE_0)
    private void login(String a) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    @JPermission({Manifest.permission.ACCESS_FINE_LOCATION})
    private void login2() {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
