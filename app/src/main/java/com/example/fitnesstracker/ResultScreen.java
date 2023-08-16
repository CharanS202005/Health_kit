package com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ResultScreen extends AppCompatActivity {

    String height_str, weight_str, age_str, activity_str;
    double activity;

    TextView bmitv,totalcaloriestv,bmrtv,fatpercenttv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);

        bmitv = findViewById(R.id.bmitv);
        totalcaloriestv = findViewById(R.id.totalcaloriestv);
        bmrtv = findViewById(R.id.bmrtv);
        fatpercenttv = findViewById(R.id.fatpercenttv);

        height_str = getIntent().getStringExtra("height");
        weight_str = getIntent().getStringExtra("weight");
        age_str = getIntent().getStringExtra("age");
        activity_str = getIntent().getStringExtra("activity");

        if(activity_str.equals("Little or No Activity")){
            activity = 1.2;
        }
        else if(activity_str.equals("Lightly Active")){
            activity = 1.375;
        }
        else if(activity_str.equals("Moderately Active")){
            activity = 1.55;
        }
        else if(activity_str.equals("Very Active")){
            activity = 1.9;
        }
        else{
            Toast.makeText(ResultScreen.this,"Please enter the activity level",Toast.LENGTH_SHORT).show();
        }

        double height = Double.parseDouble(height_str);
        double weight = Double.parseDouble(weight_str);
        int age = Integer.parseInt(age_str);

        Calculator calculator = new Calculator();

        double bmi_result = calculator.bmi_calculator(weight,height);
        double fat_result = calculator.body_fat(bmi_result,age);

        fatpercenttv.setText(String.valueOf(fat_result));

        if((fat_result>=10) && (fat_result<=14)){
            fat_result = 1.0;
        }
        else if((fat_result>=15) && (fat_result<=20)){
            fat_result = 0.95;
        }

        else if((fat_result>=21) && (fat_result<=28)){
            fat_result = 0.90;
        }

        else if(fat_result>=28){
            fat_result = 0.85;
        }
        else{
        }

        double bmr_result = calculator.bmr(weight,fat_result);
        double amr_result = calculator.amr(bmr_result,activity);

        bmitv.setText(String.valueOf(bmi_result));
        bmrtv.setText(String.valueOf(bmr_result));
        totalcaloriestv.setText(String.valueOf(amr_result));

    }
}