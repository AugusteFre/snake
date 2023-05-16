package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class title_screen extends AppCompatActivity {

    private Button BT_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
        BT_start = findViewById(R.id.start_button);
    }


    @Override
    protected void onStart() {
        super.onStart();

        BT_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameActivity = new Intent(title_screen.this,MainActivity.class);
                startActivity(gameActivity);
            }
        });
    }
}