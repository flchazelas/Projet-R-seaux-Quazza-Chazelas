package com.example.izycode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.izycode.analysePicturePackage.TakePictureActivity;

public class MainActivity extends AppCompatActivity {

    private Button takePictureButton = null;
    private Button createIzyCodeButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePictureButton = findViewById(R.id.mainButton_takePicture);
        createIzyCodeButton = findViewById(R.id.mainButton_createIzyCode);

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startTakePictureActivity = new Intent(MainActivity.this, TakePictureActivity.class);

                startActivity(startTakePictureActivity);
            }
        });

    }



}
