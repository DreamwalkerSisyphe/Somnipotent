package com.dream.somnipotent;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base){
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel(){
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if(mManager == null)
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        return mManager;
    }

    public NotificationCompat.Builder sendOnChannelOne(){
        Intent intent = new Intent(this, AddDataActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Wakey Wakey")
                .setContentText("Make sure you remember to log your dreams !")
                .setSmallIcon(R.drawable.ic_wb_sunny_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(resultPendingIntent);
        return notification;
    }

    public NotificationCompat.Builder sendOnChannelTwo(){
        LDESqliteDatabase db;
        ArrayList<LDEInformation> arrayList;
        db = new LDESqliteDatabase(this);
        arrayList=new ArrayList<LDEInformation>();
        SQLiteDatabase sqliteDatabase = db.getWritableDatabase();
        Cursor cursor = db.display();
        while (cursor.moveToNext()) {
            LDEInformation information = new LDEInformation(cursor.getString(0),cursor.getString(1),
                    cursor.getString(2));
            arrayList.add(information);
        }
        Intent intent = new Intent(this, LDEUpdateActivity.class);
        if(arrayList.size() > 0) {
            int randomIndex = (int) (Math.random() * (arrayList.size() - 1) + 0);
            intent.putExtra("subject", arrayList.get(randomIndex).getSubject());
            intent.putExtra("description", arrayList.get(randomIndex).getDescription());
            intent.putExtra("listId", arrayList.get(randomIndex).getId());
        }
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Time for bed soon")
                .setContentText("Try out a lucid dream exercise before bed.")
                .setSmallIcon(R.drawable.ic_airline_seat_individual_suite_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent);
        return notification;
    }

    public NotificationCompat.Builder sendOnChannelThree(){
        RCSqliteDatabase db;
        ArrayList<RCInformation> arrayList;
        db = new RCSqliteDatabase(this);
        arrayList=new ArrayList<RCInformation>();
        SQLiteDatabase sqliteDatabase = db.getWritableDatabase();
        Cursor cursor = db.display();
        while (cursor.moveToNext()) {
            RCInformation information = new RCInformation(cursor.getString(0),cursor.getString(1),
                    cursor.getString(2));
            arrayList.add(information);
        }
        Intent intent = new Intent(this, RCUpdateActivity.class);
        if(arrayList.size() > 0) {
            int randomIndex = (int) (Math.random() * (arrayList.size() - 1) + 0);
            intent.putExtra("subject", arrayList.get(randomIndex).getSubject());
            intent.putExtra("description", arrayList.get(randomIndex).getDescription());
            intent.putExtra("listId", arrayList.get(randomIndex).getId());
        }
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Check Reality!")
                .setContentText("Use a reality check to check if you dreaming.")
                .setSmallIcon(R.drawable.ic_airline_seat_individual_suite_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent);
        return notification;
    }

}
