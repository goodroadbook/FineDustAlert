package com.goodroadbook.finedustsmartalert.init;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.goodroadbook.finedustsmartalert.R;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        Button startbtn = (Button)findViewById(R.id.startbtn);
        startbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.startbtn:
                finish();
                break;
        }
    }
}
