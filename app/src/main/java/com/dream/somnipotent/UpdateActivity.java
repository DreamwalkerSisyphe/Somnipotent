package com.dream.somnipotent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.somnipotent.SqliteDatabase;
import com.dream.somnipotent.Information;
import com.dream.somnipotent.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {
    EditText subjectEt,descriptionEt;
    Button cancelBt,updateBt,shareBtOnUpdate;
    SqliteDatabase dbUpdate;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);

        //passing Update Activity's context to database alass
        dbUpdate = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = dbUpdate.getWritableDatabase();

        subjectEt = findViewById(R.id.subjectEditTextIdUpdate);
        descriptionEt = findViewById(R.id.descriptionEditTextIdUpdate);

        cancelBt = findViewById(R.id.cacelButtonIdUpdate);
        updateBt = findViewById(R.id.saveButtonIdUpdate);
        shareBtOnUpdate = findViewById(R.id.shareButtonIdUpdate);
        mDisplayDate = (TextView) findViewById(R.id.tvDateUpdate);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        String sub = intent.getStringExtra("subject");
        String des = intent.getStringExtra("description");
        String date = intent.getStringExtra("date");
        final String id = intent.getStringExtra("listId");


        subjectEt.setText(sub);
        descriptionEt.setText(des);
        mDisplayDate.setText(date);

        //for sharing data to social media
        shareBtOnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String sub = subjectEt.getText().toString();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                String des = descriptionEt.getText().toString();
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,des);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                        UpdateActivity.this,
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

        //for updating database data
        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dbUpdate.update(subjectEt.getText().toString(),descriptionEt.getText().toString(),mDisplayDate.getText().toString(),id)==true){
                    Toast.makeText(getApplicationContext(),"Data updated",Toast.LENGTH_SHORT).show();
                    backToMain();
                }
            }
        });

    }

    //this method to clearing top activity and starting new activity
    public void backToMain()
    {
        Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}