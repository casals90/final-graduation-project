package com.example.prototypetfgv2.controller;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.view.FragmentProfile;

public class PushReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Log.d("prototypev1", "on receive ");
		
		//Serveix per anar a una activity al clicar la notificacio
		Intent resultIntent = new Intent(context,FragmentProfile.class);
		PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher);
			
          //Crea la notificacio en funcio del json enviat	
		try {

            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            //Esto hace posible crear la notificación
            NotificationCompat.Builder mBuilder1 =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                            .setLights(Color.BLUE, 3000, 3000)
                            .setSound(Uri.parse("uri://sadfasdfasdf.mp3"))
                            .setContentTitle(json.getString("titulo"))
                            .setContentText(json.getString("texto"))
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder1.build());

        } catch (JSONException e) {
            Log.d("prototypev1", "JSONException: " + e.getMessage());
        }
	}
	
}
