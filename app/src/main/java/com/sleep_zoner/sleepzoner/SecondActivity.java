package com.sleep_zoner.sleepzoner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 7/22/17.
 */

public class SecondActivity extends AppCompatActivity {

    private TextView txvName, txvProfession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        txvName = (TextView) findViewById(R.id.txvName);
        txvProfession = (TextView) findViewById(R.id.txvProfession);
    }

    public void loadAccountData(View view) {
    }

    public void clearAccountData(View view) {
    }

    public void removeProfessionKey(View view) {
    }
}
