package com.example.tanvir.diary.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanvir.diary.Database.SqliteDatabase;
import com.example.tanvir.diary.R;

import java.util.Calendar;
import java.util.Date;

public class AddDataActivity extends AppCompatActivity {
    EditText subjectEt,descriptionEt;
    Button cancelBt,saveBt,shareBt;
    SqliteDatabase mydb;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        mydb = new SqliteDatabase(this);

        subjectEt = findViewById(R.id.subjectEditTextId);
        descriptionEt = findViewById(R.id.descriptionEditTextId);

        cancelBt = findViewById(R.id.cacelButtonId);
        saveBt = findViewById(R.id.saveButtonId);
        shareBt = findViewById(R.id.shareButtonId);

        mDisplayDate = findViewById(R.id.tvDateId);

        shareBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //passing data via intent
                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String sub = subjectEt.getText().toString();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                String des = descriptionEt.getText().toString();
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,des);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                backToMain();
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddDataActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

    }

    //for inserting new data
    public void insertData(){
        long l = -1;

        if(subjectEt.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"You didn't add any subject",Toast.LENGTH_SHORT).show();
        }
        else{
            l = mydb.insertData(subjectEt.getText().toString(),
                    descriptionEt.getText().toString(),
                    mDisplayDate.getText().toString());
        }

        if(l>=0){
            Toast.makeText(getApplicationContext(),"Data added",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Data not added", Toast.LENGTH_SHORT).show();
        }
    }
    public void backToMain()
    {
        Intent intent = new Intent(AddDataActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
