package com.example.izycode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.izycode.createPicturePackage.CreateIzyCodeActivity;

public class MainActivity extends AppCompatActivity {

    Button createIzyCodeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createIzyCodeButton = findViewById(R.id.mainButton_createIzyCode);

        createIzyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCreateIzyCodeActivity = new Intent(MainActivity.this, CreateIzyCodeActivity.class);

                startActivity(startCreateIzyCodeActivity);
            }
        });
    }



}
