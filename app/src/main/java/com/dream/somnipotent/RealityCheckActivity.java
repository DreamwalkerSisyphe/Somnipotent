package com.dream.somnipotent;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.dream.somnipotent.ChannelThreeAlarm;

import com.dream.somnipotent.RCSqliteDatabase;
import com.dream.somnipotent.RCInformation;
import com.dream.somnipotent.RCInformationAdapter;
import com.dream.somnipotent.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class RealityCheckActivity extends AppCompatActivity {
    ListView listView;
    RCSqliteDatabase db;
    ArrayList<RCInformation> arrayList;
    ArrayList<String> selectList = new ArrayList<String>();
    ArrayList<Integer> unDeleteSelect = new ArrayList<Integer>();

    ArrayAdapter arrayAdapter;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realitycheck);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new RCSqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = db.getWritableDatabase();

        listView = findViewById(R.id.ListviewId);

        arrayList=new ArrayList<RCInformation>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // ClickListener for floating action bar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RealityCheckActivity.this,RCAddDataActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton debugButton = (FloatingActionButton) findViewById(R.id.debugButton);

        final SharedPreferences sharedPref = getSharedPreferences("com.dream.somnipotent.settings",MODE_PRIVATE);
        if(sharedPref.getBoolean("debugswitch", false))
            debugButton.show();
        else
            debugButton.hide();

        debugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChannelThreeAlarm cta = new ChannelThreeAlarm();
                Intent intent = new Intent(RealityCheckActivity.this, ChannelThreeAlarm.class);
                cta.onReceive(RealityCheckActivity.this, intent);
            }
        });

        view();//calling view method

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RealityCheckActivity.this,RCUpdateActivity.class);
                intent.putExtra("subject",arrayList.get(i).getSubject());
                intent.putExtra("description",arrayList.get(i).getDescription());
                intent.putExtra("listId",arrayList.get(i).getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dreamlog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutIdDreamJournalActivity:
                about();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void view() {
        Cursor cursor = db.display();
        while (cursor.moveToNext()) {
            RCInformation information = new RCInformation(cursor.getString(0),cursor.getString(1),
                    cursor.getString(2));
            arrayList.add(information);
        }
        Collections.reverse(arrayList);//reversing arrayList for showing data in a proper way

        arrayAdapter = new RCInformationAdapter(this, arrayList);//passing context and arrayList to arrayAdapter
        listView.setAdapter(arrayAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);//setting choice mode
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {//method for multiChoice option

            //checking state Item on Click mode or not
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                String id = arrayList.get(i).getId();//for getting database Id
                //if double click Item color will be white
                if(selectList.contains(id) && count>0){
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                    selectList.remove(id);
                    count--;
                }
                //else item color will be gray
                else{
                    selectList.add(arrayList.get(i).getId());
                    listView.getChildAt(i).setBackgroundColor(Color.GRAY);
                    unDeleteSelect.add(i);//item position storing on new arrayList
                    count++;
                }
                actionMode.setTitle(count+" item selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();//for connecting menu with main menu here
                inflater.inflate(R.menu.selector_layout,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            //this method for taking action like delete,share
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.deleteContextMenuId){
                    for(String i : selectList){
                        db.delete(i);
                        arrayAdapter.remove(i);
                        Toast.makeText(getApplicationContext(),count+" item Deleted",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RealityCheckActivity.this,RealityCheckActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                    arrayAdapter.notifyDataSetChanged();
                    actionMode.finish();
                    count = 0;
                }
                return true;
            }

            //this method for destroying actionMode
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                for(int i: unDeleteSelect){
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);//reset all selected item with gray color
                }
                count = 0;//reset count here
                unDeleteSelect.clear();
                selectList.clear();
            }
        });
    }
    public void about(){
        Intent intent = new Intent(this,InstructionActivity.class);
        startActivity(intent);
    }
}