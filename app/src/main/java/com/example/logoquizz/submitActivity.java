package com.example.logoquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class submitActivity extends AppCompatActivity {

    private TextView txtTotal,txtCorrect,txtWrong;

    private Button resetBtn,exitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        txtTotal = findViewById(R.id.txtTotal);
        txtCorrect = findViewById(R.id.txtCorrect);
        txtWrong = findViewById(R.id.txtWrong);
        resetBtn = findViewById(R.id.btnReset);
        exitBtn = findViewById(R.id.btnExit);

        Intent intent = getIntent();

        int total = intent.getIntExtra("Total",-1);
        int correct = intent.getIntExtra("Correct",-1);
        int wrong = intent.getIntExtra("Wrong",-1);

        txtTotal.setText(txtTotal.getText().toString() + total);
        txtCorrect.setText(txtCorrect.getText().toString() + correct);
        txtWrong.setText(txtWrong.getText().toString() + wrong);


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainAcIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainAcIntent);
                finish();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





    }
}