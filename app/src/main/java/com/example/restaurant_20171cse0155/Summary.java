package com.example.restaurant_20171cse0155;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Summary extends AppCompatActivity {

    TextView summaryText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        summaryText = (TextView)findViewById(R.id.summary);
        summaryText.setText(getIntent().getStringExtra("summary"));
    }
}
