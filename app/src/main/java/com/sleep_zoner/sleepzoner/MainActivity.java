package com.sleep_zoner.sleepzoner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etProfession;
    private TextView txvName, txvProfession;
    private Switch pageColorSwitch;
    private LinearLayout pageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etProfession = (EditText) findViewById(R.id.etProfession);
        txvName = (TextView) findViewById(R.id.txvName);
        txvProfession = (TextView) findViewById(R.id.txvProfession);
        pageColorSwitch = (Switch) findViewById(R.id.pageColorSwitch);
        pageLayout = (LinearLayout) findViewById(R.id.pageLayout);

    }

    public void saveAccountData(View view) {
    }

    public void loadAccountData(View view) {
    }

    public void openSecondActivity(View view) {
    }
}
