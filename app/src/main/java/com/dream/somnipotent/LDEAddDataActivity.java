package com.dream.somnipotent;

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

import com.dream.somnipotent.LDESqliteDatabase;
import com.dream.somnipotent.R;

public class LDEAddDataActivity extends AppCompatActivity {
    EditText subjectEt,descriptionEt;
    Button cancelBt,saveBt,shareBt;
    LDESqliteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lde_activity_add_data);

        mydb = new LDESqliteDatabase(this);

        subjectEt = findViewById(R.id.ldesubjectEditTextId);
        descriptionEt = findViewById(R.id.ldedescriptionEditTextId);

        cancelBt = findViewById(R.id.ldecacelButtonId);
        saveBt = findViewById(R.id.ldesaveButtonId);
        shareBt = findViewById(R.id.ldeshareButtonId);

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

    }

    //for inserting new data
    public void insertData(){
        long l = -1;

        if(subjectEt.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"You didn't add any subject",Toast.LENGTH_SHORT).show();
        }
        else{
            l = mydb.insertData(subjectEt.getText().toString(),
                    descriptionEt.getText().toString());
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
        Intent intent = new Intent(LDEAddDataActivity.this,LucidDreamActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}